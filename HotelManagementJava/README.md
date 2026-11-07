# Hotel Management System (Java OOP Console Project)

## 1. Project Overview
This project is a beginner-friendly, menu-driven, console-based **Hotel Management System** written in standard Java. It was designed specifically for an undergraduate **Object-Oriented Programming (OOPs)** course practical examination and viva.

Every class, variable, and method is written using simple, readable Java syntax without any third-party libraries, web frameworks, or external database dependencies.

---

## 2. Features Implemented
1. **Display Rooms:** Shows all hotel rooms with their room number, floor code (`A`, `B`, `C`), room type (`Single`, `Double`, `Deluxe`, `Suite`), price per night, and availability status (`Available` / `Occupied`).
2. **Add Guest (or View Registered Guests):** Registers a guest's full name, age, and contact number, assigns a unique Guest ID, and displays their details using runtime polymorphism.
3. **Book a Room:** Allows a new or existing guest to book an available room for a specified number of nights, updates the room status to occupied, and creates a `Booking` record.
4. **View Bookings:** Displays all active and completed bookings along with a recursive calculation of the hotel's total revenue.
5. **Cancel Booking:** Cancels a booking using its Booking ID and makes the room available again.
6. **Check Out:** Marks an active booking as checked out and makes the room available for future guests.
7. **Calculate Bill:** Calculates the total bill (including 12% GST and a fixed Rs. 100 service fee) using overloaded `calculateBill()` methods that optionally apply a discount percentage and extra food/room-service charges. Also calculates integer loyalty points using explicit type casting.
8. **Search Booking by ID:** Searches for and displays a booking using its unique Booking ID.
9. **Save and Load Data (File Handling):** Saves booking data to `data/bookings.txt` and loads it back between program runs using standard `FileReader`, `BufferedReader`, `FileWriter`, and `BufferedWriter`.
10. **Exit:** Automatically saves current bookings to `data/bookings.txt` and closes the program gracefully.

---

## 3. Java Version Required
- **JDK 8 or higher** (works with JDK 8, 11, 17, 21, or newer).
- No external JARs, Maven, or Gradle setup required.

---

## 4. Project File Structure

```text
HotelManagementJava/
├── src/
│   ├── Main.java                    # Entry point, console menu loop, Scanner input & validation
│   ├── HotelManagementSystem.java   # Controller managing Room[], ArrayList<Guest>, ArrayList<Booking>, file I/O
│   ├── Person.java                  # Abstract base class for people (name, age, contactNumber)
│   ├── Guest.java                   # Subclass extending Person with guestId and overridden methods
│   ├── Room.java                    # Represents a hotel room (roomNumber, type, price, floorCode, isAvailable)
│   ├── Billable.java                # Interface defining GST_RATE and calculateBill() contract
│   ├── Booking.java                 # Implements Billable; connects a Guest and a Room; overloaded bill methods
│   └── SyllabusConceptsDemo.java    # Separate runnable demo for Pass-by-Value, Nested/Inner Classes & Multithreading
├── data/
│   └── bookings.txt                 # Comma-separated text file for saving and loading bookings
├── README.md                        # Setup, compilation, and usage guide
├── OOP_CONCEPT_MAPPING.md           # Table mapping every syllabus concept to exact files and methods
├── VIVA_PREPARATION.md              # Viva questions and beginner-friendly answers based on this code
└── CODE_EXPLANATION.md              # File-by-file and line-by-line explanation of the entire project
```

---

## 5. How to Open the Folder in VS Code
1. Open **Visual Studio Code**.
2. Click **File -> Open Folder...**
3. Select the **`HotelManagementJava`** folder.
4. Open the integrated terminal in VS Code using **Terminal -> New Terminal** (or press `` Ctrl + ` ``).

---

## 6. How to Compile and Run Using the Terminal

Make sure your terminal working directory is inside the `HotelManagementJava` folder:

```bash
cd HotelManagementJava
```

### Step 1: Compile all Java source files
```bash
javac src/*.java
```

### Step 2: Run the main Hotel Management System application
```bash
java -cp src Main
```

### Step 3: Run the separate Syllabus Concepts Demonstration (Optional)
To demonstrate **Pass-by-Value**, **Static Nested vs Non-Static Inner Classes**, and **Multithreading with Synchronization (`wait`/`notify`)**:
```bash
java -cp src SyllabusConceptsDemo
```

---

## 7. How to Use Each Menu Option
When you run `java -cp src Main`, the program loads existing bookings from `data/bookings.txt` and displays the main menu:

- **Option 1 (`Display Rooms`):** Prints all 8 rooms (Rooms 101–103 on Floor A, 201–203 on Floor B, 301–302 on Floor C) and shows how many are available vs occupied.
- **Option 2 (`Add Guest`):** Choose `1` to register a new guest (enter Name, Age, Contact Number) or `2` to view all registered guests.
- **Option 3 (`Book a Room`):** Shows rooms, asks for a Room Number and Number of Nights, lets you pick a New Guest or Existing Guest ID, and creates a booking with a unique Booking ID (starting from `1001`).
- **Option 4 (`View Bookings`):** Lists every booking and prints the total revenue across all bookings calculated using recursion.
- **Option 5 (`Cancel Booking`):** Enter a Booking ID to remove the booking and free its room.
- **Option 6 (`Check Out`):** Enter an active Booking ID to mark the guest's stay as completed and make the room available again.
- **Option 7 (`Calculate Bill`):** Enter a Booking ID, a Discount Percentage (e.g., `10` for 10% or `0` for none), and Extra Food Charges (e.g., `500` or `0`). Demonstrates compile-time method overloading.
- **Option 8 (`Search Booking by ID`):** Enter a Booking ID (e.g., `1001`) to look up and display that specific booking.
- **Option 9 (`Save or Load Bookings`):** Choose `1` to save bookings to `data/bookings.txt` or `2` to reload bookings from `data/bookings.txt`.
- **Option 10 (`Exit`):** Saves all current bookings to `data/bookings.txt` and exits the program.

---

## 8. How the Data File (`data/bookings.txt`) Works
- Bookings are stored as simple comma-separated lines in `data/bookings.txt`.
- Format of each line:
  ```text
  bookingId,guestId,guestName,guestAge,guestContact,roomNumber,nights,totalCost,isCheckedOut
  ```
- Example line:
  ```text
  1001,1,Aarav Sharma,28,9876543210,101,2,3460.0,false
  ```
- When the program starts (or when you choose Menu Option 9 -> Load), `HotelManagementSystem.loadBookingsFromFile()` reads each line using `BufferedReader`, recreates the `Guest` and `Booking` objects, and marks active rooms (`isCheckedOut == false`) as occupied.
