package com.example.nickpellegrino.smartcoffee;
import java.util.Random;

/**
 * Created by nickpellegrino on 3/22/17.
 */

public class CoffeeOrder {

    public String coffeeOrder; // morning blend, hazelnut
    public String coffeeSize; // medium, large
    public String classroom;  // 101, 151, etc.
    public int sugars;   // 0, 1, 2, etc
    public int creams;   // 0, 1, 2, etc
    public int orderID;  // rand(0, 1023)
    public String userID; // bunchofjunk string
    public String sugarKind; // splenda, equal, regular
    public String creamKind; // A, B, C
    public String status;

    public CoffeeOrder(){

    }

    public CoffeeOrder(String coffeeOrder, String coffeeSize, String classroom, int sugars, int creams, String userID, String sugarKind, String creamKind){
        this.coffeeOrder = coffeeOrder;
        this.coffeeSize = coffeeSize;
        this.classroom = classroom;
        this.sugars = sugars;
        this.creams = creams;
        Random rand = new Random();
        this.orderID = rand.nextInt(1023) + 0;
        this.userID = userID;
        this.sugarKind = sugarKind;
        this.creamKind = creamKind;
        this.status = "Pending";
    }
}
