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

package me.marcusslover.resourcepacker.core.resource;

import me.marcusslover.resourcepacker.api.IResources;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public final class ResourceHelper implements IResources {
    private final File parent;

    public ResourceHelper(@NotNull File parent) {
        this.parent = parent;
    }

    @NotNull
    public File parent() {
        return parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ResourceHelper that = (ResourceHelper) obj;
        return Objects.equals(this.parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent);
    }

    @Override
    public String toString() {
        return "ResourceHelper[" +
                "parent=" + parent + ']';
    }

    @Override
    public RPResource get(String dir, String child) {
        File file;
        if (dir != null) {
            File directory = new File(parent, dir);
            if (!directory.exists()) throw new InvalidResourceFile(directory.toString());
            file = new File(directory, child);
        } else {
            file = new File(parent, child);
        }
        if (!file.exists()) throw new InvalidResourceFile(child);

        RPResource.Type type = null;
        if (child.endsWith(".png")) {
            type = RPResource.Type.IMAGE;
        }
        if (child.endsWith(".ogg") || child.endsWith(".mp3")) { //mp3 is acceptable because it will be converted to .ogg
            type = RPResource.Type.SOUND;
        }
        if (child.endsWith(".json")) {
            type = RPResource.Type.JSON;
        }
        return new RPResource(file, type);
    }
}
