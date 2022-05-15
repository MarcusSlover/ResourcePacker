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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marcusslover.resourcepacker.util.JsonUtil;
import com.marcusslover.resourcepacker.api.IGenerator;
import com.marcusslover.resourcepacker.core.element.item.RPItem;
import com.marcusslover.resourcepacker.core.element.texture.RPTexture;
import com.marcusslover.resourcepacker.core.registry.RPItemRegistry;
import com.marcusslover.resourcepacker.util.FileUtil;

import java.io.File;
import java.util.List;

public class ItemGenerator implements IGenerator<RPItem, RPItemRegistry> {
    private final JsonObject log;

    ItemGenerator() {
        log = new JsonObject();
    }

    @Override
    public JsonObject log() {
        return log;
    }

    @Override
    public void generate(File mc, File packer, RPItemRegistry registry) {
        List<RPItem> list = registry.list();

        File minecraftModels = FileUtil.get(mc, "models/item");
        File items = FileUtil.get(packer, "textures/item");
        File models = FileUtil.get(packer, "models/item");

        File file = FileUtil.safeFile(minecraftModels, "paper.json");
        JsonObject fileJson = new JsonObject();
        JsonArray variants = new JsonArray(); // 'overrides'
        fileJson.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "item/paper");
        fileJson.add("textures", textures);

        int customModelData = 1;
        for (RPItem item : list) {
            /*Assign model data*/
            item.data().setCustomModelData(customModelData);

            RPTexture texture = item.texture();
            String name = texture.name();
            /*Texture*/
            texture.copyFile(items);

            /*Json Item Models*/
            JsonObject model = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("custom_model_data", customModelData);
            model.add("predicate", predicate);
            model.addProperty("model", "packer:item/" + name);

            variants.add(model);
            log.addProperty(name, customModelData);

            /*Texture model*/
            JsonObject textureModel = new JsonObject();
            textureModel.addProperty("parent", "item/generated");
            JsonObject itemTexture = new JsonObject();
            itemTexture.addProperty("layer0", "packer:item/" + name);
            textureModel.add("textures", itemTexture);

            if (item.itemFrame()) { // Item frame stuff.
                int height = texture.height();
                int width = texture.width();

                double[] scale = {1.0, 1.0, 1.0};
                if (height > width) {
                    scale[1] = 4.0;
                    scale[0] = scale[1] * ((double) width / height);
                } else {
                    scale[0] = 4.0;
                    scale[1] = scale[0] * ((double) height / width);
                }
                JsonArray scaleArray = new JsonArray();
                for (double value : scale) scaleArray.add(value);

                double[] rotation = {0.0, 180.0, 0.0};
                JsonArray rotationArray = new JsonArray();
                for (double value : rotation) rotationArray.add(value);

                JsonObject displayJson = new JsonObject();
                JsonObject child = new JsonObject();
                child.add("rotation", rotationArray);
                child.add("scale", scaleArray);
                displayJson.add("fixed", child);
                displayJson.add("head", child);
                textureModel.add("display", displayJson);
            }

            File textureJson = FileUtil.safeFile(models, name + ".json");
            JsonUtil.writeFile(textureJson, textureModel);
            customModelData++;
        }
        /*Default variant*/
        /*Json Item Models*/
        JsonObject model = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty("custom_model_data", customModelData);
        model.add("predicate", predicate);
        model.addProperty("model", "minecraft:item/paper");
        variants.add(model);

        fileJson.add("overrides", variants);
        JsonUtil.writeFile(file, fileJson);
    }
}
