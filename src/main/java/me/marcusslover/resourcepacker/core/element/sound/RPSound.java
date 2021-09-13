/*
 * MIT License
 *
 * Copyright (c) 2021 MarcusSlover
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

package me.marcusslover.resourcepacker.core.element.sound;

import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.api.ISound;
import me.marcusslover.resourcepacker.core.resource.RPResource;
import me.marcusslover.resourcepacker.core.resource.ResourcesCache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RPSound implements ISound {
    private static final RPSound.Factory FACTORY = new RPSound.Factory();
    private final File sound;

    private RPSound(File sound) {
        this.sound = sound;
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

    public File copyFile(File dir) {
        try {
            File file = new File(dir, name() + ".ogg");
            Path path = file.toPath();

            Path copy = Files.copy(sound.toPath(), path);
            return copy.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String name() {
        return sound.getName().replaceAll("\\.ogg", "");
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
            if (!name.endsWith(".ogg")) throw new InvalidSoundException(file);

            return ResourcesCache.string().get(name, () -> new RPSound(file), RPSound.class);
        }
    }
}