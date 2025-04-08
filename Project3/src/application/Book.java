/* Jett Kopalek
 * CSCI 400
 * Book Class 
 * 
 * This class generates objects from books.txt to be able to implement 
 * changes in the system
 * 
 * */

package application;

public class Book {
    private String title;
    private String isbn;
    private String author;

    public Book(String title, String isbn, String author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
    }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public String getAuthor() { return author; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    @Override
    public String toString() {
        return "Title: " + title + ", ISBN: " + isbn + ", Author: " + author;
    }
}
