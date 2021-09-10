package controller;

import java.util.List;

public interface WriteFileAndReadFile<T> {
    void writeFile(String path);

     List<T> readFile(String path);
}
