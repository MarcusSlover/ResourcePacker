package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.internal.Packer;

public interface IPackable {
    default Packer packer() {
        return Packer.get();
    }
}
