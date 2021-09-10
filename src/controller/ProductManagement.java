package controller;

import model.Product;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManagement implements IGeneralManagement<Product>, WriteFileAndReadFile<Product> {
    static List<Product> productList = new ArrayList<>();

    static {
        productList.add(new Product("1", "Iphone 12 Pro max", 30000000));
        productList.add(new Product("2", "Iphone 12 Pro", 28000000));
        productList.add(new Product("3", "Iphone 11 Pro max", 25000000));
        productList.add(new Product("4", "Iphone 11 Pro", 23000000));
        productList.add(new Product("5", "Iphone X", 15000000));
    }

    public List<Product> getProductList() {
        return productList;
    }

    @Override
    public void showAll() {
        for (Product products : productList) {
            System.out.println(products);
        }
    }

    @Override
    public void add(Product product) {
        productList.add(product);
    }

    @Override
    public void updateById(int index, Product product) {
        productList.set(index, product);
    }

    @Override
    public void removeById(int index) {
        productList.remove(index);
    }

    public int findProductById(String id) {
        int index = -1;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equals(id)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void writeFile(String path) {
        try {
            OutputStream os = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(productList);
            os.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> readFile(String path) {
        try {
            InputStream is = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(is);
            productList = (List<Product>) ois.readObject();
            is.close();
            ois.close();
        } catch (EOFException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
