/*
 * ASIF demuxer, written by Brandon Walters (based on the raw pcm demuxer listed below)
 * CS 3505, Spring 2020
 * 
 * This demuxer is designed to take an .asif file that has been encoded or given to us before and create a singular 
 * packet that contains all of the data making up the .asif file we are encoding, and turn the data from the file 
 * into frames of data that can be encoded into any other audio file format inside of ffmpeg.
 *
 * RAW PCM demuxers
 * Copyright (c) 2002 Fabrice Bellard
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

#include "avformat.h"
#include "internal.h"
#include "pcm.h"
#include "libavutil/log.h"
#include "libavutil/opt.h"
#include "libavutil/avassert.h"

/*
 * This function is designed to probe the file given to this demuxer to check if it is actually an ASIF file.
 * This is done by checking the first 4 bytes of the file, which, on a properly formatted ASIF file, will read as
 * 'asif'. If this is found, we will return a high score to tell ffmpeg that this is an asif file, and to continue
 * demuxing the file. Otherwise, return 0 to notify ffmpeg that this is the incorrect demuxer for the given file, or that
 * the file is corrupt.
 */
static int asif_read_probe (const AVProbeData *p)
{
  if (!memcmp(p->buf, "asif", 4))
    {
      return AVPROBE_SCORE_MAX;
    } 
  return 0;
}

/*
 * This function sets up an AVStream and gives it some basic context, in order to properly stream the packet's data.
 */
static int asif_read_header (AVFormatContext *s)
{
  // Creates a new AVStream using the ffmpeg function avformat_new_stream().
  AVStream *stream = avformat_new_stream(s, NULL);

  if(!stream)
    {
      return AVERROR(ENOMEM);
    }
  
  // Tells the stream that it will be handling an ASIF audio file, and that the stream must be parsed fully.
  stream->codecpar->codec_type = AVMEDIA_TYPE_AUDIO;
  stream->codecpar->codec_id = AV_CODEC_ID_ASIF;
  stream->need_parsing = AVSTREAM_PARSE_FULL;

  return 0;
}

/*
 * This function reads the data from the file into a singular packet containing all of the file's audio samples, as
 * well as the header data.
 */
static int asif_read_packet (AVFormatContext *s, AVPacket *pkt)
{
  int ret, size;
  // This function gives us the size of the file, so that our packet can be allocated appropriately.
  size = avio_size(s->pb);

  // Creates our singular packet and checks to make sure it was properly created. 
  // If the packet was incorrectly allocated, the memory is immediately released again and the error is returned.
  if ((ret = av_append_packet(s->pb, pkt, size)) < 0)
    {
      av_packet_unref(pkt);
      return ret;
    }
  // Returns the size of the created packet.
  return ret;
}

// Our demuxer struct, this lets ffmpeg call our functions properly and associates it with the .asif format.
AVInputFormat ff_asif_demuxer = {
    .name           = "asif",
    .long_name      = NULL_IF_CONFIG_SMALL("ASIF for CS 3505"),
    .priv_data_size = 0,
    .read_probe     = asif_read_probe,
    .read_header    = asif_read_header,
    .read_packet    = asif_read_packet,
    .extensions     = "asif",
    .raw_codec_id   = AV_CODEC_ID_ASIF,
};
