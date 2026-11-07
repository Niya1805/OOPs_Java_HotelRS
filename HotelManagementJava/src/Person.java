/**
 * Person.java
 *
 * PURPOSE:
 * Represents a general person with basic details (name, age, contact number).
 * This is an ABSTRACT class because in our hotel system we never create a generic
 * "Person" object directly; instead, we create specific subclasses such as "Guest".
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. Abstract Class (`abstract class Person`)
 * 2. Abstract Method (`public abstract String getRole()`)
 * 3. `protected` Access Modifier (allows subclasses like `Guest` to access fields directly)
 * 4. Encapsulation (controlled access via getters and setters)
 * 5. Base Class for Inheritance and Runtime Polymorphism
 */
public abstract class Person {

    // Protected fields: accessible within this class, same package, and subclasses (like Guest)
    protected String name;
    protected int age;
    protected String contactNumber;

    /**
     * Constructor to initialize common person details.
     */
    public Person(String name, int age, String contactNumber) {
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
    }

    /**
     * Abstract method: has no body here.
     * Every subclass (like Guest) MUST override and provide an implementation for this method.
     */
    public abstract String getRole();

    /**
     * Concrete method: displays basic person details.
     * Subclasses can override this method to show extra information (Method Overriding).
     */
    public void displayDetails() {
        System.out.println("Role           : " + getRole());
        System.out.println("Name           : " + name);
        System.out.println("Age            : " + age);
        System.out.println("Contact Number : " + contactNumber);
    }

    // Public getter and setter methods (Encapsulation)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
