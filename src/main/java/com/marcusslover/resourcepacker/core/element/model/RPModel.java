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
import com.marcusslover.resourcepacker.api.IModel;
import com.marcusslover.resourcepacker.core.resource.ResourcesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RPModel implements IModel {
    private static final Factory FACTORY = new Factory();

    private final String name;
    private final RPModelFile modelFile;
    private final RPModelType type;

    public RPModel(@Nullable String name, @NotNull RPModelFile modelFile, @NotNull RPModelType type) {

        this.name = name;
        this.modelFile = modelFile;
        this.type = type;
    }

    public static RPModel of(@Nullable String name, @NotNull RPModelFile modelFile, @NotNull RPModelType type) {
        return FACTORY.setName(name).setModelFile(modelFile).setType(type).create();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public @NotNull RPModelFile modelFile() {
        return modelFile;
    }

    @Override
    public @NotNull RPModelType type() {
        return type;
    }

    @Override
    public boolean isBlock() {
        return type == RPModelType.BLOCK;
    }

    @Override
    public boolean isItem() {
        return type == RPModelType.ITEM;
    }

    /*Internal factory for creating textures*/
    private static class Factory implements IFactory<RPModel> {
        private RPModelFile modelFile;
        private String name;
        private RPModelType modelType;

        private Factory() {
        }

        private Factory setName(@Nullable String name) {
            this.name = name;
            return this;
        }

        private Factory setModelFile(@NotNull RPModelFile modelFile) {
            this.modelFile = modelFile;
            return this;
        }


        private Factory setType(@NotNull RPModelType type) {
            this.modelType = type;
            return this;
        }

        @Override
        public RPModel create() {
            return ResourcesCache.string().get(name, () -> new RPModel(name, modelFile, modelType), RPModel.class);
        }
    }
}
