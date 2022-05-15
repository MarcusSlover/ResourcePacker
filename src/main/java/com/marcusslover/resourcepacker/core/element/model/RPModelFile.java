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

package com.marcusslover.resourcepacker.core.element.model;

import com.marcusslover.resourcepacker.api.IFactory;
import com.marcusslover.resourcepacker.api.IFile;
import com.marcusslover.resourcepacker.core.element.texture.InvalidFileFormat;
import com.marcusslover.resourcepacker.core.resource.ResourcesCache;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class RPModelFile implements IFile {
    private static final Factory FACTORY = new Factory();
    private final File file;

    private RPModelFile(File file) {
        this.file = file;
    }

    /*Public way of creating model files*/
    public static RPModelFile of(@NotNull File file) {
        return FACTORY.setFile(file).create();
    }

    @Override
    public File copyFile(File dir) {
        return null;
    }

    @Override
    public File file() {
        return file;
    }

    /*Internal factory for creating model files*/
    private static class Factory implements IFactory<RPModelFile> {
        private File file;

        private Factory() {
        }

        private Factory setFile(File file) {
            this.file = file;
            return this;
        }

        @Override
        public RPModelFile create() {
            if (file == null) return null;
            String name = file.getName();

            /*Validate the format*/
            if (!name.endsWith(".json")) throw new InvalidFileFormat(file);

            return ResourcesCache.string().get(name, () -> new RPModelFile(file), RPModelFile.class);
        }
    }
}
