import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HotelManagementSystem.java
 *
 * PURPOSE:
 * The core controller class that manages the hotel's rooms, registered guests,
 * bookings, billing calculations, and text-file persistence.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. Fixed-Size Array of Objects (`Room[] rooms`) for managing hotel rooms
 * 2. Dynamic Collection (`ArrayList<Guest>` and `ArrayList<Booking>`) for guests and bookings
 * 3. Passing Objects as Method Parameters and Returning Objects from Methods
 * 4. Runtime Polymorphism (`Person personRef = newGuest; personRef.displayDetails();`)
 * 5. Recursion (`calculateTotalRevenueRecursive(int index)`)
 * 6. `final` Keyword (`HOTEL_NAME` and `DATA_FILE_PATH` constants)
 * 7. File Handling (`FileWriter`, `BufferedWriter`, `FileReader`, `BufferedReader`)
 * 8. Exception Handling (`try-catch-finally` during file operations)
 */
public class HotelManagementSystem {

    // `final` constants: values cannot be modified after initialization
    public static final String HOTEL_NAME = "Sunrise Grand Hotel";
    public static final String DATA_FILE_PATH = "data/bookings.txt";

    /**
     * Resolves the bookings file path whether the user runs the program from inside
     * `HotelManagementJava/` or from its parent workspace folder.
     */
    private File getBookingsFile() {
        File directFile = new File(DATA_FILE_PATH);
        if (directFile.exists()) {
            return directFile;
        }
        File nestedDirFile = new File("HotelManagementJava/" + DATA_FILE_PATH);
        if (nestedDirFile.exists() || new File("HotelManagementJava").isDirectory()) {
            return nestedDirFile;
        }
        return directFile;
    }

    // Fixed-size array of Room objects (a hotel has a fixed number of physical rooms)
    private Room[] rooms;

    // Dynamic ArrayLists for Guests and Bookings (can grow or shrink at runtime)
    private ArrayList<Guest> guests;
    private ArrayList<Booking> bookings;

    // Counters to generate unique IDs using the increment operator (++)
    private int nextGuestId;
    private int nextBookingId;

    /**
     * Constructor: initializes rooms array, lists, ID counters, and sample data.
     */
    public HotelManagementSystem() {
        rooms = new Room[8]; // Fixed array of 8 hotel rooms
        guests = new ArrayList<Guest>();
        bookings = new ArrayList<Booking>();
        nextGuestId = 1;
        nextBookingId = 1001;

        initializeRooms();
        initializeSampleData();
    }

    /**
     * Initializes the fixed-size `Room[]` array with 8 rooms across 3 floors.
     * Demonstrates array initialization and Constructor Overloading in `Room`.
     */
    private void initializeRooms() {
        rooms[0] = new Room(101, "Single", 1500.0);
        rooms[1] = new Room(102, "Single", 1500.0);
        rooms[2] = new Room(103, "Double", 2500.0);
        rooms[3] = new Room(201, "Double", 2500.0);
        rooms[4] = new Room(202, "Deluxe", 3800.0);
        rooms[5] = new Room(203, "Deluxe", 3800.0);
        rooms[6] = new Room(301, "Suite", 5500.0);
        // Using the overloaded 4-parameter Room constructor for Room 302
        rooms[7] = new Room(302, "Suite", 5500.0, true);
    }

    /**
     * Pre-loads fictional guests and one sample booking if no saved data file exists yet,
     * so the student can immediately demonstrate features during an exam.
     */
    private void initializeSampleData() {
        File dataFile = getBookingsFile();
        if (dataFile.exists()) {
            loadBookingsFromFile();
            return;
        }

        // Fictional sample guests (Demonstrates both Guest constructors)
        Guest sampleGuest1 = new Guest(nextGuestId++, "Aarav Sharma", 28, "9876543210");
        Guest sampleGuest2 = new Guest(nextGuestId++, "Priya Nair", "9123456780"); // Overloaded constructor (default age 18)
        guests.add(sampleGuest1);
        guests.add(sampleGuest2);

        // Create 1 sample active booking in Room 101 for Aarav Sharma
        Room room101 = findRoomByNumber(101);
        if (room101 != null) {
            room101.setAvailable(false);
            Booking sampleBooking = new Booking(nextBookingId++, sampleGuest1, room101, 2);
            bookings.add(sampleBooking);
        }
    }

