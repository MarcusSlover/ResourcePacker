package me.marcusslover.resourcepacker.core.object.texture;

import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.api.ITexture;
import me.marcusslover.resourcepacker.core.internal.RPCache;

import java.io.File;

public class Texture implements ITexture {
    private static final Factory FACTORY = new Factory();
    private final File image;

    private Texture(File image) {
        this.image = image;
    }

    /*Public way of creating textures*/
    public static Texture of(File file) {
        return FACTORY.setFile(file).create();
    }

    @Override
    public File image() {
        return image;
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
