package me.marcusslover.resourcepacker.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.marcusslover.resourcepacker.core.internal.Core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtil() {
    }

    public static JsonElement readFile(File file) {
        return readFile(file, null);
    }

    public static JsonElement readFile(File file, JsonElement defaultValue) {
        if (file == null) return defaultValue;
        Logger logger = Core.LOGGER;

        // If file does not exist.
        if (!file.exists()) {
            // Attempt to create the file.
            try {
                boolean create = file.createNewFile();
                if (create) logger.info("Created the " + file.getName() + " file.");
            } catch (IOException e) {
                logger.info("Could not create the " + file.getName() + " file.");
                e.printStackTrace();
            }
        }

        // Read the file.
        JsonElement element;
        try (FileReader reader = new FileReader(file)) {
            element = JsonParser.parseReader(reader);
        } catch (IOException e) {
            logger.info("Could not read the " + file.getName() + " file.");
            e.printStackTrace();
            return defaultValue;
        }

        return element;
    }

    public static void writeFile(File file, JsonElement element) {
        if (file == null) return;
        Logger logger = Core.LOGGER;

        if (element == null) {
            boolean delete = file.delete();
            if (delete) logger.info("Created the " + file.getName() + " file.");
            return;
        }

        // Write the file.
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(GSON.toJson(element));
            fileWriter.flush();

        } catch (IOException e) {
            logger.info("Could not write the " + file.getName() + " file.");
            e.printStackTrace();
        }
    }
}
