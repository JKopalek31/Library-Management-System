/* Jett Kopalek
 * CSCI 400
 * Patron Class 
 * 
 * This class generates objects from patrons.txt to be able to implement 
 * changes in the system
 * 
 * */

package application;

public class Patron {
    private String name;
    private String id;

    public Patron(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public String getName() { return name; }
    public String getId() { return id; }
    public void setName(String name) { this.name = name; }
    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id;
    }
}
