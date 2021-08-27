package me.marcusslover.resourcepacker.core.internal;

import me.marcusslover.resourcepacker.ResourcePacker;
import me.marcusslover.resourcepacker.core.generator.PackGenerator;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;
import me.marcusslover.resourcepacker.core.object.item.RPItem;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;
import me.marcusslover.resourcepacker.core.window.RPWindow;
import me.marcusslover.resourcepacker.util.FileUtil;
import org.apache.commons.cli.*;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.LongFunction;
import java.util.logging.Logger;

public class Core {
    public static final Logger LOGGER = Logger.getLogger("Packer");
    public static final RPCache CACHE = new RPCache();
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

        Path currentRelativePath = Paths.get("");
        File workingDir = new File(currentRelativePath.toUri());

        File r;
        File o;

        /*Path*/
        if (resources.isEmpty()) r = FileUtil.safeDir(workingDir, "Resources");
        else r = new File(resources);
        if (r == null || !r.exists()) {
            JOptionPane.showMessageDialog(null,
                    "The specified path for the 'Resources' directory is invalid! The directory doesn't exist!",
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
                    "The specified path for the 'Output' directory is invalid! The directory doesn't exist!",
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
            Packer packer = new Packer(resources, output);

            LOGGER.info("Loading the data...");
            ResourcePacker resourcePacker = new ResourcePacker();
            resourcePacker.pack(packer);

            if (packer.mode() == Mode.AUTOMATIC) {
                ResourceHelper r = packer.resources();
                File parent = packer.resources().parent();
                if (parent != null && parent.exists()) {
                    File blocks = FileUtil.safeDir(parent, "blocks");
                    File items = FileUtil.safeDir(parent, "items");

                    if (blocks != null && items != null) {
                        String[] b = blocks.list();
                        String[] i = items.list();
                        if (b.length + i.length == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "No textures found! Processes skipped.",
                                    "Information",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            System.exit(0);
                            return;
                        }
                        for (String s : b) packer.blocks().register(RPBlock.of(null, r.block(s)));
                        for (String s : i) packer.items().register(RPItem.of(null, r.item(s)));
                    }
                }
            }

            try { //Wait
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }

            /*Generate*/
            window.getProgress().setValue(33);
            LOGGER.info("Reading & preparing...");
            PackGenerator packGenerator = new PackGenerator();
            packGenerator.generate(packer);

            try { //Wait
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
            window.getProgress().setValue(66);

            LOGGER.info("Packing...");
            //Todo: Packing
            try { //Wait
                Thread.sleep(500);
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
