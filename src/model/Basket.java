package model;

import java.io.Serializable;

public class Basket implements Serializable {
    private Product product;
    private int amount;

    public Basket() {
    }

    public Basket(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "\n"+product +
                "\n Số lượng : " + amount
                ;
    }
}