# Complete Code Explanation (File-by-File Guide)

This document explains every file in the **Hotel Management System** project so you can confidently understand and explain how each class works and why it was written that way.

---

## 1. Execution Flow Overview

When you run the program (`java -cp src Main`), execution follows this clear path:

1. **`Main.main()`** starts first:
   - Creates a `Scanner` object to read keyboard input.
   - Creates a `HotelManagementSystem` object (`hotel`), which automatically initializes the 8 hotel rooms (`Room[]`) and loads saved bookings from `data/bookings.txt` (or creates sample data if no file exists).
2. **Menu Loop (`do-while` in `Main.java`)**:
   - Prints options `1` through `10`.
   - Reads the user's integer choice safely inside `readIntInput()` using a `try-catch` block.
   - Uses a `switch (choice)` statement to call the matching method on the `hotel` object.
3. **Controller Operations (`HotelManagementSystem.java`)**:
   - Coordinates between `Room`, `Guest`, and `Booking` objects.
   - Searches rooms/guests/bookings, updates room availability, calculates bills, and saves/loads text data.

---

## 2. File-by-File Breakdown

### File 1: `src/Person.java` (Abstract Base Class)

#### Purpose
Provides a common template for any person in the hotel system (`name`, `age`, `contactNumber`). Because the hotel never creates a generic "Person" without a specific role, `Person` is declared as an `abstract class`.

#### Variables and Data Types
- `protected String name;` — Person's full name.
- `protected int age;` — Person's age in years.
- `protected String contactNumber;` — Phone number stored as a `String` (since phone numbers have 10 digits and are not used in arithmetic).
- **Why `protected`?** So child classes like `Guest` can directly access `name`, `age`, and `contactNumber`.

#### Constructors and Methods
- `public Person(String name, int age, String contactNumber)` — Initializes the three fields.
- `public abstract String getRole();` — An **abstract method** with no body. Forces any subclass (`Guest`) to provide its own implementation.
- `public void displayDetails()` — Prints the person's role, name, age, and contact number.
- Public getters and setters (`getName()`, `setAge()`, etc.) — Demonstrate **encapsulation**.

---

### File 2: `src/Guest.java` (Subclass Extending `Person`)

#### Purpose
Represents a hotel guest. Inherits `name`, `age`, and `contactNumber` from `Person` and adds a `guestId`.

#### Variables and Data Types
- `private int guestId;` — Unique integer ID assigned to each registered guest.

#### Constructors (Demonstrates Constructor Overloading)
```java
// Constructor 1: Takes all 4 parameters
public Guest(int guestId, String name, int age, String contactNumber) {
    super(name, age, contactNumber); // Calls Person constructor
    this.guestId = guestId;
}

// Constructor 2 (Overloaded): Used when age is not specified (defaults to 18)
public Guest(int guestId, String name, String contactNumber) {
    super(name, 18, contactNumber);
    this.guestId = guestId;
}
```

#### Methods (Demonstrates Method Overriding)
- `@Override public String getRole()` — Implements the abstract method from `Person` and returns `"Hotel Guest"`.
- `@Override public void displayDetails()` — Prints `Guest ID` first and then calls `super.displayDetails()` to reuse the parent class's print logic.

---

### File 3: `src/Room.java` (Hotel Room Entity)

#### Purpose
Represents one physical room in the hotel.

#### Variables and Data Types
- `private int roomNumber;` — Room number (e.g., `101`, `202`, `301`).
- `private String roomType;` — Category (`"Single"`, `"Double"`, `"Deluxe"`, `"Suite"`).
- `private double pricePerNight;` — Nightly tariff in Rupees.
- `private char floorCode;` — `'A'` for 1st floor (`100–199`), `'B'` for 2nd floor (`200–299`), `'C'` for 3rd floor (`300+`).
- `private boolean isAvailable;` — `true` if available for booking, `false` if occupied.

#### Constructors (Demonstrates Constructor Overloading)
- `public Room(int roomNumber, String roomType, double pricePerNight)` — Sets `isAvailable = true` automatically.
- `public Room(int roomNumber, String roomType, double pricePerNight, boolean isAvailable)` — Overloaded constructor allowing initial availability to be specified explicitly.

#### Key Methods
- `private char determineFloorCode(int number)` — Uses `if-else` and relational/logical operators (`number >= 100 && number < 200`) to return `'A'`, `'B'`, or `'C'`.
- `public void displayRoomInfo()` — Prints a formatted line with the room's details.
- Getters and setters (`isAvailable()`, `setAvailable(boolean)`, `getPricePerNight()`, etc.).

---

### File 4: `src/Billable.java` (Interface)

#### Purpose
Defines a standard billing contract for any class that calculates a bill.

#### Code Highlights
```java
public interface Billable {
    double GST_RATE = 0.12; // Implicitly public static final (12% GST)
    double calculateBill(); // Implicitly public abstract
}
```
- **Why use an interface here?** It demonstrates how an interface specifies a constant (`GST_RATE`) and a required method (`calculateBill()`) that `Booking` implements.

---

### File 5: `src/Booking.java` (Implements `Billable`)

