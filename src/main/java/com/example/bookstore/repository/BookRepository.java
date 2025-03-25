package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Marks this interface as a repository component, which will be managed by Spring.
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // This interface extends JpaRepository, providing CRUD operations for the Book entity.
    // No need to implement methods manually, as JpaRepository provides built-in methods like:
    // - save(Book book): Saves a book to the database.
    // - findById(Long id): Retrieves a book by its ID.
    // - findAll(): Fetches all books from the database.
    // - delete(Book book): Deletes a book from the database.
}
