package me.marcusslover.resourcepacker.core.internal;

import me.marcusslover.resourcepacker.api.IPacker;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.io.File;
import java.util.List;

/**
 * Used publicly to register all blocks and items.
 */
public class Packer implements IPacker {
    private static Packer instance;
    final RPBlockRegistry blockRegistry;
    final RPItemRegistry itemRegistry;

    final ResourceHelper resources;
    final File output;

    Mode mode;
    String name;
    String logo;
    String prefix;
    List<String> description;

    /*Only for internal creation*/
    Packer(File resources, File output) {
        instance = this;
        this.resources = new ResourceHelper(resources);
        this.output = output;

        mode = Mode.AUTOMATIC;
        name = "Packer";
        logo = null;
        prefix = "packer";
        description = List.of("Created with ResourcePacker by MarcusSlover");

        blockRegistry = new RPBlockRegistry();
        itemRegistry = new RPItemRegistry();
    }

    public static Packer get() {
        return instance;
    }

    @Override
    public RPBlockRegistry blocks() {
        return blockRegistry;
    }

    @Override
    public RPItemRegistry items() {
        return itemRegistry;
    }

    @Override
    public void setLogo(String path) {
        logo = path;
    }

    @Override
    public String logo() {
        return logo;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    public List<String> description() {
        return description;
    }

    @Override
    public ResourceHelper resources() {
        return resources;
    }

    @Override
    public File output() {
        return output;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Mode mode() {
        return mode;
    }
}
