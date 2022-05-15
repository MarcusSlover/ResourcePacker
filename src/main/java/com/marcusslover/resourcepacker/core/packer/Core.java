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

package com.marcusslover.resourcepacker.core.packer;

import com.marcusslover.resourcepacker.ResourcePacker;
import com.marcusslover.resourcepacker.core.element.block.RPBlock;
import com.marcusslover.resourcepacker.core.element.item.RPItem;
import com.marcusslover.resourcepacker.core.element.sound.RPSound;
import com.marcusslover.resourcepacker.core.generator.PackGenerator;
import com.marcusslover.resourcepacker.core.resource.ResourceHelper;
import com.marcusslover.resourcepacker.util.FileUtil;
import org.apache.commons.cli.*;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Core {
    public static final Logger LOGGER = Logger.getLogger("Packer");
    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor();
    private static final DecimalFormat DF = new DecimalFormat("#.##");
    private static final CommandLineParser PARSER = new DefaultParser();
    private static final Options OPTIONS = new Options()
            .addOption(
                    "r",
                    "resources",
                    true,
                    "The path to resource directory")
            .addOption(
                    "o",
                    "output",
                    true,
                    "The path where resource pack is created");

    public static RPWindow window;
    public static boolean terminated = false;

    private File resources;
    private File output;

    private Core() {
    }

    public static void main(String[] args) {
        terminated = false;

        /*Parse commands*/
        CommandLine cmd;
        try {
            cmd = PARSER.parse(OPTIONS, args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        /*Set up the core*/
        Core core = new Core();
        String resources = cmd.hasOption("r") ? cmd.getOptionValue("r") : "";
        String output = cmd.hasOption("o") ? cmd.getOptionValue("o") : "";

        Path currentRelativePath = Paths.get(""); //Todo: not sure this may cause incompatibility with MacOs
        File workingDir = new File(currentRelativePath.toUri());

        File r;
        File o;

        /*Path*/
        if (resources.isEmpty()) r = FileUtil.safeDir(workingDir, "Resources");
        else r = new File(resources);
        if (r == null || !r.exists()) {
            JOptionPane.showMessageDialog(null,
                    "The specified path for the 'Resources' directory is invalid!" +
                            " The directory doesn't exist!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
            return;
        }

        if (output.isEmpty()) o = FileUtil.safeDir(workingDir, "Output");
        else o = new File(output);
        if (o == null || !o.exists()) {
            JOptionPane.showMessageDialog(null,
                    "The specified path for the 'Output' directory is invalid!" +
                            " The directory doesn't exist!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
            return;
        }

        core.setResources(r);
        core.setOutput(o);
        core.start();
    }

    private static String formatDate(long date) {
        long seconds = date / 1_000L;
        return DF.format(seconds);
    }

    private void start() {
        /*Create the loading window*/
        window = new RPWindow(400, 200);
        window.init();

        /*Start the packer*/
        SERVICE.execute(() -> {
            LOGGER.info("Starting the packer...");

            long date = System.currentTimeMillis();
            RPPacker packer = new RPPacker(resources, output);

            LOGGER.info("Loading the data...");
            ResourcePacker resourcePacker = new ResourcePacker();
            resourcePacker.pack(packer);

            if (packer.mode() == RPMode.AUTOMATIC) {
                ResourceHelper r = packer.resources();
                File parent = packer.resources().parent();

                if (parent.exists()) {
                    File blocks = FileUtil.safeDir(parent, "blocks");
                    File items = FileUtil.safeDir(parent, "items");
                    File frames = FileUtil.safeDir(parent, "itemframes");
                    File sounds = FileUtil.safeDir(parent, "sounds");


                    if (blocks != null && items != null && frames != null && sounds != null) {
                        String[] block = blocks.list();
                        String[] item = items.list();
                        String[] frame = frames.list();
                        String[] sound = sounds.list();

                        if (block.length + item.length + frame.length + sound.length == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "No files found! Processes skipped.",
                                    "Information",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            System.exit(0);
                            return;
                        }
                        for (String s : block)
                            packer.blocks()
                                    .register(RPBlock.of(null, r.block(s)));
                        for (String s : item)
                            packer.items()
                                    .register(RPItem.of(null, r.item(s)));
                        for (String s : frame)
                            packer.items()
                                    .register(RPItem.of(null, r.frame(s), true));
                        for (String s : sound)
                            packer.sounds()
                                    .register(RPSound.of(r.sound(s)));
                    }
                }
            }

            try { //Wait
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }

            /*Generate*/
            window.getProgress().setValue(50);
            LOGGER.info("Reading & preparing...");
            PackGenerator packGenerator = new PackGenerator();
            packGenerator.generate(packer);

            try { //Wait
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
            window.getProgress().setValue(100);

            long compare = System.currentTimeMillis() - date;
            LOGGER.info(String.format("Done in %ss!", formatDate(compare)));
            JOptionPane.showMessageDialog(
                    Core.window,
                    "The resource pack was successfully created!",
                    "Done",
                    JOptionPane.INFORMATION_MESSAGE);
            window.setVisible(false);
            window.dispose();
            System.exit(0);
        });
    }

    private void setOutput(File output) {
        this.output = output;
    }

    private void setResources(File resources) {
        this.resources = resources;
    }
}
