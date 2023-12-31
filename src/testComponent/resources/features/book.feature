Feature: the user can create and retrieve the books
  Scenario: user creates two books and retrieve both of them
    When the user creates the book "Les Misérables" written by "Victor Hugo"
    And the user creates the book "L'avare" written by "Molière"
    And the user get all books
    Then the list should contains the following books in the same order
      | name | author | reserved |
      | L'avare | Molière | false |
      | Les Misérables | Victor Hugo | false |


  Scenario: user reserves a book
    When the user creates the book "Inferno" written by "Dante"
    And Can we reserve the following book "Inferno" ?
    And the user reserves the book "Inferno" written by "Dante"
    And the user reserves the book "Inferno" written by "Dante"
    And the user get all books
    Then the list should contains the following books in the same order
      | name | author | reserved |
      | Inferno | Dante | false |
      | Inferno | Dante | true |
      | Inferno | Dante | true |
    # We test only the first line, so the code is not very relevant