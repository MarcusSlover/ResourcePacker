package me.marcusslover.resourcepacker.api;

import java.util.List;

public interface IManager<T> {
    void add(T obj);

    List<T> list();
}
