package com.example.nickpellegrino.smartcoffee;
import java.util.Random;

/**
 * Created by nickpellegrino on 3/22/17.
 */

public class CoffeeOrder {

    public String coffeeOrder;
    public String coffeeSize;
    public String classroom;
    public int sugars;
    public int creams;
    public int orderID;

    public CoffeeOrder(String coffeeOrder, String coffeeSize, String classroom, int sugars, int creams){
        this.coffeeOrder = coffeeOrder;
        this.coffeeSize = coffeeSize;
        this.classroom = classroom;
        this.sugars = sugars;
        this.creams = creams;
        Random rand = new Random();
        this.orderID = rand.nextInt(1023) + 0;
    }
}
