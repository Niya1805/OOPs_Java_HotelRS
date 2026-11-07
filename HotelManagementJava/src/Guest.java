/**
 * Guest.java
 *
 * PURPOSE:
 * Represents a hotel guest. Inherits common personal details (name, age, contactNumber)
 * from the abstract `Person` class and adds a unique `guestId`.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. Inheritance (`extends Person`)
 * 2. `super` keyword (calling the parent class constructor and parent method)
 * 3. Constructor Overloading (two constructors with different parameter lists)
 * 4. Encapsulation (`private int guestId` with public getter)
 * 5. Method Overriding (`@Override` on `getRole()` and `displayDetails()`)
 */
public class Guest extends Person {

    // Private instance variable specific to a Guest
    private int guestId;

    /**
     * Constructor 1 (Full Constructor):
     * Initializes a Guest with ID, name, age, and contact number.
     */
    public Guest(int guestId, String name, int age, String contactNumber) {
        // Call the parent class (Person) constructor first
        super(name, age, contactNumber);
        this.guestId = guestId;
    }

    /**
     * Constructor 2 (Overloaded Constructor):
     * Initializes a Guest when age is not provided (sets default age to 18).
     * Demonstrates Constructor Overloading.
     */
    public Guest(int guestId, String name, String contactNumber) {
        super(name, 18, contactNumber);
        this.guestId = guestId;
    }

    /**
     * Implementing the abstract method from Person.
     */
    @Override
    public String getRole() {
        return "Hotel Guest";
    }

    /**
     * Overriding the parent class displayDetails() method to include Guest ID.
     * Demonstrates Method Overriding and Runtime Polymorphism.
     */
    @Override
    public void displayDetails() {
        System.out.println("Guest ID       : " + guestId);
        // Reuse the display logic from the parent Person class
        super.displayDetails();
    }

    // Getter for guestId (Encapsulation)
    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
}