    // =========================================================================
    // FEATURE 1: DISPLAY ROOMS (Demonstrates array iteration with a `for` loop)
    // =========================================================================
    public void displayAllRooms() {
        System.out.println("\n================ ROOM LIST AT " + HOTEL_NAME.toUpperCase() + " ================");
        int availableCount = 0;

        // Standard `for` loop iterating through the fixed-size `Room[]` array
        for (int i = 0; i < rooms.length; i++) {
            rooms[i].displayRoomInfo();
            if (rooms[i].isAvailable()) {
                availableCount++; // Increment operator
            }
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Total Rooms: " + rooms.length + " | Available: " + availableCount
                + " | Occupied: " + (rooms.length - availableCount));
    }

    // =========================================================================
    // FEATURE 2: ADD GUEST (Demonstrates Runtime Polymorphism & Returning Objects)
    // =========================================================================
    public Guest addGuest(String name, int age, String contactNumber) {
        int id = nextGuestId++;
        Guest newGuest = new Guest(id, name, age, contactNumber);
        guests.add(newGuest);

        System.out.println("\n[SUCCESS] Guest registered successfully!");
        // RUNTIME POLYMORPHISM DEMONSTRATION:
        // Parent class (`Person`) reference pointing to a Child class (`Guest`) object.
        // At runtime, Java calls `Guest.displayDetails()` via dynamic method dispatch.
        Person personReference = newGuest;
        personReference.displayDetails();

        return newGuest; // Returning an object from a method
    }

    public void displayAllGuests() {
        if (guests.isEmpty()) {
            System.out.println("\nNo guests registered yet.");
            return;
        }
        System.out.println("\n================ REGISTERED GUESTS ================");
        for (int i = 0; i < guests.size(); i++) {
            Person personRef = guests.get(i); // Upcasting to Person reference
            personRef.displayDetails();
            System.out.println("---------------------------------------------------");
        }
    }

    // =========================================================================
    // FEATURE 3: BOOK A ROOM (Demonstrates Passing Objects to a Method)
    // =========================================================================
    public Booking bookRoom(Guest guest, int roomNumber, int nights) {
        Room selectedRoom = findRoomByNumber(roomNumber);

        // Validation using relational and logical operators
        if (selectedRoom == null) {
            System.out.println("[ERROR] Room " + roomNumber + " does not exist.");
            return null;
        }
        if (!selectedRoom.isAvailable()) {
            System.out.println("[ERROR] Room " + roomNumber + " is currently occupied and cannot be booked.");
            return null;
        }
        if (nights <= 0) {
            System.out.println("[ERROR] Number of nights must be at least 1.");
            return null;
        }

        // Update room state (demonstrates modifying object state via setter)
        selectedRoom.setAvailable(false);

        // Create booking by passing `guest` and `selectedRoom` object references
        int bookingId = nextBookingId++;
        Booking newBooking = new Booking(bookingId, guest, selectedRoom, nights);
        bookings.add(newBooking);

        System.out.println("\n[SUCCESS] Room booked successfully! Here are your booking details:");
        newBooking.displayBookingDetails();
        return newBooking;
    }

