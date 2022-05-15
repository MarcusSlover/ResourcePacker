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

package com.marcusslover.resourcepacker.core.element.block;

import com.marcusslover.resourcepacker.api.IBlock;
import com.marcusslover.resourcepacker.api.IFactory;
import com.marcusslover.resourcepacker.core.element.model.RPModel;
import com.marcusslover.resourcepacker.core.element.texture.RPTexture;
import com.marcusslover.resourcepacker.core.resource.RPResource;
import com.marcusslover.resourcepacker.core.resource.ResourcesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RPBlock implements IBlock {
    private static final Factory FACTORY = new Factory();
    @Nullable
    private final String name;
    @NotNull
    private final RPTexture texture;
    @NotNull
    private final RPState state;
    private RPModel model;

    private RPBlock(@Nullable String name, @NotNull RPTexture texture) {
        this.name = name;
        this.texture = texture;
        this.state = new RPState();
    }

    /*Public way of creating blocks*/
    public static RPBlock of(@Nullable String name, @NotNull RPResource textureResource) {
        if (textureResource.getType() == RPResource.Type.IMAGE) {
            RPTexture texture = RPTexture.of(textureResource.getFile());
            return of(name, texture);
        }
        return null;
    }

    /*Public way of creating blocks*/
    public static RPBlock of(@Nullable String name, @NotNull RPTexture texture) {
        return FACTORY.setName(name).setTexture(texture).create();
    }

    @Nullable
    @Override
    public String name() {
        return name == null ? texture.name() : name;
    }

    @Override
    public RPTexture texture() {
        return texture;
    }

    @Override
    public RPState data() {
        return state;
    }

    @Override
    @NotNull
    public RPBlock model(@Nullable RPModel model) {
        this.model = model;
        return this;
    }

    @Override
    public RPModel model() {
        return model;
    }

    /*Internal factory for creating blocks*/
    private static class Factory implements IFactory<RPBlock> {

        private RPTexture texture;
        private String name;


        private Factory() {
        }

        @NotNull
        private Factory setName(@Nullable String name) {
            this.name = name;
            return this;
        }

        @NotNull
        private Factory setTexture(@NotNull RPTexture texture) {
            this.texture = texture;
            return this;
        }

        @Override
        public RPBlock create() {
            return ResourcesCache.string().get(name, () -> new RPBlock(name, texture), RPBlock.class);
        }
    }
}
