package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.object.block.RPState;
import me.marcusslover.resourcepacker.core.object.texture.Texture;

public interface IBlock extends IData<RPState> {
    String getName();

    Texture getTexture();
}
