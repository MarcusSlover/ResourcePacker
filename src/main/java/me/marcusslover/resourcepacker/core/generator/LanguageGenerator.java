package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.core.internal.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.internal.RPItemRegistry;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;
import me.marcusslover.resourcepacker.core.object.item.RPItem;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;
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
