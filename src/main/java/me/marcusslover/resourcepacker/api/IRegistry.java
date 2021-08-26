package me.marcusslover.resourcepacker.api;

import java.util.List;

public interface IRegistry<T> {
    void register(T obj);

    List<T> list();
}
