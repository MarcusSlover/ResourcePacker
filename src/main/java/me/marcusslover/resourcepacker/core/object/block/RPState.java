package me.marcusslover.resourcepacker.core.object.block;

import me.marcusslover.resourcepacker.api.IManager;
import me.marcusslover.resourcepacker.core.object.item.RPMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds block states data.
 */
public class RPState extends RPMeta implements IManager<RPState.Element> {
    private final List<Element> elements = new ArrayList<>();

    @Override
    public void add(Element obj) {
        elements.add(obj);
    }

    @Override
    public List<Element> list() {
        return elements;
    }

    public static record Element(String key, String value) {

    }
}
