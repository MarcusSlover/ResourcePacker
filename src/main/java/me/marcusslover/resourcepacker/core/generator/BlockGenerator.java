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

package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.api.IGenerator;
import me.marcusslover.resourcepacker.core.element.block.RPBlock;
import me.marcusslover.resourcepacker.core.element.texture.RPTexture;
import me.marcusslover.resourcepacker.core.registry.RPBlockRegistry;
import me.marcusslover.resourcepacker.util.BlockUtil;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;

import java.io.File;
import java.util.List;

public class BlockGenerator implements IGenerator<RPBlock, RPBlockRegistry> {
    private final JsonObject log;

    BlockGenerator() {
        log = new JsonObject();
    }

    @Override
    public JsonObject log() {
        return log;
    }

    @Override
    public void generate(File mc, File packer, RPBlockRegistry registry) {
        List<RPBlock> list = registry.list();

        File minecraftModels = FileUtil.get(mc, "models/item");
        File blockStates = FileUtil.get(mc, "blockstates");
        File blocks = FileUtil.get(packer, "textures/block");
        File blockModels = FileUtil.get(packer, "models/block");
        File itemModels = FileUtil.get(packer, "models/item");

        File blockFile = FileUtil.safeFile(blockStates, "note_block.json");
        JsonObject blockFileJson = new JsonObject();
        JsonObject blockVariants = new JsonObject();

        File itemFile = FileUtil.safeFile(minecraftModels, "note_block.json");
        JsonObject itemFileJson = new JsonObject();
        JsonArray itemVariants = new JsonArray(); // 'overrides'
        itemFileJson.addProperty("parent", "minecraft:block/note_block");

        int customModelData = 1;
        int instrument = 0;
        int note = 0;
        boolean powered = false;
        for (RPBlock block : list) {
            if (note > BlockUtil.NOTE_LIMIT) {
                note = 0;
                instrument++;
            }
            /*Assign model data*/
            block.data().setCustomModelData(customModelData);

            RPTexture texture = block.texture();
            String name = texture.name();
            texture.copyFile(blocks); //Copy texture file.

            /*Json block model*/
            JsonObject blockModel = new JsonObject();
            blockModel.addProperty("parent", "minecraft:block/cube_all");
            JsonObject blockTextures = new JsonObject();
            blockTextures.addProperty("all", "packer:block/" + name);
            blockModel.add("textures", blockTextures);
            File blockModelFile = FileUtil.safeFile(blockModels, name + ".json");
            JsonUtil.writeFile(blockModelFile, blockModel); //Create file for model.

            /*Block variant creation*/
            JsonObject blockVariant = new JsonObject();
            blockVariant.addProperty("model", "packer:block/" + name);

            String blockPath = "instrument=" + BlockUtil.INSTRUMENTS[instrument] + ",note=" + note + ",powered=" + powered;
            blockVariants.add(blockPath, blockVariant);

            /*Json item model*/
            JsonObject itemModel = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("custom_model_data", customModelData);
            itemModel.add("predicate", predicate);
            itemModel.addProperty("model", "packer:item/" + name);
            itemVariants.add(itemModel);

            /*Texture model*/
            JsonObject textureModel = new JsonObject();
            textureModel.addProperty("parent", "minecraft:block/cube_all");
            JsonObject itemTexture = new JsonObject();
            itemTexture.addProperty("all", "packer:block/" + name);
            textureModel.add("textures", itemTexture);
            File textureJson = FileUtil.safeFile(itemModels, name + ".json");
            JsonUtil.writeFile(textureJson, textureModel);

            if (powered) note++;
            powered = !powered;
            customModelData++;
            /*Log creation*/
            log.addProperty(name, blockPath);
        }
        /*Default variant*/
        JsonObject itemModel = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty("custom_model_data", customModelData);
        itemModel.add("predicate", predicate);
        itemModel.addProperty("model", "minecraft:item/note_block");
        itemVariants.add(itemModel);

        /*Block items*/
        itemFileJson.add("overrides", itemVariants);
        JsonUtil.writeFile(itemFile, itemFileJson);

        /*Blocks*/
        blockFileJson.add("variants", blockVariants);
        JsonUtil.writeFile(blockFile, blockFileJson);
    }
}
