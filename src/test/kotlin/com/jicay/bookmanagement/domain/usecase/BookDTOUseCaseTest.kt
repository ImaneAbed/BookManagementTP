package com.jicay.bookmanagement.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookDTOUseCaseTest {

    @InjectMockKs
    private lateinit var bookUseCase: BookUseCase

    @MockK
    private lateinit var bookPort: BookPort

    @Test
    fun `get all books should returns all books sorted by name`() {
        every { bookPort.getAllBooks() } returns listOf(
            Book("Les Misérables", "Victor Hugo"),
            Book("Hamlet", "William Shakespeare")
        )

        val res = bookUseCase.getAllBooks()

        assertThat(res).containsExactly(
            Book("Hamlet", "William Shakespeare"),
            Book("Les Misérables", "Victor Hugo")
        )
    }

    @Test
    fun `add book`() {
        justRun { bookPort.createBook(any()) }

        val book = Book("Les Misérables", "Victor Hugo")

        bookUseCase.addBook(book)

        verify(exactly = 1) { bookPort.createBook(book) }
    }

    @Test
    fun `reserve should return true and set reserved to true if the book is not already reserved`() {
        val book = Book("Les Misérables", "Victor Hugo")
        every { bookPort.getAllBooks() } returns listOf(book)

        val result = bookUseCase.reserve(book)

        assertTrue(result)
        assertTrue(book.reserved)
    }

    @Test
    fun `reserve should return false if the book is already reserved`() {
        val reservedBook = Book("Hamlet", "William Shakespeare", reserved = true)
        every { bookPort.getAllBooks() } returns listOf(reservedBook)

        val result = bookUseCase.reserve(reservedBook)

        assertFalse(result)
        assertTrue(reservedBook.reserved)
    }
}