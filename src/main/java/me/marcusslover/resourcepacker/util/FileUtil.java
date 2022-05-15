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

package me.marcusslover.resourcepacker.util;

import me.marcusslover.resourcepacker.api.IRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileUtil {
    private FileUtil() {

    }

    public static File get(File parent, String path) {
        if (path.contains("/")) {
            String name = path.split("/")[0];
            File file = safeDir(parent, name);
            return get(file, path.replaceFirst(name + "/", ""));
        }
        return safeDir(parent, path);
    }

    public static File safeFile(File parent, String name) {
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

    public static void deleteFile(File file) {
        if (!file.exists()) return;
        @SuppressWarnings("unused")
        boolean delete = file.delete();
    }

    public static File safeDir(File parent, String name) {
        File child;
        if (parent == null) child = new File(name);
        else child = new File(parent, name);
        if (child.exists()) return child;
        boolean mkdirs = child.mkdirs();
        if (mkdirs) return child;
        return null;
    }

    public static void deleteDir(File file) {
        File[] allContents = file.listFiles();
        if (allContents != null) for (File f : allContents) deleteDir(f);
        @SuppressWarnings("unused")
        boolean delete = file.delete();
    }

    public static <T> List<T> sortByFileCreated(IRegistry<T> reg, Function<T, File> function) {
        return reg.list()
                .stream()
                .sorted(Comparator.comparingLong(file -> getCreationTime(function.apply(file))))
                .collect(Collectors.toList());
    }


    private static long getCreationTime(File file) {
        try {
            Object l = Files.getAttribute(file.toPath(), "creationTime");
            return ((FileTime) l).toMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
