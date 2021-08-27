package me.marcusslover.resourcepacker.util;

import me.marcusslover.resourcepacker.core.internal.Core;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File get(File parent, String path) {
        if (path.contains("/")) {
            String name = path.split("/")[0];
            File file = safeDir(parent, name);
            return get(file, path.replaceFirst(name+"/", ""));
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
}
