package me.marcusslover.resourcepacker.api;

import com.google.gson.JsonObject;

import java.io.File;

public interface IGenerator<T, V extends IRegistry<T>> {
    JsonObject log();

    void generate(File mc, File packer, V registry);
}
