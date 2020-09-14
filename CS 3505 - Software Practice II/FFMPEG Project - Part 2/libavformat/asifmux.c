/*
 * ASIF muxer, written by Brandon Walters (based on the raw pcm muxer listed below)
 * CS 3505, Spring 2020
 * 
 * This muxer is designed to take a singular packet that contains all of the data making up the .asif file we are
 * encoding, and write it into a file that can be used/played by ffmpeg and other applications.
 *
 * RAW PCM muxers
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
#include "rawenc.h"

/*
 * This method takes our singular packet given from the ASIF encoder written in asifenc.c and creates a
 * file that can be used within ffmpeg and/or played through ffplay. This is accomplished using the avio_write
 * function provided by our AVFormatContext. Returns 0 to indicate success.
 */
static int asif_write_packet(AVFormatContext *s, AVPacket *pkt)
{
  avio_write(s->pb, pkt->data, pkt->size);
  return 0;
}

/*
 * Our muxer struct. This gives ffmpeg the context needed to properly call this file and its method.
 */
AVOutputFormat ff_asif_muxer = {
    .name         = "asif",
    .long_name    = NULL_IF_CONFIG_SMALL("ASIF for CS 3505"),
    .extensions   = "asif",
    .audio_codec  = AV_CODEC_ID_ASIF,
    .video_codec  = AV_CODEC_ID_NONE,
    .write_packet = asif_write_packet,
    .flags        = AVFMT_NOTIMESTAMPS,
};
