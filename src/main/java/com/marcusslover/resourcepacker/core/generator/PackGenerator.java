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

import com.google.gson.JsonObject;
import com.marcusslover.resourcepacker.core.element.sound.RPSound;
import com.marcusslover.resourcepacker.core.packer.ProgramCore;
import com.marcusslover.resourcepacker.core.packer.RPPacker;
import com.marcusslover.resourcepacker.core.registry.RPBlockRegistry;
import com.marcusslover.resourcepacker.core.registry.RPItemRegistry;
import com.marcusslover.resourcepacker.core.registry.RPSoundRegistry;
import com.marcusslover.resourcepacker.core.resource.RPResource;
import com.marcusslover.resourcepacker.core.resource.ResourceHelper;
import com.marcusslover.resourcepacker.util.FileUtil;
import com.marcusslover.resourcepacker.util.JsonUtil;
import net.lingala.zip4j.ZipFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackGenerator {

    public static File createMeta(File d, @Nullable List<String> l) {
        File meta = FileUtil.safeFile(d, "pack.mcmeta");

        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", 8); // 1.18+

        StringBuilder builder = new StringBuilder();
        if (l == null) l = new ArrayList<>();
        for (String s : l) builder.append(s).append("\n");
        pack.addProperty("description", builder.toString().trim());

        JsonObject json = new JsonObject();
        json.add("pack", pack);
        JsonUtil.writeFile(meta, json);

        return meta;
    }

    public void generate(@NotNull RPPacker rpPacker, boolean program) {
        /*Some main specifications of the resource pack*/
        String name = rpPacker.name();
        String logo = rpPacker.logo();
        //String prefix = packer.prefix();
        List<String> description = rpPacker.description();
        ResourceHelper r = rpPacker.resources();
        File output = rpPacker.output();

        /*Creating the directory first*/
        File d = FileUtil.safeDir(output, name);
        if (d == null) return;
        File creationLog = new File(d, "packer_log.json");

        if (creationLog.exists() && program) { // Ask if should overwrite.
            Object[] options = {"Delete & Recreate", "Cancel"};
            int action = JOptionPane.showOptionDialog(
                    ProgramCore.window,
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
                d = FileUtil.safeDir(output, name); // Create it again.
            } else {
                System.exit(0);
                return;
            }
        } else {
            throw new RuntimeException("Could not generate the resource pack because of the existing output files!");
        }
        if (d == null) return;

        /*Pack structure*/
        File meta = createMeta(d, description);
        File pack = createLogo(logo, d, r);
        File assets = FileUtil.safeDir(d, "assets");
        File minecraft = FileUtil.safeDir(assets, "minecraft");
        File packer = FileUtil.safeDir(assets, "packer");

        JsonObject logJson = new JsonObject(); // For logs.
        logJson.addProperty("generatedAt", System.currentTimeMillis());

        /*Blocks*/
        BlockGenerator blocks = new BlockGenerator();
        RPBlockRegistry blockRegistry = rpPacker.blocks();
        blockRegistry.set(FileUtil.sortByFileCreated(blockRegistry, (v) -> v.texture().file()));
        blocks.generate(minecraft, packer, blockRegistry);
        logJson.add("blocks", blocks.log());

        /*Items*/
        ItemGenerator items = new ItemGenerator();
        RPItemRegistry itemRegistry = rpPacker.items();
        itemRegistry.set(FileUtil.sortByFileCreated(itemRegistry, (v) -> v.texture().file()));
        items.generate(minecraft, packer, itemRegistry);
        logJson.add("items", items.log());

        /*Sounds*/
        SoundGenerator sounds = new SoundGenerator();
        RPSoundRegistry soundRegistry = rpPacker.sounds();
        soundRegistry.set(FileUtil.sortByFileCreated(soundRegistry, RPSound::file));
        sounds.generate(minecraft, packer, soundRegistry);
        logJson.add("sounds", sounds.log());

        /*Language*/
        LanguageGenerator lang = new LanguageGenerator();
        lang.generate(packer, itemRegistry, blockRegistry);
        JsonUtil.writeFile(creationLog, logJson);

        // Make sure the zip file does not exist.
        File file = new File(output, name + ".zip");
        int x = 0;
        while (file.exists()) {
            x++;
            file = new File(output, name + x + ".zip");
        }

        /*Zipping*/
        try (ZipFile zipFile = new ZipFile(file)) {
            zipFile.addFolder(assets);
            List<File> filesToAdd = new java.util.ArrayList<>();
            filesToAdd.add(meta);
            filesToAdd.add(pack);
            zipFile.addFiles(filesToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createLogo(@Nullable String logo, @NotNull File d, @NotNull ResourceHelper r) {
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
                if (inputStream != null) {
                    Objects.requireNonNull(output, "out");
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = inputStream.read(buffer, 0, 8192)) >= 0) {
                        output.write(buffer, 0, read);
                    }
                }
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
