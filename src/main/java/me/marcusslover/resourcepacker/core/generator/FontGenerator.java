package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.api.IGenerator;
import me.marcusslover.resourcepacker.core.element.font.RPFont;
import me.marcusslover.resourcepacker.core.element.texture.RPTexture;
import me.marcusslover.resourcepacker.core.registry.RPFontRegistry;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;
import me.marcusslover.resourcepacker.util.UnicodeUtil;

import java.io.File;
import java.util.List;

public class FontGenerator implements IGenerator<RPFont, RPFontRegistry> {
    private final JsonObject log;

    FontGenerator() {
        log = new JsonObject();
    }

    @Override
    public JsonObject log() {
        return log;
    }

    @Override
    public void generate(File mc, File packer, RPFontRegistry registry) {
        List<RPFont> list = registry.list();

        File fontFolder = FileUtil.get(mc, "font");
        File fonts = FileUtil.get(packer, "textures/font");

        File file = FileUtil.safeFile(fontFolder, "default.json");
        JsonObject fileJson = new JsonObject();
        JsonArray providers = new JsonArray();

        int unicode = 0;
        for (RPFont font : list) {
            RPTexture texture = font.texture();
            String name = font.name();

            texture.copyFile(fonts);

            JsonObject provider = new JsonObject();
            provider.addProperty("type", "bitmap");
            provider.addProperty("file", "packer:font/" + name + ".png");
            provider.addProperty("ascent", 500); // TODO: Make this configurable
            provider.addProperty("height", 2048); // TODO: Make this configurable
            JsonArray shift = new JsonArray();
            shift.add(3.0); // TODO: Make this configurable
            shift.add(50.0); // TODO: Make this configurable
            provider.add("shift", shift);
            JsonArray chars = new JsonArray();
            String code = UnicodeUtil.CHARS[unicode];
            chars.add(code);
            provider.add("chars", chars);

            providers.add(provider);
            log.addProperty(name, code);

            unicode++;
        }

        fileJson.add("providers", providers);
        JsonUtil.writeFile(file, fileJson);
    }

}
