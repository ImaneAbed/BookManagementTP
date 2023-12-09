package com.jicay.bookmanagement

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
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
        //val isReserved: Boolean = convertToBoolean(reserved)
        
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


    /*Scenario: user reserves a book
    Given the user has the book "Les Misérables" in the library
    When the user reserves the book "Les Misérables"
    Then the book "Les Misérables" should be marked as reserved
*/


    /*
    @Given("the user has the book {string} in the library")
    fun givenUserHasBookInTheLibrary(title: String) {
        // Logique pour s'assurer que le livre spécifié existe dans la bibliothèque
        // Cela pourrait inclure l'ajout du livre à la bibliothèque si nécessaire
        createBook(title, "Auteur par défaut", false) // Créez le livre avec l'auteur par défaut et non réservé
    }*/

/*
    @When("the user reserve the book {string}")
    fun updateBook(title: String, author: String, reserved: Boolean) {
        //val isReserved: Boolean = convertToBoolean(reserved)

        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "name": "$title",
                      "author": "$author",
                      "reserved": $reserved
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }*/

    /*
    @When("the user reserves the book {string}")
    fun reserveBook(title: String) {
        // Logique pour réserver le livre spécifié
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .patch("/books/reserve/$title") // Assurez-vous d'ajuster l'URL selon votre API
            .then()
            .statusCode(200)
    }*/

    /*
    @Then("the book {string} should be marked as reserved")
    fun bookShouldBeMarkedAsReserved(title: String) {
        // Logique pour vérifier que le livre est marqué comme réservé
        val reservedStatus = // Obtenir le statut de réservation du livre (par exemple, en faisant une requête GET)
            assertThat(reservedStatus).isEqualTo(true)
    }*/

    @When("the user get all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
    }

    /*@Then("athe list should contains the following books in the same order")
    fun ashouldHaveListOfBooks(payload: List<Map<String, Any>>) {
        val expectedResponse = payload.joinToString(separator = ",", prefix = "[", postfix = "]") { line ->
            """
                ${
                line.entries.joinToString(separator = ",", prefix = "{", postfix = "}") {
                    """"${it.key}": "${it.value}""""
                }
            }
            """.trimIndent()

        }
        assertThat(lastBookResult.extract().body().jsonPath().prettify())
            .isEqualTo(JsonPath(expectedResponse).prettify())

    }*/

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