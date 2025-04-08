# Better Library Management System

## Description

The **Better Library Management System** is a Java-based application designed to manage a small library's operations. This program allows users to perform CRUD (Create, Read, Update, Delete) operations for books, patrons, and checkouts. The system supports the following features:

- **Check-in/Check-out Books**: Track the borrowing and returning of books.
- **Manage Patrons**: Add, remove, and update patron information.
- **Manage Books**: Add, remove, and update book information.
- **Persistent Storage**: All data is saved to text files (`books.txt`, `checkouts.txt`, and `patrons.txt`) when the program is exited. The user will be displayed the file directory that holds the data files.

## Features

1. **Check-in/Check-out Books**
   - Allows users to check out books to patrons and track the return of books.

2. **Manage Books**
   - Add new books, remove outdated books, or update existing book information (title, author, etc.).

3. **Manage Patrons**
   - Add new patrons, update patron details, or remove patrons from the system.

4. **Persistent Data Storage**
   - When the program is closed, the system saves all books, patrons, and checkouts to corresponding text files (`books.txt`, `checkouts.txt`, and `patrons.txt`).

5. **Data Directory Information**
   - Displays the file directory containing the data files when the program exits.

## Installation

1. Clone or download the project repository.

2. Ensure you have Java installed on your system. You can download it from [here](https://www.java.com/en/download/).

3. Open the project in an IDE (e.g., IntelliJ IDEA, Eclipse, or NetBeans).

4. Compile and run the project using the following command:


## Usage

- After launching the application, users can perform the following actions through the GUI:
1. **Add Books**: Add a new book by entering the book details (title, author, etc.).
2. **Manage Patrons**: Add new patrons or remove them.
3. **Check-in/Check-out Books**: Checkout books to patrons and track the return.
4. **View Data**: View the status of books, patrons, and checkouts.

## Data Files

The system uses the following text files to store data:

- **books.txt**: Stores information about all books in the library.
- **patrons.txt**: Stores information about patrons of the library.
- **checkouts.txt**: Stores information about book checkouts and returns.

When the program is closed, the current state of the library is saved in these files.

## Author

Jett Kopalek  
CSCI 400 - Software Engineering  
Better Library Management System

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
