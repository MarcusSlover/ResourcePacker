package me.marcusslover.resourcepacker.api;

import java.util.List;

public interface IRegistry<T> {
    void register(T obj);
    void set(List<T> list);
    List<T> list();
}
