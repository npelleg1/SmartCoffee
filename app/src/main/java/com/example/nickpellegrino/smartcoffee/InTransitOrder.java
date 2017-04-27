package com.example.nickpellegrino.smartcoffee;

/**
 * Created by nickpellegrino on 4/27/17.
 */

public class InTransitOrder {
    String orderID;
    String status;
    String roomNumber;

    public InTransitOrder(){

    }

    public InTransitOrder(String orderID, String status, String roomNumber){
        this.orderID = orderID;
        this.status = status;
        this.roomNumber = roomNumber;
    }
}
