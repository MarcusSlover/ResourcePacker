package me.marcusslover.resourcepacker.core.resource;

import java.io.File;

public class RPResource {
    private final File file;
    private final Type type;

    public RPResource(File file, Type type) {
        this.file = file;
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        IMAGE
    }
}
