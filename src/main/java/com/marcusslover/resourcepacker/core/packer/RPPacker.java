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

package com.marcusslover.resourcepacker.core.packer;

import com.marcusslover.resourcepacker.api.IPacker;
import com.marcusslover.resourcepacker.core.registry.*;
import com.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Used publicly to register all blocks and items.
 */
public class RPPacker implements IPacker {
    private static RPPacker instance;

    final RPBlockRegistry blockRegistry;
    final RPItemRegistry itemRegistry;
    final RPSoundRegistry soundRegistry;
    final RPFontRegistry fontRegistry;
    final RPMenuRegistry menuRegistry;

    final ResourceHelper resources;
    final File output;

    RPMode mode;
    String name;
    String logo;
    String prefix;
    List<String> description;

    /*Only for internal creation*/
    RPPacker(File resources, File output) {
        instance = this;
        this.resources = new ResourceHelper(resources);
        this.output = output;

        mode = RPMode.AUTOMATIC;
        name = "Packer";
        logo = null;
        prefix = "packer";
        description = new ArrayList<>();
        description.add("Created with ResourcePacker");

        blockRegistry = new RPBlockRegistry();
        itemRegistry = new RPItemRegistry();
        soundRegistry = new RPSoundRegistry();
        fontRegistry = new RPFontRegistry();
        menuRegistry = new RPMenuRegistry();

    }

    public static RPPacker get() {
        return instance;
    }

    @Override
    public RPBlockRegistry blocks() {
        return blockRegistry;
    }

    @Override
    public RPItemRegistry items() {
        return itemRegistry;
    }

    @Override
    public RPSoundRegistry sounds() {
        return soundRegistry;
    }

    @Override
    public RPFontRegistry fonts() {
        return fontRegistry;
    }

    @Override
    public RPMenuRegistry menus() {
        return menuRegistry;
    }

    @Override
    public void setLogo(String path) {
        logo = path;
    }

    @Override
    public String logo() {
        return logo;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    public List<String> description() {
        return description;
    }

    @Override
    public ResourceHelper resources() {
        return resources;
    }

    @Override
    public File output() {
        return output;
    }

    @Override
    public void setMode(RPMode mode) {
        this.mode = mode;
    }

    @Override
    public RPMode mode() {
        return mode;
    }
}
