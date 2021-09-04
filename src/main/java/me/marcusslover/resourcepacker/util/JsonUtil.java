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
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

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
