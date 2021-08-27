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
    private final RPMeta meta;

    private RPItem(String name, Texture texture) {
        this.name = name;
        this.texture = texture;
        this.meta = new RPMeta();
    }

    /*Public way of creating blocks*/
    public static RPItem of(String name, RPResource resource) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            Texture texture = Texture.of(resource.getFile());
            return of(name, texture);
        }
        return null;
    }

    /*Public way of creating blocks*/
    public static RPItem of(String name, Texture texture) {
        return FACTORY.setName(name).setTexture(texture).create();
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

    /*Internal factory for creating blocks*/
    private static class Factory implements IFactory<RPItem> {

        private Texture texture;
        private String name;

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

        @Override
        public RPItem create() {
            return RPCache.get(name, () -> new RPItem(name, texture), RPItem.class);
        }
    }
}
