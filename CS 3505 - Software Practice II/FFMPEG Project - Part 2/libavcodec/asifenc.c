/*
 * ASIF encoder, written by Brandon Walters (based on the .wav encoder listed below)
 * CS 3505, Spring 2020
 * 
 * This encoder is designed to take frames of data from another format's decoding, and turn the data from the frames
 * into a single packet that contains all of the encoded file's data. This packet will be sent to the muxer to be
 * turned into a proper .asif file to be used inside ffmpeg.
 *
 * WavPack lossless audio encoder
 *
 * This file is part of FFmpeg.
 *
 * FFmpeg is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * FFmpeg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with FFmpeg; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

#define BITSTREAM_WRITER_LE

#include "libavutil/intreadwrite.h"
#include "libavutil/opt.h"
#include "avcodec.h"
#include "internal.h"
#include "put_bits.h"
#include "bytestream.h"

// The struct for our growing array that will contain our file's data.
typedef struct array
{
  int8_t *array;
  int size;
} array;

// Controls the state of our encoder across functions, which lets us keep track of our settings and our file's samples.
typedef struct asif_encoder_data
{
  int number_of_channels;
  int number_of_frames;
  int sample_rate;
  int total_samples;
  
  int last_frame_done;
  int is_first_frame;

  array *growing_array;
} asif_encoder_data;

// Function that allows us to expand our array as needed by using the av_realloc() function.
static void asif_extend_array(array *a, int num_channels) 
{ 
  a->size = a->size + (1000000 * num_channels);
  a->array = (int8_t *)av_realloc(a->array, a->size);
}

// Memory management function that deletes our array after encoding, to stop any memory leaks. Uses the 
// av_free() function.
static void asif_free_array(array *a) 
{
  av_free(a->array);
  a->array = NULL;
}

/*
 * This function intitializes the encoding process, creating our private data structures and setting up our
 * beginning settings, largely using our AVCodecContext structure.
 */
static av_cold int asif_encode_init(AVCodecContext *avctx)
{
  // Setting up our state and basic settings.
  asif_encoder_data *data;
  array *sample_array;
  data = avctx->priv_data;
  avctx->sample_fmt = avctx->codec->sample_fmts[0];

  data->number_of_channels = avctx->channels;
  data->sample_rate = avctx->sample_rate;
  data->is_first_frame = 1;
  data->last_frame_done = 0;
  data->total_samples = 0;
  data->number_of_frames = avctx->frame_number; 

  // Uses av_malloc to give us our starting array to hold our "header data".
  sample_array = av_malloc(sizeof(array));
  sample_array->array = av_malloc(14);
  sample_array->size = 14;
  data->growing_array = sample_array;

  // Creation of our header data and insertion into the array.
  sample_array->array[0] = 'a';
  sample_array->array[1] = 's';
  sample_array->array[2] = 'i';
  sample_array->array[3] = 'f';

  sample_array->array[4] = (int8_t)data->sample_rate;
  sample_array->array[8] = (int8_t)data->number_of_channels;
  sample_array->array[10] = (int8_t)data->total_samples;

  av_log (avctx, AV_LOG_INFO, "ASIF encode INIT\n");

  // Setting our frame size, which is at 1,000,000 samples.
  avctx->frame_size = 1000000;

  return 0;
}

/*
 * This function is used to transfer samples from the given frame into our private data array, converting 
 * the samples into the delta form we would like to have. Once every frame is done, we will have a singular packet
 * containing all of the file's data that we can send to the muxer.
 */
