package com.klu.GrocertInventoryItems.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
public class GroceryInventoryController {

    private final List<GroceryItem> inventoryList = new ArrayList<>();
    private int idCounter = 300;

    // Constructor – sample data
    public GroceryInventoryController() {
        inventoryList.add(new GroceryItem(301, "Rice 25kg Bag", "Grains", 1450.0, 12, 5));
        inventoryList.add(new GroceryItem(302, "Sugar 1kg", "Grocery", 45.0, 3, 10));
        idCounter = 302;
    }

    // ================= GET ALL =================
    @GetMapping("/inventory")
    public List<GroceryItem> getAllItems() {
        return inventoryList;
    }

    // ================= GET BY ID =================
    @GetMapping("/inventory/{id}")
    public GroceryItem getItemById(@PathVariable int id) {
        return findById(id);
    }

    // ================= CREATE =================
    @PostMapping("/inventory")
    public GroceryItem addItem(@RequestBody GroceryItem newItem) {
        newItem.id = ++idCounter;
        inventoryList.add(newItem);
        return newItem;
    }

    // ================= UPDATE =================
    @PutMapping("/inventory/{id}")
    public GroceryItem updateItem(@PathVariable int id,
                                  @RequestBody GroceryItem updatedItem) {

        GroceryItem existing = findById(id);
        if (existing == null) {
            return null;
        }

        existing.itemName = updatedItem.itemName;
        existing.category = updatedItem.category;
        existing.price = updatedItem.price;
        existing.quantity = updatedItem.quantity;
        existing.reorderLevel = updatedItem.reorderLevel;

        return existing;
    }

    // ================= DELETE =================
    @DeleteMapping("/inventory/{id}")
    public String deleteItem(@PathVariable int id) {
        GroceryItem item = findById(id);
        if (item == null) {
            return "Item not found";
        }
        inventoryList.remove(item);
        return "Item deleted successfully : " + id;
    }

    // ================= SEARCH =================
    @GetMapping("/inventory/search")
    public List<GroceryItem> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean lowStock) {

        List<GroceryItem> result = new ArrayList<>();

        for (GroceryItem g : inventoryList) {
            boolean isLowStock = g.quantity <= g.reorderLevel;

            if ((name == null || g.itemName.toLowerCase().contains(name.toLowerCase())) &&
                (category == null || g.category.equalsIgnoreCase(category)) &&
                (lowStock == null || lowStock == isLowStock)) {

                result.add(g);
            }
        }
        return result;
    }

    // ================= FIND BY ID =================
    private GroceryItem findById(int id) {
        for (GroceryItem g : inventoryList) {
            if (g.id == id)
                return g;
        }
        return null;
    }
}

/* ================= POJO CLASS ================= */

class GroceryItem {
    public int id;
    public String itemName;
    public String category;
    public double price;
    public int quantity;
    public int reorderLevel;

    public GroceryItem() {}

    public GroceryItem(int id, String itemName, String category,
                       double price, int quantity, int reorderLevel) {
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }
}
