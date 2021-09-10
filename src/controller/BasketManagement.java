package controller;

import model.Basket;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class BasketManagement implements WriteFileAndReadFile<Basket> {
    List<Basket> basketList = new ArrayList<>();

    public List<Basket> getBasketList() {
        return basketList;
    }

    public void addProductToBasket(Basket basket) {
        basketList.add(basket);
    }

    public void removeProductToBasket(int index) {
        basketList.remove(index);
    }

    public void showBasket() {
        for (Basket basket : basketList) {
            System.out.println(basket);
        }
    }

    public void removeAllProductToBasket() {
        basketList.removeAll(basketList);
    }

    public int findBasketByIdProduct(String id) {
        int index = -1;
        for (int i = 0; i < basketList.size(); i++) {
            if (basketList.get(i).getProduct().getId().equals(id)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void writeFile(String path) {
        try {
            OutputStream os = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(basketList);
            os.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Basket> readFile(String path) {
        try {
            InputStream is = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(is);
            basketList = (List<Basket>) ois.readObject();
            is.close();
            ois.close();
        } catch (EOFException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return basketList;
    }
}
