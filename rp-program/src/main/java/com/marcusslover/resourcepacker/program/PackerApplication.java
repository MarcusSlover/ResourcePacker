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

package com.marcusslover.resourcepacker.program;

import com.marcusslover.resourcepacker.program.application.MainProgram;
import com.marcusslover.resourcepacker.program.application.settings.ProgramSettings;
import com.marcusslover.resourcepacker.program.application.settings.ProgramSettingsBuilder;
import org.apache.commons.cli.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PackerApplication {
    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor();
    private static final CommandLineParser COMMAND_LINE_PARSER = new DefaultParser();
    private static final Options OPTIONS = new Options()
            .addOption("r", true, "Path to the resources directory.")
            .addOption("b", true, "Path to where the resource pack is built.");

    public static void main(String[] args) throws ParseException {
        CommandLine commandLine = COMMAND_LINE_PARSER.parse(OPTIONS, args);
        Optional<String> resourcePath = Optional.ofNullable(commandLine.getOptionValue("r"));
        Optional<String> buildPath = Optional.ofNullable(commandLine.getOptionValue("b"));

        File currentDirectory = Paths.get("").toFile();
        ProgramSettings settings = ProgramSettingsBuilder
                .create()
                .setResourcePath(resourcePath.orElse(currentDirectory + "/resource"))
                .setBuildPath(buildPath.orElse(currentDirectory + "/build"))
                .build();

        MainProgram mainProgram = new MainProgram(settings);
        SERVICE.execute(mainProgram);
    }
}
