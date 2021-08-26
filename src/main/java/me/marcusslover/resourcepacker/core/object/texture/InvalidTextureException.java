package me.marcusslover.resourcepacker.core.object.texture;

import java.io.File;

/**
 * Thrown when a texture isn't associated with the .png format.
 */
public class InvalidTextureException extends RuntimeException {
    public InvalidTextureException(File file) {
        super("Cannot recognize file: " + file.getName());
    }
}
