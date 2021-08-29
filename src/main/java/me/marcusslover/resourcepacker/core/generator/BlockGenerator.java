package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.api.IGenerator;
import me.marcusslover.resourcepacker.core.internal.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;
import me.marcusslover.resourcepacker.core.object.texture.RPTexture;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;

import java.io.File;
import java.util.List;

public class BlockGenerator implements IGenerator<RPBlock, RPBlockRegistry> {
    private static final Integer LIMIT = 24;
    private static final String[] INSTRUMENTS =
            {
                    "harp", "pling", "banjo", "bit",
                    "didgeridoo", "cow_bell", "iron_xylophone", "xylophone",
                    "chime", "guitar", "bell", "flute",
                    "bass", "hat", "snare", "bassdrum"
            };
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

        int customModelData = 7000;
        int instrument = 0;
        int note = 0;
        for (RPBlock block : list) {
            if (note > LIMIT) {
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

            String blockPath = "instrument=" + INSTRUMENTS[instrument] + ",note=" + note;
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

            note++;
            customModelData++;
            /*Log creation*/
            log.addProperty(name, blockPath);
        }
        /*Block items*/
        itemFileJson.add("overrides", itemVariants);
        JsonUtil.writeFile(itemFile, itemFileJson);

        /*Blocks*/
        blockFileJson.add("variants", blockVariants);
        JsonUtil.writeFile(blockFile, blockFileJson);
    }
}
