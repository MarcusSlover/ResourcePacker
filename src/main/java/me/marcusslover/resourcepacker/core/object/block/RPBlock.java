package me.marcusslover.resourcepacker.core.object.block;

import me.marcusslover.resourcepacker.api.IBlock;
import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.core.internal.RPCache;
import me.marcusslover.resourcepacker.core.object.texture.Texture;
import me.marcusslover.resourcepacker.core.resource.RPResource;

public class RPBlock implements IBlock {
    private static final Factory FACTORY = new Factory();
    private final String name;
    private final Texture texture;
    private final RPState state;

    private RPBlock(String name, Texture texture) {
        this.name = name;
        this.texture = texture;
        this.state = new RPState();
    }

    /*Public way of creating blocks*/
    public static RPBlock of(String name, RPResource resource) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            Texture texture = Texture.of(resource.getFile());
            return of(name, texture);
        }
        return null;
    }

    /*Public way of creating blocks*/
    public static RPBlock of(String name, Texture texture) {
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
    public RPState data() {
        return state;
    }

    /*Internal factory for creating blocks*/
    private static class Factory implements IFactory<RPBlock> {

        private Texture texture;
        private String name;

        private Factory() {
        }

        private Factory setName(String name) {
            this.name = name;
            return this;
        }

        private Factory setTexture(Texture texture) {
            this.texture = texture;
            return this;
        }

        @Override
        public RPBlock create() {
            return RPCache.get(name, () -> new RPBlock(name, texture), RPBlock.class);
        }
    }
}
