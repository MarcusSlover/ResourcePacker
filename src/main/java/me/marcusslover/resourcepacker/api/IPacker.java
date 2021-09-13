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

package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.packer.RPMode;
import me.marcusslover.resourcepacker.core.registry.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.registry.RPItemRegistry;
import me.marcusslover.resourcepacker.core.registry.RPSoundRegistry;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.io.File;
import java.util.List;

public interface IPacker {
    RPBlockRegistry blocks();

    RPItemRegistry items();

    RPSoundRegistry sounds();

    void setLogo(String path);

    String logo();

    void setName(String name);

    String name();

    void setPrefix(String prefix);

    String prefix();

    void setDescription(List<String> description);

    List<String> description();

    ResourceHelper resources();

    File output();

    void setMode(RPMode RPMode);

    RPMode mode();
}
