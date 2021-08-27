package me.marcusslover.resourcepacker.core.object.texture;

import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.api.ITexture;
import me.marcusslover.resourcepacker.core.internal.RPCache;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static me.marcusslover.resourcepacker.core.internal.Core.LOGGER;

public class Texture implements ITexture {
    private static final Factory FACTORY = new Factory();
    private final File image;
    private BufferedImage buffer;

    private Texture(File image) {
        this.image = image;
        try {
            this.buffer = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Public way of creating textures*/
    public static Texture of(File file) {
        return FACTORY.setFile(file).create();
    }

    @Override
    public File image() {
        return image;
    }

    public File copyFile(File dir) {
        try {
            File file = new File(dir, name() + ".png");
            Path path = file.toPath();

            Path copy = Files.copy(image.toPath(), path);
            return copy.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String name() {
        return image.getName().replaceAll("\\.png", "");
    }

    public int height() {
        return buffer.getHeight();
    }

    public int width() {
        return buffer.getWidth();
    }

    /*Internal factory for creating textures*/
    private static class Factory implements IFactory<Texture> {
        private File file;

        private Factory() {
        }

        private Factory setFile(File file) {
            this.file = file;
            return this;
        }

        @Override
        public Texture create() {
            if (file == null) return null;
            String name = file.getName();

            /*Validate the format*/
            if (!name.endsWith(".png")) throw new InvalidTextureException(file);

            return RPCache.get(name, () -> new Texture(file), Texture.class);
        }
    }
}
