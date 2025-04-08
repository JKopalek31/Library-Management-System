/* Jett Kopalek
 * CSCI 400
 * Checkout Class 
 * 
 * This class generates objects from checkouts.txt to be able to implement 
 * changes in the system
 * 
 * */

package application;

public class Checkout {
    private String patronId;
    private String isbn;

    public Checkout(String patronId, String isbn) {
        this.patronId = patronId;
        this.isbn = isbn;
    }
    public String getPatronId() { return patronId; }
    public String getIsbn() { return isbn; }
    
    @Override
    public String toString() {
        return "Patron ID: " + patronId + ", ISBN: " + isbn;
    }
}
