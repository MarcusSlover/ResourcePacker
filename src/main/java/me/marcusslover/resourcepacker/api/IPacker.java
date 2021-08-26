package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.internal.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.internal.RPItemRegistry;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.util.List;

public interface IPacker {
    RPBlockRegistry blocks();

    RPItemRegistry items();

    void setName(String name);

    void setPrefix(String prefix);

    void setDescription(List<String> description);

    ResourceHelper getResources();

}
