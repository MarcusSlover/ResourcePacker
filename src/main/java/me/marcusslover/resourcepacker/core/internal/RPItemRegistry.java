package me.marcusslover.resourcepacker.core.internal;

import me.marcusslover.resourcepacker.api.IRegistry;
import me.marcusslover.resourcepacker.core.object.item.RPItem;

import java.util.ArrayList;
import java.util.List;

public class RPItemRegistry implements IRegistry<RPItem> {
    private final List<RPItem> items = new ArrayList<>();

    RPItemRegistry() {
    }

    @Override
    public void register(RPItem obj) {
        items.add(obj);
    }

    @Override
    public List<RPItem> list() {
        return items;
    }
}
