/*
 * MIT License
 *
 * Copyright (c) 2022 MarcusSlover
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.marcusslover.resourcepacker.util;

import org.jetbrains.annotations.NotNull;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;

public class AudioUtil {
    private AudioUtil() {
    }

    private static final Integer BITRATE = 256000;
    private static final Integer CHANNELS = 2; // Stereo
    private static final Integer RATE = 44100;


    public static void convertToOgg(@NotNull File source, @NotNull File target) {
        try {

            // Attributes.
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libvorbis");
            audio.setBitRate(BITRATE);
            audio.setChannels(CHANNELS);
            audio.setSamplingRate(RATE);

            // Encoding.
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setDecodingThreads(2);
            attrs.setEncodingThreads(2);
            attrs.setInputFormat("mp3");
            attrs.setOutputFormat("ogg");
            attrs.setAudioAttributes(audio);

            // Encode.
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
