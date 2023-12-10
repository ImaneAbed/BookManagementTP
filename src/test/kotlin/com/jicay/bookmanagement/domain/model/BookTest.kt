package com.jicay.bookmanagement.domain.model

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test

class BookTest {
    @Test
    fun emptyTitle(){
        //Arrange
        val name: String = ""
        val author: String = "Charles_Perrault"

        //Act & Assert
        assertFailure { Book(name, author) }
            .isInstanceOf(Exception::class)
            .hasMessage("empty title")
    }
    @Test
    fun emptyAuthor(){
        //Arrange
        val name: String = "Le_Petit_Chaperon_Rouge"
        val author: String = ""

        //Act & Assert
        assertFailure { Book(name, author) }
            .isInstanceOf(Exception::class)
            .hasMessage("empty author")
    }
    @Test
    fun defaultReserved(){
        //Arrange
        val name: String = "Le_Petit_Chaperon_Rouge"
        val author: String = "Charles_Perrault"

        //Act & Assert
        assertThat(Book(name, author).reserved).isEqualTo(false)
    }
    @Test
    fun reserved(){
        //Arrange
        val name: String = "Le_Petit_Chaperon_Rouge"
        val author: String = "Charles_Perrault"
        val reserved: Boolean = true

        //Act & Assert
        assertThat((Book(name, author, reserved)).reserved).isEqualTo(reserved)
    }
}