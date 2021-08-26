package me.marcusslover.resourcepacker.core.internal;

import me.marcusslover.resourcepacker.ResourcePacker;
import me.marcusslover.resourcepacker.core.generator.PackGenerator;
import me.marcusslover.resourcepacker.core.window.RPWindow;
import org.apache.commons.cli.*;

import javax.swing.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public static boolean terminated = false;
    private File resources;
    private File output;

    private Core() {
    }

    public static void main(String[] args) {
        terminated = false;
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
        if (resources.isEmpty() || output.isEmpty()) return;

        core.setResources(new File(resources));
        core.setOutput(new File(output));
        core.start();
    }

    private static String formatDate(long date) {
        long seconds = date / 1_000L;
        return DF.format(seconds);
    }

    private void start() {
        /*Create the loading window*/
        RPWindow window = new RPWindow(400, 200);
        window.init();

        /*Start the packer*/
        SERVICE.execute(() -> {
            LOGGER.info("Starting the packer...");

            long date = System.currentTimeMillis();
            Packer packer = new Packer(resources, output);

            LOGGER.info("Loading the data...");
            ResourcePacker resourcePacker = new ResourcePacker();
            resourcePacker.pack(packer);

            try { //Wait
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }

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
                    null,
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
