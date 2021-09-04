/*
 * MIT License
 *
 * Copyright (c) 2021 MarcusSlover
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

import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.core.internal.Core;
import me.marcusslover.resourcepacker.core.internal.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.internal.RPItemRegistry;
import me.marcusslover.resourcepacker.core.internal.RPPacker;
import me.marcusslover.resourcepacker.core.resource.RPResource;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;
import net.lingala.zip4j.ZipFile;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static me.marcusslover.resourcepacker.util.FileUtil.safeDir;
import static me.marcusslover.resourcepacker.util.FileUtil.safeFile;

public class PackGenerator {

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

    public void generate(RPPacker rpPacker) {
        /*Some main specifications of the resource pack*/
        String name = rpPacker.name();
        String logo = rpPacker.logo();
        //String prefix = packer.prefix();
        List<String> description = rpPacker.description();
        ResourceHelper r = rpPacker.resources();
        File output = rpPacker.output();

        /*Creating the directory first*/
        File d = safeDir(output, name);
        File creationLog = new File(d, "packer_log.json");

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
        File meta = createMeta(d, description);
        File pack = createLogo(logo, d, r);
        File assets = safeDir(d, "assets");
        File minecraft = safeDir(assets, "minecraft");
        File packer = safeDir(assets, "packer");

        JsonObject logJson = new JsonObject(); // For logs.
        logJson.addProperty("generatedAt", System.currentTimeMillis());

        /*Blocks*/
        BlockGenerator blocks = new BlockGenerator();
        RPBlockRegistry blockRegistry = rpPacker.blocks();
        blockRegistry.set(FileUtil.sortByFileCreated(blockRegistry));
        blocks.generate(minecraft, packer, blockRegistry);
        logJson.add("blocks", blocks.log());

        /*Items*/
        ItemGenerator items = new ItemGenerator();
        RPItemRegistry itemRegistry = rpPacker.items();
        itemRegistry.set(FileUtil.sortByFileCreated(itemRegistry));
        items.generate(minecraft, packer, itemRegistry);
        logJson.add("items", items.log());

        /*Language*/
        LanguageGenerator lang = new LanguageGenerator();
        lang.generate(packer, itemRegistry, blockRegistry);
        JsonUtil.writeFile(creationLog, logJson);

        /*Zipping*/
        try (ZipFile zipFile = new ZipFile(new File(output, name + ".zip"))) {
            zipFile.addFolder(assets);
            zipFile.addFiles(List.of(meta, pack));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            RPResource rpResource = r.get(null, logo);
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
