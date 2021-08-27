package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.marcusslover.resourcepacker.api.IGenerator;
import me.marcusslover.resourcepacker.core.internal.RPItemRegistry;
import me.marcusslover.resourcepacker.core.object.item.RPItem;
import me.marcusslover.resourcepacker.core.object.texture.Texture;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;

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
    public void generate(File parent, RPItemRegistry registry) {
        List<RPItem> list = registry.list();

        File minecraftModels = FileUtil.get(parent, "models/item");
        File items = FileUtil.get(parent, "textures/packer/items");
        File models = FileUtil.get(parent, "models/packer/items");

        File file = FileUtil.safeFile(minecraftModels, "paper.json");
        JsonObject fileJson = new JsonObject();
        fileJson.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "item/paper");
        fileJson.add("textures", textures);

        JsonArray variants = new JsonArray(); // 'overrides'
        int customModelData = 700;
        for (RPItem item : list) {
            Texture texture = item.texture();
            String name = texture.name();
            /*Texture*/
            texture.copyFile(items);

            /*Json Item Models*/
            JsonObject model = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("custom_model_data", customModelData);
            model.add("predicate", predicate);
            model.addProperty("model", "packer/items/" + name);

            variants.add(model);
            log.addProperty(name, customModelData);

            /*Texture model*/
            JsonObject textureModel = new JsonObject();
            textureModel.addProperty("parent", "item/generated");
            JsonObject itemTexture = new JsonObject();
            itemTexture.addProperty("layer0", "packer/items/" + name);
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
                textureModel.add("display", displayJson);
            }

            File textureJson = FileUtil.safeFile(models, name + ".json");
            JsonUtil.writeFile(textureJson, textureModel);
            customModelData++;
        }
        fileJson.add("overrides", variants);
        JsonUtil.writeFile(file, fileJson);
    }
}