    // =========================================================================
    // FEATURE 4: VIEW BOOKINGS (Also shows Recursive Total Revenue Calculation)
    // =========================================================================
    public void viewAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
            return;
        }

        System.out.println("\n================ ALL HOTEL BOOKINGS ================");
        for (int i = 0; i < bookings.size(); i++) {
            bookings.get(i).displayBookingDetails();
        }

        // Demonstrate RECURSION by calculating the sum of all booking bills recursively
        double totalRevenue = calculateTotalRevenueRecursive(0);
        System.out.printf("Total Revenue Across All Bookings (Calculated Recursively): Rs. %.2f%n", totalRevenue);
    }

    /**
     * RECURSION DEMONSTRATION:
     * Calculates total revenue across all bookings in the `bookings` list.
     * - Base Case: when `index` reaches `bookings.size()`, return 0.0.
     * - Recursive Step: current booking's cost + sum of remaining bookings from `index + 1`.
     */
    public double calculateTotalRevenueRecursive(int index) {
        // Base case: end of list reached
        if (index >= bookings.size()) {
            return 0.0;
        }
        // Recursive step: add current booking's totalCost to the recursive result of the rest
        return bookings.get(index).getTotalCost() + calculateTotalRevenueRecursive(index + 1);
    }

    // =========================================================================
    // FEATURE 5: CANCEL BOOKING
    // =========================================================================
    public boolean cancelBooking(int bookingId) {
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.getBookingId() == bookingId) {
                // Free the room if it was currently occupied by this booking
                if (!b.isCheckedOut()) {
                    b.getRoom().setAvailable(true);
                }
                bookings.remove(i);
                System.out.println("\n[SUCCESS] Booking ID " + bookingId + " has been cancelled.");
                System.out.println("Room " + b.getRoom().getRoomNumber() + " is now marked as Available.");
                return true;
            }
        }
        System.out.println("\n[ERROR] Booking ID " + bookingId + " not found.");
        return false;
    }

    // =========================================================================
    // FEATURE 6: CHECK OUT
    // =========================================================================
    public boolean checkOut(int bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            System.out.println("\n[ERROR] Booking ID " + bookingId + " not found.");
            return false;
        }
        if (booking.isCheckedOut()) {
            System.out.println("\n[INFO] Booking ID " + bookingId + " has already checked out.");
            return false;
        }

        // Complete stay: mark booking as checked out and make the room available again
        booking.setCheckedOut(true);
        booking.getRoom().setAvailable(true);

        System.out.println("\n[SUCCESS] Check-out completed for Guest: " + booking.getGuest().getName());
        System.out.println("Room " + booking.getRoom().getRoomNumber() + " is now Available for new guests.");
        booking.displayBookingDetails();
        return true;
    }

    // =========================================================================
    // FEATURE 7: CALCULATE BILL (Demonstrates Method Overloading on Booking)
    // =========================================================================
    public void calculateAndDisplayBill(int bookingId, double discountPercent, double extraFoodCharges) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            System.out.println("\n[ERROR] Booking ID " + bookingId + " not found.");
            return;
        }

        double updatedBill;
        // Demonstrate compile-time selection of overloaded `calculateBill` methods
        if (discountPercent == 0.0 && extraFoodCharges == 0.0) {
            // Calls calculateBill() with 0 parameters
            updatedBill = booking.calculateBill();
        } else if (extraFoodCharges == 0.0) {
            // Calls overloaded calculateBill(double discountPercent) with 1 parameter
            updatedBill = booking.calculateBill(discountPercent);
        } else {
            // Calls overloaded calculateBill(double discountPercent, double extraFoodCharges) with 2 parameters
            updatedBill = booking.calculateBill(discountPercent, extraFoodCharges);
        }

        booking.setTotalCost(updatedBill);

        System.out.println("\n================ DETAILED BILL BREAKDOWN ================");
        System.out.println("Booking ID         : " + booking.getBookingId());
        System.out.println("Guest Name         : " + booking.getGuest().getName());
        System.out.println("Room Number & Type : " + booking.getRoom().getRoomNumber() + " (" + booking.getRoom().getRoomType() + ")");
        System.out.printf("Base Room Charge   : Rs. %.2f (%d nights x Rs. %.2f)%n",
                booking.getRoom().getPricePerNight() * booking.getNumberOfNights(),
                booking.getNumberOfNights(),
                booking.getRoom().getPricePerNight());
        System.out.printf("Discount Applied   : %.1f%%%n", discountPercent);
        System.out.printf("Extra Food Charges : Rs. %.2f%n", extraFoodCharges);
        System.out.printf("GST Rate           : %.0f%%%n", Billable.GST_RATE * 100);
        System.out.printf("Fixed Service Fee  : Rs. %.2f%n", Booking.FIXED_SERVICE_FEE);
        System.out.printf("FINAL TOTAL BILL   : Rs. %.2f%n", updatedBill);
        System.out.println("Loyalty Points     : " + booking.calculateLoyaltyPoints() + " pts");
        System.out.println("=========================================================");
    }

    // =========================================================================
    // FEATURE 8: SEARCH METHODS (Demonstrates Returning Objects from Methods)
    // =========================================================================

    /**
     * Searches the `Room[]` array by room number and returns the `Room` object (or `null` if not found).
     */
    public Room findRoomByNumber(int roomNumber) {
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getRoomNumber() == roomNumber) {
                return rooms[i];
            }
        }
        return null;
    }

    /**
     * Searches the `bookings` list by booking ID and returns the `Booking` object (or `null`).
     */
    public Booking findBookingById(int bookingId) {
        for (int i = 0; i < bookings.size(); i++) {
            Booking current = bookings.get(i);
            if (current.getBookingId() == bookingId) {
                return current;
            }
        }
        return null;
    }

    /**
     * Searches the `guests` list by guest ID and returns the `Guest` object (or `null`).
     */
    public Guest findGuestById(int guestId) {
        for (int i = 0; i < guests.size(); i++) {
            Guest current = guests.get(i);
            if (current.getGuestId() == guestId) {
                return current;
            }
        }
        return null;
    }

    // =========================================================================
    // FEATURE 9: FILE HANDLING (Save and Load Bookings using Text File)
    // =========================================================================

    /**
     * Writes all current bookings to `data/bookings.txt` using `FileWriter` and `BufferedWriter`.
     * Demonstrates `try-catch-finally` exception handling for file I/O.
     */
    public void saveBookingsToFile() {
        BufferedWriter writer = null;
        try {
            File file = getBookingsFile();
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < bookings.size(); i++) {
                writer.write(bookings.get(i).toFileString());
                writer.newLine();
            }
            System.out.println("\n[SUCCESS] Saved " + bookings.size() + " booking(s) to " + file.getPath());
        } catch (IOException e) {
            System.out.println("\n[ERROR] Could not save bookings to file: " + e.getMessage());
        } finally {
            // The `finally` block always executes to close the file resource safely
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("[ERROR] Failed to close file writer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Reads saved bookings from `data/bookings.txt` using `FileReader` and `BufferedReader`.
     * Reconstructs `Guest` and `Booking` objects and updates room availability.
     */
    public void loadBookingsFromFile() {
        File file = getBookingsFile();
        if (!file.exists()) {
            System.out.println("\n[INFO] No saved booking file found at " + file.getPath() + ".");
            return;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            bookings.clear();
            guests.clear();

            // Reset all rooms to available before applying loaded booking states
            for (int i = 0; i < rooms.length; i++) {
                rooms[i].setAvailable(true);
            }

            String line;
            int maxGuestId = 0;
            int maxBookingId = 1000;

            // `while` loop reading the file line by line until EOF (`null`)
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 9) {
                    continue;
                }

                int bookingId = Integer.parseInt(parts[0]);
                int guestId = Integer.parseInt(parts[1]);
                String guestName = parts[2];
                int guestAge = Integer.parseInt(parts[3]);
                String guestContact = parts[4];
                int roomNumber = Integer.parseInt(parts[5]);
                int nights = Integer.parseInt(parts[6]);
                double totalCost = Double.parseDouble(parts[7]);
                boolean isCheckedOut = Boolean.parseBoolean(parts[8]);

                // Reuse existing guest if already loaded, otherwise create and add
                Guest guest = findGuestById(guestId);
                if (guest == null) {
                    guest = new Guest(guestId, guestName, guestAge, guestContact);
                    guests.add(guest);
                }

                Room room = findRoomByNumber(roomNumber);
                if (room != null) {
                    if (!isCheckedOut) {
                        room.setAvailable(false);
                    }
                    Booking loadedBooking = new Booking(bookingId, guest, room, nights, totalCost, isCheckedOut);
                    bookings.add(loadedBooking);
                }

                if (guestId > maxGuestId) {
                    maxGuestId = guestId;
                }
                if (bookingId > maxBookingId) {
                    maxBookingId = bookingId;
                }
            }

            nextGuestId = maxGuestId + 1;
            nextBookingId = maxBookingId + 1;
            System.out.println("\n[SUCCESS] Loaded " + bookings.size() + " booking(s) from " + DATA_FILE_PATH);
        } catch (IOException | NumberFormatException e) {
            System.out.println("\n[ERROR] Problem reading bookings file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("[ERROR] Failed to close file reader: " + e.getMessage());
                }
            }
        }
    }
}
