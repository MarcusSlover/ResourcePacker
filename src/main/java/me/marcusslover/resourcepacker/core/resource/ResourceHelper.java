package me.marcusslover.resourcepacker.core.resource;

import me.marcusslover.resourcepacker.api.IResources;

import java.io.File;

public class ResourceHelper implements IResources {
    private final File parent;

    public ResourceHelper(File parent) {
        this.parent = parent;
    }

    @Override
    public RPResource get(String child) {
        if (child.endsWith(".png")) {
            File file = new File(parent, child);
            if (!file.exists()) throw new InvalidResourceFile(child);

            return new RPResource(file, RPResource.Type.IMAGE);
        }
        return null;
    }
}
