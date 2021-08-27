package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.core.internal.Core;
import me.marcusslover.resourcepacker.core.internal.Packer;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;
import me.marcusslover.resourcepacker.core.object.item.RPItem;
import me.marcusslover.resourcepacker.core.object.texture.Texture;
import me.marcusslover.resourcepacker.core.resource.RPResource;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static me.marcusslover.resourcepacker.util.FileUtil.safeDir;
import static me.marcusslover.resourcepacker.util.FileUtil.safeFile;

public class PackGenerator {
    private static final Integer LIMIT = 24;
    private static final String[] INSTRUMENTS =
            {
                    "harp", "pling", "banjo", "bit",
                    "didgeridoo", "cow_bell", "iron_xylophone", "xylophone",
                    "chime", "guitar", "bell", "flute",
                    "bass", "hat", "snare", "bassdrum"
            };

    public static File createMeta(File d, List<String> l) {
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

    public void generate(Packer packer) {
        /*Some main specifications of the resource pack*/
        String name = packer.name();
        String logo = packer.logo();
        String prefix = packer.prefix();
        List<String> description = packer.description();
        ResourceHelper r = packer.resources();
        File output = packer.output();

        /*Objects to be packed*/
        List<RPBlock> blocks = packer.blocks().list();
        List<RPItem> items = packer.items().list();

        /*Creating the directory first*/
        File d = safeDir(output, name);
        File creationLog = new File(d, "packerLog.json");

        if (creationLog.exists()) { // Ask if should overwrite.
            Object[] options = {"Delete & Recreate", "Cancel"};
            int action = JOptionPane.showOptionDialog(
                    Core.window,
                    "Seems like this pack had already been built. Would you like to delete the old files?",
                    "Old Files Found",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            System.out.println(action);
            if (action == 0) {
                FileUtil.deleteFile(creationLog);
                FileUtil.deleteDir(d); // Delete all files.
                d = safeDir(output, name); // Create it again.
            } else {
                System.exit(0);
                return;
            }
        }

        /*Pack structure*/
        createMeta(d, description);
        createLogo(logo, d, r);

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

        JsonObject logJson = new JsonObject(); // For logs.
        logJson.addProperty("generatedAt", System.currentTimeMillis());
        JsonObject blocksLog = new JsonObject();

        int instrument = 0;
        int note = 0;

        /*Blocks*/
        for (RPBlock block : blocks) {
            if (note > LIMIT) {
                note = 0;
                instrument++;
            }

            Texture texture = block.texture();
            File image = texture.image();
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
            String path = "instrument=" + INSTRUMENTS[instrument] + ",note=" + note;
            specialVariants.add(path, variant);

            /*Log creation*/
            blocksLog.addProperty(s, path);

            note++;
        }

        specialJson.add("variants", specialVariants);
        JsonUtil.writeFile(specialNoteBlock, specialJson);

        logJson.add("blocks", blocksLog);
        JsonUtil.writeFile(creationLog, logJson);
    }

    private File createLogo(String logo, File d, ResourceHelper r) {
        File packPng = new File(d, "pack.png");
        File logoSource = null;

        String defaultName = "/logo/rp.png";
        if (logo == null) {
            URL url = getClass().getResource(defaultName);
            try {
                logoSource = new File(url.toURI());
            } catch (Exception ignored) {
            }
        } else {
            RPResource rpResource = r.get(logo);
            logoSource = rpResource.getFile();
        }

        if (logoSource == null) {
            InputStream inputStream = getClass().getResourceAsStream(defaultName);
            try (OutputStream output = new FileOutputStream(packPng, false)) {
                if (inputStream != null) inputStream.transferTo(output);
            } catch (Exception ignore) {
            }
            try {
                Files.copy(inputStream, packPng.toPath());
            } catch (IOException ignored) {
            }
        } else {
            try {
                Path copy = Files.copy(logoSource.toPath(), packPng.toPath());
                packPng = copy.toFile();
            } catch (IOException ignored) {
            }
        }
        @SuppressWarnings("unused")
        boolean b = packPng.setLastModified(System.currentTimeMillis());
        return packPng;
    }


}
