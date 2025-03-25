package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.BookRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    private final Timer getAllBooksTimer;
    private final Timer getBookByIdTimer;

    @Autowired
    public BookService(BookRepository bookRepository, MeterRegistry meterRegistry) {
        this.bookRepository = bookRepository;
        this.getAllBooksTimer = meterRegistry.timer("bookstore.service.getAllBooks.time");
        this.getBookByIdTimer = meterRegistry.timer("bookstore.service.getBookById.time");
    }

    public Book addBook(Book book) {
        logger.info("Adding new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        logger.info("Fetching all books");
        return getAllBooksTimer.record(() -> bookRepository.findAll());
    }

    public Book getBookById(Long id) {
        logger.info("Fetching book with ID: {}", id);
        return getBookByIdTimer.record(() -> bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new ResourceNotFoundException("Book not found with ID: " + id);
                }));
    }

    public Book updateBook(Long id, Book updatedBook) {
        logger.info("Updating book with ID: {}", id);
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setPublishedDate(updatedBook.getPublishedDate());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        logger.warn("Deleting book with ID: {}", id);
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
