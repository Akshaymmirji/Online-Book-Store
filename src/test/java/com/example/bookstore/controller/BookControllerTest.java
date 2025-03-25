package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(
                new Book(1L, "Spring in Action", "Craig Walls", new BigDecimal("39.99"), LocalDate.of(2019, 3, 12)),
                new Book(2L, "Clean Code", "Robert C. Martin", new BigDecimal("49.99"), LocalDate.of(2008, 8, 1))
        );
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", new BigDecimal("45.99"), LocalDate.of(2018, 5, 8));
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void testAddBook() throws Exception {
        Book book = new Book(1L, "The Pragmatic Programmer", "Andrew Hunt", new BigDecimal("42.99"), LocalDate.of(1999, 10, 30));
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"The Pragmatic Programmer\", \"author\": \"Andrew Hunt\", \"price\": 42.99, \"publishedDate\": \"1999-10-30\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Pragmatic Programmer"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book updatedBook = new Book(1L, "Refactoring", "Martin Fowler", new BigDecimal("47.99"), LocalDate.of(1999, 7, 8));
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Refactoring\", \"author\": \"Martin Fowler\", \"price\": 47.99, \"publishedDate\": \"1999-07-08\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBook(1L);
    }
}
