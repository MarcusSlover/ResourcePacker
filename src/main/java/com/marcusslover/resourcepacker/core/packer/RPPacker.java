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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Used publicly to register all blocks and items.
 */
public class RPPacker implements IPacker {
    private static RPPacker instance;

    @NotNull
    final RPBlockRegistry blockRegistry;
    @NotNull
    final RPItemRegistry itemRegistry;
    @NotNull
    final RPSoundRegistry soundRegistry;
    @NotNull
    final RPFontRegistry fontRegistry;
    @NotNull
    final RPMenuRegistry menuRegistry;

    @NotNull
    final ResourceHelper resources;
    @NotNull
    final File output;

    @NotNull
    RPMode mode;
    @Nullable
    String name;
    @Nullable
    String logo;
    @Nullable
    String prefix;
    @Nullable
    List<String> description;

    /*Only for internal creation*/
    RPPacker(@NotNull File resources, @NotNull File output) {
        instance = this;
        this.resources = new ResourceHelper(resources);
        this.output = output;

        mode = RPMode.MANUAL_REGISTRATION;
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

    @NotNull
    @Override
    public RPBlockRegistry blocks() {
        return blockRegistry;
    }

    @NotNull
    @Override
    public RPItemRegistry items() {
        return itemRegistry;
    }

    @NotNull
    @Override
    public RPSoundRegistry sounds() {
        return soundRegistry;
    }

    @NotNull
    @Override
    public RPFontRegistry fonts() {
        return fontRegistry;
    }

    @NotNull
    @Override
    public RPMenuRegistry menus() {
        return menuRegistry;
    }

    @Override
    public void setLogo(@Nullable String path) {
        logo = path;
    }

    @Nullable
    @Override
    public String logo() {
        return logo;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String name() {
        return name;
    }

    @Override
    public void setPrefix(@Nullable String prefix) {
        this.prefix = prefix;
    }

    @Nullable
    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public void setDescription(@Nullable List<String> description) {
        this.description = description;
    }

    @Nullable
    @Override
    public List<String> description() {
        return description;
    }

    @NotNull
    @Override
    public ResourceHelper resources() {
        return resources;
    }

    @NotNull
    @Override
    public File output() {
        return output;
    }

    @Override
    public void setMode(@NotNull RPMode mode) {
        this.mode = mode;
    }

    @NotNull
    @Override
    public RPMode mode() {
        return mode;
    }
}
