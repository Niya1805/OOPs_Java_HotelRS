/**
 * Room.java
 *
 * PURPOSE:
 * Represents a physical room in the hotel with a room number, type,
 * price per night, floor code, and availability status.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. Class and Object state
 * 2. Primitive data types (`int`, `double`, `char`, `boolean`) and `String`
 * 3. Encapsulation (`private` instance variables with `public` getters and setters)
 * 4. Constructor Overloading (initializing a new available room vs loading a room with a specific status)
 */
public class Room {

    // Private instance variables (Encapsulation: hidden from direct external modification)
    private int roomNumber;
    private String roomType;      // e.g., "Single", "Double", "Deluxe", "Suite"
    private double pricePerNight; // e.g., 1500.0
    private char floorCode;       // e.g., 'A' for Ground/1st floor, 'B' for 2nd floor, 'C' for 3rd floor
    private boolean isAvailable;  // true if vacant, false if currently booked

    /**
     * Constructor 1 (3 parameters):
     * Creates a room that is available by default (`isAvailable = true`).
     * Automatically assigns a floor code ('A', 'B', or 'C') based on room number.
     */
    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true; // Available by default
        this.floorCode = determineFloorCode(roomNumber);
    }

    /**
     * Constructor 2 (Overloaded Constructor - 4 parameters):
     * Allows specifying the initial availability status explicitly.
     */
    public Room(int roomNumber, String roomType, double pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
        this.floorCode = determineFloorCode(roomNumber);
    }

    /**
     * Helper method using conditional `if-else` and relational operators
     * to assign a `char` floor code based on room number.
     */
    private char determineFloorCode(int number) {
        if (number >= 100 && number < 200) {
            return 'A'; // First Floor
        } else if (number >= 200 && number < 300) {
            return 'B'; // Second Floor
        } else {
            return 'C'; // Third Floor or above
        }
    }

    /**
     * Displays room information in a clean single-line format.
     */
    public void displayRoomInfo() {
        String statusText = isAvailable ? "Available" : "Occupied";
        System.out.printf("Room %-5d | Floor %-2c | Type: %-8s | Price/Night: Rs. %-8.2f | Status: %s%n",
                roomNumber, floorCode, roomType, pricePerNight, statusText);
    }

    // Public Getters and Setters (Encapsulation)
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight > 0) {
            this.pricePerNight = pricePerNight;
        }
    }

    public char getFloorCode() {
        return floorCode;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
