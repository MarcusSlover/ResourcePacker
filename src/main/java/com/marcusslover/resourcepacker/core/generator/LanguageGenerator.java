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

package com.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonObject;
import com.marcusslover.resourcepacker.core.element.block.RPBlock;
import com.marcusslover.resourcepacker.core.element.item.RPItem;
import com.marcusslover.resourcepacker.core.registry.RPBlockRegistry;
import com.marcusslover.resourcepacker.core.registry.RPItemRegistry;
import com.marcusslover.resourcepacker.util.FileUtil;
import com.marcusslover.resourcepacker.util.JsonUtil;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;

public class LanguageGenerator {
    public void generate(File packer, RPItemRegistry items, RPBlockRegistry blocks) {
        File lang = FileUtil.safeDir(packer, "lang");
        JsonObject json = new JsonObject();

        for (RPItem item : items.list()) {
            String uniqueId = "packer.item." + item.data().customModelData();
            String name = item.name().replaceAll("_", " ");
            json.addProperty(uniqueId, WordUtils.capitalizeFully(name));
        }

        for (RPBlock block : blocks.list()) {
            String uniqueId = "packer.block." + block.data().customModelData();
            String name = block.name().replaceAll("_", " ");
            json.addProperty(uniqueId, WordUtils.capitalizeFully(name));
        }

        File langFile = FileUtil.safeFile(lang, "en_us.json");
        JsonUtil.writeFile(langFile, json);

    }
}
