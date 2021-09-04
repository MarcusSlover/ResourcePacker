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

package me.marcusslover.resourcepacker.core.resource;

import me.marcusslover.resourcepacker.api.IResources;

import java.io.File;

public record ResourceHelper(File parent) implements IResources {

    @Override
    public RPResource get(String dir, String child) {
        if (child.endsWith(".png")) {
            File file;
            if (dir != null) {
                File directory = new File(parent, dir);
                if (!directory.exists()) throw new InvalidResourceFile(directory.toString());
                file = new File(directory, child);
            } else {
                file = new File(parent, child);
            }
            if (!file.exists()) throw new InvalidResourceFile(child);
            return new RPResource(file, RPResource.Type.IMAGE);
        }
        return null;
    }

    public RPResource block(String s) {
        return get("blocks", s);
    }

    public RPResource item(String s) {
        return get("items", s);
    }

    public RPResource frame(String s) {
        return get("itemframes", s);
    }
}
