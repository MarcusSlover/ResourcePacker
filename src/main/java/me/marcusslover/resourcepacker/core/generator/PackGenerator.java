package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.core.internal.Packer;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;
import me.marcusslover.resourcepacker.core.object.item.RPItem;
import me.marcusslover.resourcepacker.core.object.texture.Texture;
import me.marcusslover.resourcepacker.core.resource.RPResource;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;
import me.marcusslover.resourcepacker.util.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class PackGenerator {
    private static final Integer LIMIT = 24;
    private static final String[] INSTRUMENTS =
            {
                    "harp", "pling", "banjo", "bit",
                    "didgeridoo", "cow_bell", "iron_xylophone", "xylophone",
                    "chime", "guitar", "bell", "flute",
                    "bass", "hat", "snare", "bassdrum"
            };

    public void generate(Packer packer) {
        /*Some main specifications of the resource pack*/
        String name = packer.name();
        String logo = packer.logo();
        String prefix = packer.prefix();
        List<String> description = packer.description();
        ResourceHelper r = packer.getResources();
        File output = packer.getOutput();

        /*Objects to be packed*/
        List<RPBlock> blocks = packer.blocks().list();
        List<RPItem> items = packer.items().list();

        /*Creating the directory first*/
        File d = safeDir(output, name);

        /*Pack structure*/
        File packMeta = createMeta(d, description);
        File packPng = createLogo(logo, d, r);

        /*Assets*/
        File assets = safeDir(d, "assets");

        /*Minecraft*/
        File mcDir = safeDir(assets, "minecraft");

        /*Packing*/
        File statesDir = safeDir(mcDir, "blockstates");
        File modelsDir = safeDir(mcDir, "models");
        File modelBlockDir = safeDir(modelsDir, "block");

        File texturesDir = safeDir(mcDir, "textures");
        File textureBlockDir = safeDir(texturesDir, "block");

        File specialNoteBlock = safeFile(statesDir, "note_block.json");
        JsonObject specialJson = new JsonObject();
        JsonObject specialVariants = new JsonObject();

        int instrument = 0;
        int note = 0;

        /*Blocks*/
        for (RPBlock block : blocks) {
            if (note > LIMIT) {
                note = 0;
                instrument++;
            }

            Texture texture = block.getTexture();
            File image = texture.getImage();
            String s = image.getName().replaceAll("\\.png", "");

            /*Textures*/
            try {
                Files.copy(image.toPath(), new File(textureBlockDir, s + ".png").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*Json Block Models*/
            JsonObject jsonModel = new JsonObject();
            jsonModel.addProperty("parent", "minecraft:block/cube_all");
            JsonObject modelTextures = new JsonObject();
            modelTextures.addProperty("all", "minecraft:block/" + s);
            jsonModel.add("textures", modelTextures);
            File model = safeFile(modelBlockDir, s + ".json");
            if (model == null) continue;
            JsonUtil.writeFile(model, jsonModel);

            /*Variant creation*/
            JsonObject variant = new JsonObject();
            variant.addProperty("model", "minecraft:block/" + s);
            String path = "instrument="+INSTRUMENTS[instrument]+",note="+note;
            specialVariants.add(path, variant);
            note++;
        }

        specialJson.add("variants", specialVariants);
        JsonUtil.writeFile(specialNoteBlock, specialJson);

    }

    private File createMeta(File d, List<String> l) {
        File meta = safeFile(d, "pack.mcmeta");

        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", 7); // 1.17+

        StringBuilder builder = new StringBuilder();
        for (String s : l) builder.append(s).append("\n");
        pack.addProperty("description", builder.toString().trim());

        JsonObject json = new JsonObject();
        json.add("pack", pack);
        JsonUtil.writeFile(meta, json);

        return meta;
    }

    private File createLogo(String logo, File d, ResourceHelper r) {
        File packPng = new File(d, "pack.png");
        File logoSource;

        if (logo == null) {
            URL url = getClass().getResource("/logo/rp.png");
            try {
                logoSource = new File(url.toURI());
            } catch (URISyntaxException e) {
                return packPng;
            }
        } else {
            RPResource rpResource = r.get(logo);
            logoSource = rpResource.getFile();
        }

        if (logoSource != null) {
            try {
                Files.copy(logoSource.toPath(), packPng.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return packPng;
    }


    private File safeFile(File parent, String name) {
        File child = new File(parent, name);
        if (child.exists()) return child;
        try {
            boolean newFile = child.createNewFile();
            if (newFile) return child;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File safeDir(File parent, String name) {
        File child = new File(parent, name);
        if (child.exists()) return child;
        boolean mkdirs = child.mkdirs();
        if (mkdirs) return child;
        return null;
    }
}
