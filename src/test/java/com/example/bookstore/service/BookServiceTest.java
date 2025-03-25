package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBook() {
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", new BigDecimal("45.99"), LocalDate.of(2018, 5, 8));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.addBook(book);

        assertNotNull(savedBook);
        assertEquals("Effective Java", savedBook.getTitle());
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(
                new Book(1L, "Spring in Action", "Craig Walls", new BigDecimal("39.99"), LocalDate.of(2019, 3, 12)),
                new Book(2L, "Clean Code", "Robert C. Martin", new BigDecimal("49.99"), LocalDate.of(2008, 8, 1))
        );
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    void testGetBookById() {
        Book book = new Book(1L, "Java Concurrency in Practice", "Brian Goetz", new BigDecimal("39.99"), LocalDate.of(2006, 5, 20));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals("Java Concurrency in Practice", foundBook.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void testUpdateBook() {
        Book existingBook = new Book(1L, "Old Title", "Unknown", new BigDecimal("25.99"), LocalDate.of(2015, 7, 10));
        Book updatedBook = new Book(1L, "New Title", "Updated Author", new BigDecimal("30.99"), LocalDate.of(2022, 10, 5));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertEquals("New Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book(1L, "Spring Boot", "Pivotal", new BigDecimal("29.99"), LocalDate.of(2020, 2, 2));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }
}
