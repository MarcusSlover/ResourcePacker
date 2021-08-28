package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.internal.RPPacker;

public interface IPackable {
    default RPPacker packer() {
        return RPPacker.get();
    }
}
