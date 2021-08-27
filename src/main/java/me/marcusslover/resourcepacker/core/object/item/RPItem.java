package me.marcusslover.resourcepacker.core.object.item;

import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.api.IItem;
import me.marcusslover.resourcepacker.core.internal.RPCache;
import me.marcusslover.resourcepacker.core.object.texture.Texture;
import me.marcusslover.resourcepacker.core.resource.RPResource;

public class RPItem implements IItem {
    private static final RPItem.Factory FACTORY = new RPItem.Factory();
    private final String name;
    private final Texture texture;
    private final boolean itemFrame;
    private final RPMeta meta;

    private RPItem(String name, Texture texture) {
        this(name, texture, false);
    }

    private RPItem(String name, Texture texture, boolean itemFrame) {
        this.name = name;
        this.texture = texture;
        this.itemFrame = itemFrame;
        this.meta = new RPMeta();
    }


    @Override
    public String toString() {
        return "RPItem{" +
                "name='" + name + '\'' +
                ", texture=" + texture +
                ", meta=" + meta +
                '}';
    }


    /*Public way of creating blocks*/
    public static RPItem of(String name, RPResource resource) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            Texture texture = Texture.of(resource.getFile());
            return of(name, texture, false);
        }
        return null;
    }

    public static RPItem of(String name, RPResource resource, boolean itemFrame) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            Texture texture = Texture.of(resource.getFile());
            return of(name, texture, itemFrame);
        }
        return null;
    }

    public static RPItem of(String name, Texture texture, boolean itemFrame) {
        return FACTORY
                .setName(name)
                .setTexture(texture)
                .setItemFrame(itemFrame)
                .create();
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public Texture texture() {
        return texture;
    }

    @Override
    public RPMeta data() {
        return meta;
    }

    @Override
    public boolean itemFrame() {
        return itemFrame;
    }

    /*Internal factory for creating blocks*/
    private static class Factory implements IFactory<RPItem> {

        private Texture texture;
        private String name;
        private boolean itemFrame;

        private Factory() {
        }

        private RPItem.Factory setName(String name) {
            this.name = name;
            return this;
        }

        private RPItem.Factory setTexture(Texture texture) {
            this.texture = texture;
            return this;
        }

        private RPItem.Factory setItemFrame(boolean itemFrame) {
            this.itemFrame = itemFrame;
            return this;
        }


        @Override
        public RPItem create() {
            return RPCache.get(name, () -> new RPItem(name, texture, itemFrame), RPItem.class);
        }
    }
}
