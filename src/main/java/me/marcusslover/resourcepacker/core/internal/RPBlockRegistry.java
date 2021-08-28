package me.marcusslover.resourcepacker.core.internal;

import me.marcusslover.resourcepacker.api.IRegistry;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;

import java.util.ArrayList;
import java.util.List;

public class RPBlockRegistry implements IRegistry<RPBlock> {
    private List<RPBlock> blocks = new ArrayList<>();

    RPBlockRegistry() {
    }

    @Override
    public void register(RPBlock obj) {
        blocks.add(obj);
    }

    @Override
    public void set(List<RPBlock> list) {
        blocks = list;
    }

    @Override
    public List<RPBlock> list() {
        return blocks;
    }
}
