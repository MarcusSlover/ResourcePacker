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

package com.marcusslover.resourcepacker.core.element.sound;

import com.marcusslover.resourcepacker.api.IFactory;
import com.marcusslover.resourcepacker.api.ISound;
import com.marcusslover.resourcepacker.core.resource.RPResource;
import com.marcusslover.resourcepacker.core.resource.ResourcesCache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//todo: future implementation of ogg AND oga files
public class RPSound implements ISound {
    private static final RPSound.Factory FACTORY = new RPSound.Factory();
    private final File sound;
    private final String format;

    private RPSound(File sound) {
        this.sound = sound;
        this.format = sound.getName().split("\\.")[1];
    }

    /*Public way of creating sounds*/
    public static RPSound of(RPResource resource) {
        if (resource.getType() == RPResource.Type.SOUND) {
            return of(resource.getFile());
        }
        return null;
    }

    public static RPSound of(File file) {
        return FACTORY.setFile(file).create();
    }

    public String getFormat() {
        return format;
    }

    public File copyFile(File dir) {
        try {
            File file = new File(dir, name() + ".ogg");
            Path path = file.toPath();

            if (this.format.equals("ogg")) {
                Path copy = Files.copy(sound.toPath(), path);
                return copy.toFile();
            } else {
                if (file.exists()) file.createNewFile();
                //AudioUtil.convertToOgg(sound, file); removed since its unnecessary
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String name() {
        return sound.getName() //this is kind of a mess
                .split("\\.")[0]
                .replaceAll("\\s+", "")
                .replaceAll("_", "")
                .toLowerCase();
    }

    @Override
    public File file() {
        return sound;
    }

    /*Internal factory for creating textures*/
    private static class Factory implements IFactory<RPSound> {
        private File file;

        private Factory() {
        }

        private RPSound.Factory setFile(File file) {
            this.file = file;
            return this;
        }

        @Override
        public RPSound create() {
            if (file == null) return null;
            String name = file.getName();

            /*Validate the format*/
            if (!name.endsWith(".ogg") /*&& !name.endsWith(".mp3")*/) throw new InvalidSoundException(file);

            return ResourcesCache.string().get(name, () -> new RPSound(file), RPSound.class);
        }
    }
}