static int asif_send_frame(AVCodecContext *avctx, const AVFrame *frame)
{
  av_log (avctx, AV_LOG_INFO, "ASIF send frame\n");

  asif_encoder_data *data;
  array *sample_array;
  data = avctx->priv_data;
  sample_array = data->growing_array;
  int sample_to_add;
  uint8_t difference;
  int8_t converted_sample;
  uint8_t *subarray;

  // If the frame is not null, it is not the last frame, carry on as normal
  if (frame != NULL)
    {
      // Extend our private array to accomodate these new samples.
      asif_extend_array(&sample_array, data->number_of_channels);
      // If the first frame, we want our first sample from each channel in a different form.
      if (data->is_first_frame == 1)
	{
	  // Nested for loops to scan through each sample of each channel and add it to the private array.
	  for(int i = 0; i < data->number_of_channels - 1; i++)
	    {
	      subarray = frame->extended_data[i];

	      sample_array->array[14 + data->total_samples] = subarray[0];

	      for(int j = 1; j < frame->nb_samples - 1; j++)
		{ 
		  sample_to_add = subarray[j] - subarray[j - 1];

		  // If the sample is too high or too low, clamp it and add the difference to catch up later.
		  if(sample_to_add > 127)
		    {
		      difference = difference + (sample_to_add - 127);
		      sample_to_add = 127;
		    }

		  else if(sample_to_add < -128)
		    {
		      difference = difference + (sample_to_add + 128);
		      sample_to_add = -128;
		    }

		  converted_sample = (int8_t) sample_to_add;
		  sample_array->array[14 + (1000000 * i) + j] = converted_sample;
		}
	    }
	  data->is_first_frame = 0;
	}

      // If not the first frame, go without the first sample's different format
      else
        {
	  // Nested for loops to scan through each sample of each channel and add it to the private array.
	  for(int i = 0; i < data->number_of_channels - 1; i++)
	    {
	      subarray = frame->extended_data[i];

	      for(int j = 0; j < frame->nb_samples - 1; j++)
		{
		  sample_to_add = subarray[j] - subarray[j - 1];

		  // If the sample is too high or too low, clamp it and add the difference to catch up later.
		  if(sample_to_add > 127)
		    {
		      difference = difference + (sample_to_add - 127);
		      sample_to_add = 127;
		    }

		  else if(sample_to_add < -128)
		    {
		      difference = difference + (sample_to_add + 128);
		      sample_to_add = -128;
		    }

		  converted_sample = (int8_t) sample_to_add;
		  sample_array->array[14 + (1000000 * i) + j] = converted_sample;

		}
	    }
	}
      // Increments the number of frames and the total samples processed.
      data->number_of_frames += 1;
      data->total_samples += frame->nb_samples;
    }

  // If the frame was null, mark as done for receive_packet.
  else
    {
      data->last_frame_done = 1;
    }

  return 0;
}

/*
 * This function takes our collected data from our private array and creates a singular packet holding
 * all of the file's data. We will then send this packet to the muxer for final writing to an .asif file.
 */
static int asif_receive_packet(AVCodecContext *avctx, AVPacket *avpkt)
{
  av_log (avctx, AV_LOG_INFO, "ASIF receive packet\n");

  asif_encoder_data *data;
  data = avctx->priv_data;
  array *sample_array;
  sample_array = data->growing_array;
  int ret;
  int final_total;
  int packet_created = 0;

  // If the last frame has not yet been received, continue to wait.
  if (data->last_frame_done != 1)
    {
      return AVERROR(EAGAIN);
    }
  
  // If the last frame is received and the packet has not yet been created, create the packet with our total
  // size calculation and the ff_alloc_packet2 function from ffmpeg.
  else if (packet_created == 0)
    {
      final_total = data->total_samples * data->number_of_channels + 14;

      if ((ret = ff_alloc_packet2(avctx, avpkt, final_total, final_total)) < 0)
        return ret;

      sample_array = avpkt->data;
      packet_created = 1;
      return 0;
    }
  
  // If the packet has already been created, return an EOF to tell ffmpeg to break the encoding loop and go to muxer.
  else 
    {
      return AVERROR(EOF);
    }
}

/*
 * This function frees up the memory used by our private data and closes the encoder.
 */
static av_cold int asif_encode_close(AVCodecContext *avctx)
{
  asif_encoder_data *data;
  data = avctx->priv_data;
  array *sample_array;
  sample_array = data->growing_array;

  av_log (avctx, AV_LOG_INFO, "ASIF encode CLOSE");
  asif_free_array(&sample_array);
  return 0;
}
// Our encoder struct, lets ffmpeg know to use this encoder for .asif files and sets up some basic settings.
AVCodec ff_asif_encoder = {
    .name           = "asif",
    .priv_data_size = sizeof(asif_encoder_data),
    .long_name      = NULL_IF_CONFIG_SMALL("ASIF for CS 3505"),
    .type           = AVMEDIA_TYPE_AUDIO,
    .id             = AV_CODEC_ID_ASIF,
    .init           = asif_encode_init,
    .send_frame     = asif_send_frame,
    .receive_packet = asif_receive_packet,
    .close          = asif_encode_close,
    .capabilities   = AV_CODEC_CAP_VARIABLE_FRAME_SIZE | AV_CODEC_CAP_DELAY,
    .sample_fmts    = (const enum AVSampleFormat[]){ AV_SAMPLE_FMT_U8P,
                                                     AV_SAMPLE_FMT_NONE },
};
