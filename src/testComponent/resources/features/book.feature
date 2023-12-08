Feature: the user can create and retrieve the books
#  Scenario: user creates two books and retrieve both of them
#    When the user creates the book "Les_Misérables" written by "Victor_Hugo" and reserved is false
#    #When the user creates the book "Les_Misérables" written by "Victor_Hugo"
    #And the user creates the book "L_avare" written by "Molière" and reserved is false
    #And the user get all books
#    Then the list should contains the following books in the same order
#      | name | author | reserved |
    #  | L_avare | Molière | false |
#      | Les_Misérables | Victor_Hugo | false |


  Scenario: User creates a reserved book
    When the user creates the book "Harry Potter" written by "J.K. Rowling" with reserved status true
    Then the list should contains the following books in the same order
      | name         | author       | reserved |
      | "Harry Potter"| "J.K. Rowling"| true     |
