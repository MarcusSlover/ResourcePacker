package me.marcusslover.resourcepacker.core.object.block;

import me.marcusslover.resourcepacker.api.IBlock;
import me.marcusslover.resourcepacker.api.IFactory;
import me.marcusslover.resourcepacker.core.internal.RPCache;
import me.marcusslover.resourcepacker.core.object.texture.RPTexture;
import me.marcusslover.resourcepacker.core.resource.RPResource;

public class RPBlock implements IBlock {
    private static final Factory FACTORY = new Factory();
    private final String name;
    private final RPTexture texture;
    private final RPState state;

    private RPBlock(String name, RPTexture texture) {
        this.name = name;
        this.texture = texture;
        this.state = new RPState();
    }

    /*Public way of creating blocks*/
    public static RPBlock of(String name, RPResource resource) {
        if (resource.getType() == RPResource.Type.IMAGE) {
            RPTexture texture = RPTexture.of(resource.getFile());
            return of(name, texture);
        }
        return null;
    }

    /*Public way of creating blocks*/
    public static RPBlock of(String name, RPTexture texture) {
        return FACTORY.setName(name).setTexture(texture).create();
    }

    @Override
    public String name() {
        return name == null ? texture.name() : name;
    }

    @Override
    public RPTexture texture() {
        return texture;
    }

    @Override
    public RPState data() {
        return state;
    }

    /*Internal factory for creating blocks*/
    private static class Factory implements IFactory<RPBlock> {

        private RPTexture texture;
        private String name;

        private Factory() {
        }

        private Factory setName(String name) {
            this.name = name;
            return this;
        }

        private Factory setTexture(RPTexture texture) {
            this.texture = texture;
            return this;
        }

        @Override
        public RPBlock create() {
            return RPCache.get(name, () -> new RPBlock(name, texture), RPBlock.class);
        }
    }
}
