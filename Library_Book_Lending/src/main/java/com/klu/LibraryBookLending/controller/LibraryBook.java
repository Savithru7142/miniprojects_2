package com.klu.LibraryBookLending.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryBook {

    private final List<Book> bookList = new ArrayList<>();
    private int idCounter = 200;

    // Constructor – sample data
    public LibraryBook() {
        bookList.add(new Book(201, "PFSD", "DEEPAK>V", "PYTHON - FULLSTACK", true, null));
        bookList.add(new Book(202, "Effective Java", "Joshua Bloch", "Programming", false, "Sharvan"));
        idCounter = 202;
    }

    // ================= GET ALL =================
    @GetMapping("/books")
    public List<Book> getAllBooks(
            @RequestParam(required = false) Boolean available) {

        if (available == null) {
            return bookList;
        }

        List<Book> result = new ArrayList<>();
        for (Book b : bookList) {
            if (b.available == available) {
                result.add(b);
            }
        }
        return result;
    }

    // ================= GET BY ID =================
    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable int id) {
        return findById(id);
    }

    // ================= CREATE =================
    @PostMapping("/books")
    public Book addBook(@RequestBody Book newBook) {
        newBook.id = ++idCounter;
        newBook.available = true;
        newBook.borrowerName = null;
        bookList.add(newBook);
        return newBook;
    }

    // ================= UPDATE =================
    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable int id,
                           @RequestBody Book updatedBook) {

        Book existing = findById(id);
        if (existing == null) {
            return null;
        }

        existing.title = updatedBook.title;
        existing.author = updatedBook.author;
        existing.category = updatedBook.category;
        existing.available = updatedBook.available;
        existing.borrowerName = updatedBook.borrowerName;

        return existing;
    }

    // ================= DELETE =================
    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable int id) {
        Book b = findById(id);
        if (b == null) {
            return "Book not found";
        }
        bookList.remove(b);
        return "Book deleted successfully : " + id;
    }

    // ================= SEARCH =================
    @GetMapping("/books/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category) {

        List<Book> result = new ArrayList<>();

        for (Book b : bookList) {
            if ((title == null || b.title.toLowerCase().contains(title.toLowerCase())) &&
                (author == null || b.author.toLowerCase().contains(author.toLowerCase())) &&
                (category == null || b.category.equalsIgnoreCase(category))) {
                result.add(b);
            }
        }
        return result;
    }

    // ================= FIND BY ID =================
    private Book findById(int id) {
        for (Book b : bookList) {
            if (b.id == id)
                return b;
        }
        return null;
    }
}

/* ================= POJO CLASS ================= */

class Book {
    public int id;
    public String title;
    public String author;
    public String category;
    public boolean available;
    public String borrowerName;

    public Book() {}

    public Book(int id, String title, String author,
                String category, boolean available, String borrowerName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
        this.borrowerName = borrowerName;
    }
}
