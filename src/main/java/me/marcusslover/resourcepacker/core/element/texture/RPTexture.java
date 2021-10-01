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

package me.marcusslover.resourcepacker.core.element.texture;

import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.api.ITexture;
import me.marcusslover.resourcepacker.core.resource.ResourcesCache;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RPTexture implements ITexture {
    private static final Factory FACTORY = new Factory();
    private final boolean bitMap;
    private final File image;
    private BufferedImage buffer;

    private RPTexture(File image, boolean bitMap) {
        this.image = image;
        this.bitMap = bitMap;
        try {
            this.buffer = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Public way of creating textures*/
    public static RPTexture of(File file) {
        return FACTORY.setFile(file).create();
    }

    @Override
    public boolean bitMap() {
        return bitMap;
    }

    @Override
    public File file() {
        return image;
    }

    public File copyFile(File dir) {
        try {
            File file = new File(dir, name() + ".png");
            Path path = file.toPath();

            Path copy = Files.copy(image.toPath(), path);
            return copy.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String name() {
        return image.getName().replaceAll("\\.png", "");
    }

    @Override
    public BufferedImage buffer() {
        return buffer;
    }

    /*Internal factory for creating textures*/
    private static class Factory implements IFactory<RPTexture> {
        private File file;
        private boolean bitMap;

        private Factory() {
        }

        private Factory setFile(File file) {
            this.file = file;
            return this;
        }

        private Factory setBitMap(boolean bitMap) {
            this.bitMap = bitMap;
            return this;
        }

        @Override
        public RPTexture create() {
            if (file == null) return null;
            String name = file.getName();

            /*Validate the format*/
            if (!name.endsWith(".png")) throw new InvalidTextureException(file);

            return ResourcesCache.string().get(name, () -> new RPTexture(file, bitMap), RPTexture.class);
        }
    }
}
