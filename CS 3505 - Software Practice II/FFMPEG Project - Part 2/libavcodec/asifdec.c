/*
 * ASIF decoder, written by Brandon Walters (based on the .wav decoder listed below)
 * CS 3505, Spring 2020
 * 
 * This decoder is designed to take a singular packet that contains all of the data making up the .asif file
 * we were given from the demuxer, and turn the data from the file into frames of data that can be encoded 
 * into any other audio file format inside of ffmpeg.
 *
 * WavPack lossless audio decoder
 * Copyright (c) 2006,2011 Konstantin Shishkov
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

#include "libavutil/channel_layout.h"

#define BITSTREAM_READER_LE
#include "avcodec.h"
#include "bytestream.h"
#include "get_bits.h"
#include "internal.h"
#include "thread.h"
#include "unary.h"
#include "wavpack.h"

/*
 * This function provides all of our decoding operations, turning our singular packet into frames of data that
 * can be used by another format's encoder inside ffmpeg.
 */
static int asif_decode(AVCodecContext *avctx, void *data,
                       int *got_frame_ptr, AVPacket *avpkt)
{
  av_log (avctx, AV_LOG_INFO, "ASIF decode INIT\n");
  AVFrame *frame;
  int ret;

  // Creates and assigns our frame that will be filled with data.
  frame = data;

  // Creates our header information from the backing data array in the packet, to give to the frame
  uint32_t sample_rate = avpkt->data[4] | (avpkt->data[5] << 8) | (avpkt->data[6] << 16) | (avpkt->data[7] << 24);
  uint32_t nb_samples = avpkt->data[10] | (avpkt->data[11] << 8) | (avpkt->data[12] << 16) | (avpkt->data[13] << 24);
  uint16_t channels = avpkt->data[8] | (avpkt->data[9] << 8);

  // Populate frame samples, channel, format, etc.
  frame->format = avctx->sample_fmt;
  frame->nb_samples = nb_samples;
  frame->sample_rate = sample_rate;
  frame->channels = channels;

  // Creates our buffer for the frame
  if ((ret = ff_get_buffer(avctx, frame, 0)) < 0)
    {
      return ret;
    }

  // Move data samples in
  for(int i = 0; i < (frame->nb_samples * frame->channels); i++)
    {
      if (i == 0)
	{
	  frame->data[i] = avpkt->data[14 + i];
	}
      // After the first sample, corrects the samples back from the delta format to regular audio.
      else
	{
	  frame->data[i] = avpkt->data[14 + i] + 127;
	}
    }

  // Informs ffmpeg of the completion of a frame, then returns the size of the packet that was used in the packet. 
  // Since we are only building one frame, we return the entire packet's size here.
  *got_frame_ptr = 1;
  return avpkt->size;
}
// Our decoder struct, lets ffmpeg know of our decoder and allows it to be properly used in the decoding process.
AVCodec ff_asif_decoder = {
    .name           = "asif",
    .long_name      = NULL_IF_CONFIG_SMALL("ASIF for CS 3505"),
    .type           = AVMEDIA_TYPE_AUDIO,
    .id             = AV_CODEC_ID_ASIF,
    .decode         = asif_decode,
    .capabilities   = AV_CODEC_CAP_DR1,
};
