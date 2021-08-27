package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.resource.RPResource;

import java.io.File;

public interface IResources {
    RPResource get(String dir, String child);
}
