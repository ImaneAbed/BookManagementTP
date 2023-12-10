package com.jicay.bookmanagement

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.springframework.boot.test.web.server.LocalServerPort

class BookStepDefs {
    @LocalServerPort
    private var port: Int? = 0

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @When("the user creates the book {string} written by {string}")
    fun createBook(title: String, author: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "name": "$title",
                      "author": "$author",
                      "reserved": false
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @When("Can we reserve the following book {string} ?")
    fun canWeReserveThisBook(title: String) {
        val firstBook = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200).extract().body().jsonPath().getList("", Map::class.java)[0]
        //val actualBook = actualBooks[0]
        //assertThat(actualBook["name"]).isEqualTo(title)
        if (firstBook["name"] == title){
            assertThat(firstBook["reserved"]).isEqualTo(false)
        }

    }

    @When("the user reserves the book {string} written by {string}")
    fun updateReservationStatus(title: String, author: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "name": "$title",
                      "author": "$author",
                      "reserved": true
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @When("the user get all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
    }


    @Then("the list should contains the following books in the same order")
    fun shouldHaveListOfBooks(payload: List<Map<String, Any>>) {
        val actualBooks = lastBookResult.extract().body().jsonPath().getList("", Map::class.java)
        for ((index, expectedBook) in payload.withIndex()) {
            val actualBook = actualBooks[index] as Map<*, *>
            assertThat(actualBook["name"]).isEqualTo(expectedBook["name"])
            assertThat(actualBook["author"]).isEqualTo(expectedBook["author"])
            assertThat(actualBook["reserved"].toString()).isEqualTo(expectedBook["reserved"])
        }
    }

    companion object {
        lateinit var lastBookResult: ValidatableResponse
    }
}