#### Purpose
Links a `Guest` object and a `Room` object for a specific number of nights and computes the total bill and loyalty points.

#### Variables and Data Types
- `public static final double FIXED_SERVICE_FEE = 100.0;` — Constant service charge using the `final` keyword.
- `private int bookingId;` — Unique booking ID (starts from `1001`).
- `private Guest guest;` — Object reference to the `Guest` who made the booking.
- `private Room room;` — Object reference to the booked `Room`.
- `private int numberOfNights;` — Duration of stay.
- `private double totalCost;` — Total calculated bill amount.
- `private boolean isCheckedOut;` — `false` while staying, `true` once checked out.

#### Method Overloading Demonstration (`calculateBill`)
`Booking.java` defines **three overloaded versions** of `calculateBill`:
1. `calculateBill()` — Standard calculation: `(price * nights) + 12% GST + 100.0`.
2. `calculateBill(double discountPercent)` — Applies a percentage discount before adding GST and the service fee.
3. `calculateBill(double discountPercent, double extraFoodCharges)` — Applies the discount and adds extra food/room-service charges.

#### Explicit Type Casting Demonstration (`calculateLoyaltyPoints`)
```java
public int calculateLoyaltyPoints() {
    double exactPoints = totalCost / 100.0;
    int pointsEarned = (int) exactPoints; // Explicit narrowing cast from double to int
    return pointsEarned;
}
```

---

### File 6: `src/HotelManagementSystem.java` (Core Controller Class)

#### Purpose
Manages the collection of rooms, guests, and bookings, and implements all business logic and file persistence.

#### Why Both an Array and `ArrayList` Are Used
- `private Room[] rooms;` — A hotel has a **fixed number of physical rooms** (8 rooms), so a standard fixed-size array is appropriate and demonstrates array declaration, initialization, and traversal.
- `private ArrayList<Guest> guests;` and `private ArrayList<Booking> bookings;` — The number of guests and bookings changes dynamically as new bookings are added or cancelled, so `ArrayList` is used.

#### Key Methods and OOP Concepts
1. **`addGuest(String name, int age, String contactNumber)`**:
   - Creates a `new Guest(...)`, adds it to `guests`, and demonstrates **Runtime Polymorphism**:
     ```java
     Person personReference = newGuest;
     personReference.displayDetails(); // Calls Guest.displayDetails() at runtime
     ```
2. **`bookRoom(Guest guest, int roomNumber, int nights)`**:
   - Finds the `Room` object using `findRoomByNumber(roomNumber)`.
   - Checks availability (`!selectedRoom.isAvailable()`).
   - Marks the room as occupied (`selectedRoom.setAvailable(false)`).
   - Passes both `guest` and `selectedRoom` object references to `new Booking(...)`.
3. **`calculateTotalRevenueRecursive(int index)`**:
   - Demonstrates **Recursion** by summing the bill of each booking from `index = 0` to `bookings.size() - 1`:
     ```java
     public double calculateTotalRevenueRecursive(int index) {
         if (index >= bookings.size()) {
             return 0.0; // Base case
         }
         return bookings.get(index).getTotalCost() + calculateTotalRevenueRecursive(index + 1);
     }
     ```
4. **`saveBookingsToFile()` & `loadBookingsFromFile()`**:
   - Demonstrates **File Handling** (`FileWriter`, `BufferedWriter`, `FileReader`, `BufferedReader`) and **Exception Handling** (`try-catch-finally`).

---

### File 7: `src/Main.java` (User Interface & Input Handling)

#### Purpose
Provides the console menu loop and safe user input reading.

#### Key Highlights
- Uses a `do-while` loop and `switch-case` block for options `1` to `10`.
- Uses `readIntInput()` and `readDoubleInput()` helper methods containing `try-catch-finally` blocks to catch `NumberFormatException`. If you accidentally type letters when asked for a number, the program prints a friendly error message and asks again without crashing.

---

### File 8: `src/SyllabusConceptsDemo.java` (Standalone Syllabus Demonstrations)

#### Purpose
To keep the main hotel booking application clean and easy to explain, three specialized syllabus topics are placed in this separate, self-contained runnable file:

1. **`demonstratePassByValue()`**:
   - Shows that modifying a primitive `int` parameter inside a method does not change the caller's variable, while modifying a `Room` object's field via its copied reference updates the object's state on the heap.
2. **`demonstrateNestedAndInnerClasses()`**:
   - Compares a **`static` nested class** (`HotelTaxRules`, created without an outer object) and a **non-static inner class** (`BranchBadge`, which requires an outer instance and accesses the outer class's private `hotelBranchName` field).
3. **`demonstrateMultithreading()`**:
   - Creates two threads (`Reception-Thread` and `Housekeeping-Thread`) using the `Runnable` interface (`ReceptionTask` and `HousekeepingTask`).
   - Demonstrates `start()`, `setPriority()`, `join()`, `synchronized` methods, and inter-thread communication where `Reception-Thread` calls `wait()` until `Housekeeping-Thread` finishes cleaning the room and calls `notify()`.
