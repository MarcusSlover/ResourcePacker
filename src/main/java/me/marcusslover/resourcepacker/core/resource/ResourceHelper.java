package me.marcusslover.resourcepacker.core.resource;

import me.marcusslover.resourcepacker.api.IResources;

import java.io.File;

public record ResourceHelper(File parent) implements IResources {

    @Override
    public RPResource get(String dir, String child) {
        if (child.endsWith(".png")) {
            File file;
            if (dir != null) {
                File directory = new File(parent, dir);
                if (!directory.exists()) throw new InvalidResourceFile(directory.toString());
                file = new File(directory, child);
            } else {
                file = new File(parent, child);
            }
            if (!file.exists()) throw new InvalidResourceFile(child);
            return new RPResource(file, RPResource.Type.IMAGE);
        }
        return null;
    }

    public RPResource block(String s) {
        return get("blocks", s);
    }

    public RPResource item(String s) {
        return get("items", s);
    }
}
