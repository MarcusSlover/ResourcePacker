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

package me.marcusslover.resourcepacker.core.element.font;

import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.api.IFont;
import me.marcusslover.resourcepacker.core.element.texture.RPTexture;
import me.marcusslover.resourcepacker.core.resource.RPResource;
import me.marcusslover.resourcepacker.core.resource.ResourcesCache;

public class RPFont implements IFont {
    private static final RPFont.Factory FACTORY = new RPFont.Factory();
    private final String name;
    private final RPTexture texture;

    private RPFont(String name, RPTexture texture) {
        this.name = name;
        this.texture = texture;
    }

    public static RPFont of(String name, RPResource resource) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            RPTexture texture = RPTexture.of(resource.getFile());
            return of(name, texture);
        }
        return null;
    }

    public static RPFont of(String name, RPTexture texture) {
        return FACTORY.setName(name).setTexture(texture).create();
    }

    @Override
    public String name() {
        return name == null ? texture.name() : name;
    }

    @Override
    public RPTexture texture() {
        return texture;
    }

    private static class Factory implements IFactory<RPFont> {
        private RPTexture texture;
        private String name;

        private Factory() {
        }

        private Factory setName(String name) {
            this.name = name;
            return this;
        }

        private Factory setTexture(RPTexture texture) {
            this.texture = texture;
            return this;
        }

        @Override
        public RPFont create() {
            return ResourcesCache.string().get(name, () -> new RPFont(name, texture), RPFont.class);
        }
    }

}