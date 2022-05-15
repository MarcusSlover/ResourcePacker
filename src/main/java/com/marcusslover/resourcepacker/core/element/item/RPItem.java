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

package com.marcusslover.resourcepacker.core.element.item;

import com.marcusslover.resourcepacker.api.IFactory;
import com.marcusslover.resourcepacker.api.IItem;
import com.marcusslover.resourcepacker.core.element.model.RPModel;
import com.marcusslover.resourcepacker.core.element.texture.RPTexture;
import com.marcusslover.resourcepacker.core.resource.RPResource;
import com.marcusslover.resourcepacker.core.resource.ResourcesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RPItem implements IItem {
    private static final RPItem.Factory FACTORY = new RPItem.Factory();
    private final String name;
    private final RPTexture texture;
    private final boolean itemFrame;
    private final RPMeta meta;
    private RPModel model;

    private RPItem(String name, RPTexture texture) {
        this(name, texture, false);
    }

    private RPItem(String name, RPTexture texture, boolean itemFrame) {
        this.name = name;
        this.texture = texture;
        this.itemFrame = itemFrame;
        this.meta = new RPMeta();
    }

    /*Public way of creating blocks*/
    public static RPItem of(String name, RPResource resource) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            RPTexture texture = RPTexture.of(resource.getFile());
            return of(name, texture, false);
        }
        return null;
    }

    public static RPItem of(String name, RPResource resource, boolean itemFrame) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            RPTexture texture = RPTexture.of(resource.getFile());
            return of(name, texture, itemFrame);
        }
        return null;
    }

    public static RPItem of(String name, RPTexture texture, boolean itemFrame) {
        return FACTORY
                .setName(name)
                .setTexture(texture)
                .setItemFrame(itemFrame)
                .create();
    }

    @Override
    @NotNull
    public RPItem model(@Nullable RPModel model) {
        this.model = model;
        return this;
    }

    @Override
    public @NotNull RPModel model() {
        return model;
    }


    @Override
    public String name() {
        return name == null ? texture.name() : name;
    }

    @Override
    public RPTexture texture() {
        return texture;
    }

    @Override
    public RPMeta data() {
        return meta;
    }

    @Override
    public boolean itemFrame() {
        return itemFrame;
    }

    /*Internal factory for creating blocks*/
    private static class Factory implements IFactory<RPItem> {

        private RPTexture texture;
        private String name;
        private boolean itemFrame;

        private Factory() {
        }

        private RPItem.Factory setName(String name) {
            this.name = name;
            return this;
        }

        private RPItem.Factory setTexture(RPTexture texture) {
            this.texture = texture;
            return this;
        }

        private RPItem.Factory setItemFrame(boolean itemFrame) {
            this.itemFrame = itemFrame;
            return this;
        }


        @Override
        public RPItem create() {
            return ResourcesCache.string().get(name, () -> new RPItem(name, texture, itemFrame), RPItem.class);
        }
    }
}
