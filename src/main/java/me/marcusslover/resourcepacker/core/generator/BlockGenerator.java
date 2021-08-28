package me.marcusslover.resourcepacker.core.generator;

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

        File blockStates = FileUtil.get(mc, "blockstates");
        File blocks = FileUtil.get(packer, "textures/block");
        File models = FileUtil.get(packer, "models/block");

        File file = FileUtil.safeFile(blockStates, "note_block.json");
        JsonObject fileJson = new JsonObject();
        JsonObject variants = new JsonObject();

        int instrument = 0;
        int note = 0;
        for (RPBlock block : list) {
            if (note > LIMIT) {
                note = 0;
                instrument++;
            }
            RPTexture texture = block.texture();
            String name = texture.name();
            texture.copyFile(blocks); //Copy texture file.

            /*Json Block Models*/
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:block/cube_all");
            JsonObject modelTextures = new JsonObject();
            modelTextures.addProperty("all", "packer:block/" + name);
            model.add("textures", modelTextures);
            File modelFile = FileUtil.safeFile(models, name + ".json");
            JsonUtil.writeFile(modelFile, model); //Create file for model.

            /*Variant creation*/
            JsonObject variant = new JsonObject();
            variant.addProperty("model", "packer:block/" + name);

            String path = "instrument=" + INSTRUMENTS[instrument] + ",note=" + note;
            variants.add(path, variant);
            note++;

            /*Log creation*/
            log.addProperty(name, path);
        }
        fileJson.add("variants", variants);
        JsonUtil.writeFile(file, fileJson);
    }
}
