package controller;

public interface IGeneralManagement<T> {
    void showAll();

    void add(T t);

    void updateById(int index, T t);

    void removeById(int index);
}