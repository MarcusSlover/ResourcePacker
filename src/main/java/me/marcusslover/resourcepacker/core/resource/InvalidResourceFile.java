package me.marcusslover.resourcepacker.core.resource;

public class InvalidResourceFile extends RuntimeException {
    public InvalidResourceFile(String file) {
        super("The resource file: '" + file + "' doesn't exist!");
    }
}
