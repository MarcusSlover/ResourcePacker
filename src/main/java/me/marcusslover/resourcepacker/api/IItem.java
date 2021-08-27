package me.marcusslover.resourcepacker.api;

import me.marcusslover.resourcepacker.core.object.item.RPMeta;

public interface IItem extends IData<RPMeta>, INameable, ITexturable {
    boolean itemFrame();
}
