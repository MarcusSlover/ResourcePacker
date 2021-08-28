package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.internal.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.internal.RPItemRegistry;
import me.marcusslover.resourcepacker.core.internal.RPMode;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.io.File;
import java.util.List;

public interface IPacker {
    RPBlockRegistry blocks();

    RPItemRegistry items();

    void setLogo(String path);

    String logo();

    void setName(String name);

    String name();

    void setPrefix(String prefix);

    String prefix();

    void setDescription(List<String> description);

    List<String> description();

    ResourceHelper resources();

    File output();

    void setMode(RPMode RPMode);

    RPMode mode();
}
