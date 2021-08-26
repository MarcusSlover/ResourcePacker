package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.resource.RPResource;

public interface IResources {
    RPResource get(String child);
}
