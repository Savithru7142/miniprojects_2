package com.klu.CollageCanteenOrderQueue.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
public class CollegeCanteenController {

    private final List<CanteenOrder> orderList = new ArrayList<>();
    private int idCounter = 500;

    // Constructor – sample data
    public CollegeCanteenController() {
        orderList.add(new CanteenOrder(501, "Sharvan",
                "Veg Fried Rice x1, Water Bottle x1", 110.0, "NEW"));
        orderList.add(new CanteenOrder(502, "Abhi",
                "Idli x2, Coffee x1", 60.0, "READY"));
        idCounter = 502;
    }

    // ================= GET ALL =================
    @GetMapping("/orders")
    public List<CanteenOrder> getAllOrders(
            @RequestParam(required = false) String status) {

        if (status == null) {
            return orderList;
        }

        List<CanteenOrder> result = new ArrayList<>();
        for (CanteenOrder o : orderList) {
            if (o.status.equalsIgnoreCase(status)) {
                result.add(o);
            }
        }
        return result;
    }

    // ================= GET BY ID =================
    @GetMapping("/orders/{id}")
    public CanteenOrder getOrderById(@PathVariable int id) {
        return findById(id);
    }

    // ================= CREATE =================
    @PostMapping("/orders")
    public CanteenOrder placeOrder(@RequestBody CanteenOrder newOrder) {
        newOrder.id = ++idCounter;
        newOrder.status = "NEW";
        orderList.add(newOrder);
        return newOrder;
    }

    // ================= UPDATE =================
    @PutMapping("/orders/{id}")
    public CanteenOrder updateOrder(@PathVariable int id,
                                    @RequestBody CanteenOrder updatedOrder) {

        CanteenOrder existing = findById(id);
        if (existing == null) {
            return null;
        }

        existing.items = updatedOrder.items;
        existing.totalAmount = updatedOrder.totalAmount;
        existing.status = updatedOrder.status;

        return existing;
    }

    // ================= DELETE / CANCEL =================
    @DeleteMapping("/orders/{id}")
    public String cancelOrder(@PathVariable int id) {
        CanteenOrder o = findById(id);
        if (o == null) {
            return "Order not found";
        }
        orderList.remove(o);
        return "Order canceled successfully : " + id;
    }

    // ================= SEARCH =================
    @GetMapping("/orders/search")
    public List<CanteenOrder> searchOrders(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String status) {

        List<CanteenOrder> result = new ArrayList<>();

        for (CanteenOrder o : orderList) {
            if ((studentName == null || o.studentName.toLowerCase().contains(studentName.toLowerCase())) &&
                (status == null || o.status.equalsIgnoreCase(status))) {
                result.add(o);
            }
        }
        return result;
    }

    // ================= FIND BY ID =================
    private CanteenOrder findById(int id) {
        for (CanteenOrder o : orderList) {
            if (o.id == id)
                return o;
        }
        return null;
    }
}

/* ================= POJO CLASS ================= */

class CanteenOrder {
    public int id;
    public String studentName;
    public String items;
    public double totalAmount;
    public String status; // NEW, PREPARING, READY, DELIVERED, CANCELED

    public CanteenOrder() {}

    public CanteenOrder(int id, String studentName, String items,
                        double totalAmount, String status) {
        this.id = id;
        this.studentName = studentName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }
}
