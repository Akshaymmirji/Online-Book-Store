package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    private final Timer getAllBooksTimer;
    private final Timer getBookByIdTimer;

    @Autowired
    public BookController(BookService bookService, MeterRegistry meterRegistry) {
        this.bookService = bookService;
        this.getAllBooksTimer = meterRegistry.timer("bookstore.getAllBooks.time");
        this.getBookByIdTimer = meterRegistry.timer("bookstore.getBookById.time");
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        logger.info("Adding new book: {}", book.getTitle());
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("Fetching all books");
        List<Book> books = getAllBooksTimer.record(() -> bookService.getAllBooks());
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Fetching book with ID: {}", id);
        Book book = getBookByIdTimer.record(() -> bookService.getBookById(id));
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        logger.info("Updating book with ID: {}", id);
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        logger.warn("Deleting book with ID: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}
