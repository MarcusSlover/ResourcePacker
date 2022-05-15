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

import com.marcusslover.resourcepacker.api.IManager;
import com.marcusslover.resourcepacker.core.element.item.RPMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds block states data.
 */
public class RPState extends RPMeta implements IManager<RPState.Element> {
    private final List<Element> elements = new ArrayList<>();

    @Override
    public void add(Element obj) {
        elements.add(obj);
    }

    @Override
    public List<Element> list() {
        return elements;
    }

    public static class Element {
        public final String key;
        public final String value;

        public Element(String key, String value) {

            this.key = key;
            this.value = value;
        }
    }
}
