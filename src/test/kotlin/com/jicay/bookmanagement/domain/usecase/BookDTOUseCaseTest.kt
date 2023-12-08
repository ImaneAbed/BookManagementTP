package com.jicay.bookmanagement.domain.usecase

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
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
            Book("Les_Misérables", "Victor_Hugo"),
            Book("Hamlet", "William_Shakespeare")
        )

        val res = bookUseCase.getAllBooks()

        assertThat(res).containsExactly(
            Book("Hamlet", "William_Shakespeare"),
            Book("Les_Misérables", "Victor_Hugo")
        )
    }

    @Test
    fun `add book`() {
        justRun { bookPort.createBook(any()) }

        val book = Book("Les_Misérables", "Victor_Hugo")

        bookUseCase.addBook(book)

        verify(exactly = 1) { bookPort.createBook(book) }
    }

    @Test
    fun `reserve should return true and set reserved to true if the book is not already reserved`() {
        justRun { bookPort.updateBook(any()) }

        val book = Book("Les_Misérables", "Victor_Hugo")
        bookUseCase.reserve(book)

        assertTrue(book.reserved)
        verify(exactly = 1) { bookPort.updateBook(book) }

    }


    @Test
    fun `reserve should return false if the book is already reserved`() {
        justRun { bookPort.updateBook(any()) }

        val book = Book("Les_Misérables", "Victor_Hugo", reserved = true)

        assertFailure { bookUseCase.reserve(book) }
            .isInstanceOf(Exception::class)
            .hasMessage("already reserved")
    }
}