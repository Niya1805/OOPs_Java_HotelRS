# Viva Preparation Guide (Questions & Answers)

This guide contains the most common practical examination and viva questions your teacher may ask, with short, accurate answers tied directly to your **Hotel Management System** code.

---

## 1. Basic Java Questions

**Q1. Why did you use `int`, `double`, `char`, and `boolean` in `Room.java`?**
- **Answer:** Each primitive data type matches a specific kind of room attribute:
  - `int roomNumber` for whole-number room numbers (`101`, `202`).
  - `double pricePerNight` for decimal currency values (`1500.0`).
  - `char floorCode` for a single floor letter (`'A'`, `'B'`, `'C'`).
  - `boolean isAvailable` for a true/false room vacancy status.

**Q2. Where did you use type conversion and type casting in your project?**
- **Answer:** In `Booking.java`:
  - **Implicit Type Conversion (Widening):** In `room.getPricePerNight() * numberOfNights`, Java automatically converts the `int` (`numberOfNights`) to a `double` before multiplying.
  - **Explicit Type Casting (Narrowing):** In `calculateLoyaltyPoints()`, `double exactPoints = totalCost / 100.0;` is explicitly cast using `(int) exactPoints` so loyalty points are returned as a whole integer.

**Q3. Why did you use a `do-while` loop in `Main.java` instead of a `while` or `for` loop?**
- **Answer:** A `do-while` loop executes its body at least once before checking the condition (`while (choice != 10)`). Since the hotel menu must always be displayed to the user at least once when the program starts, `do-while` is the most natural loop.

---

## 2. Class and Object Questions

**Q4. What is the difference between a Class and an Object in your project?**
- **Answer:** A **class** is a logical blueprint or template (such as `Room.java`), which defines what attributes and methods a room has. An **object** is a real runtime instance created in heap memory using the `new` keyword—for example, `new Room(101, "Single", 1500.0)` represents physical Room 101.

**Q5. How do objects interact in your `Booking` class?**
- **Answer:** `Booking` uses object composition (object references). Inside `Booking.java`, there are private fields `private Guest guest;` and `private Room room;`. A `Booking` object links a specific `Guest` object to a specific `Room` object.

---

## 3. Constructor and Method Questions

**Q6. What is a constructor, and where did you use Constructor Overloading?**
- **Answer:** A constructor is a special method with the same name as the class and no return type, used to initialize a new object's state.
- I used **Constructor Overloading** in:
  1. `Room.java`: One constructor takes 3 parameters (`roomNumber, roomType, pricePerNight`) and sets `isAvailable = true` by default; the second takes 4 parameters (`..., boolean isAvailable`).
  2. `Guest.java`: One constructor takes 4 parameters (`guestId, name, age, contactNumber`), and the overloaded constructor takes 3 parameters (omitting `age` and defaulting it to `18`).

**Q7. How does Java decide which overloaded constructor to call?**
- **Answer:** Java resolves constructor overloading at **compile time** by matching the number, types, and order of arguments passed inside `new Room(...)` or `new Guest(...)`.

**Q8. Does Java pass objects by reference or by value?**
- **Answer:** Java is **strictly pass-by-value** for everything:
  - When a primitive (`int`) is passed to a method, a copy of the value is passed.
  - When an object (`Room` or `Guest`) is passed to a method (such as `new Booking(bookingId, guest, selectedRoom, nights)`), Java passes a **copy of the object's reference (memory address) by value**. Because both references point to the same object on the heap, calling `selectedRoom.setAvailable(false)` updates the actual room object, but reassigning the parameter variable itself inside a method would not affect the caller's reference. (Demonstrated in `SyllabusConceptsDemo.demonstratePassByValue()`).

---

## 4. Encapsulation and Access Modifier Questions

**Q9. What is Encapsulation, and how is it implemented in `Room.java`?**
- **Answer:** Encapsulation is wrapping data (variables) and code (methods) together into a single class while hiding internal fields from direct unauthorized access. In `Room.java`, all fields (`roomNumber`, `pricePerNight`, `isAvailable`, etc.) are declared `private`, and outside classes can only read or update them through `public` getter and setter methods like `getPricePerNight()` and `setAvailable()`.

**Q10. Why did you use `protected` in `Person.java` instead of `private`?**
- **Answer:** `name`, `age`, and `contactNumber` are declared `protected` in `Person.java` so that its child class `Guest` can directly access those inherited fields while still keeping them protected from unrelated external classes outside the package.

---

## 5. Inheritance and Polymorphism Questions

**Q11. Why does `Guest` extend `Person`?**
- **Answer:** A `Guest` **IS-A** `Person`. By writing `public class Guest extends Person`, `Guest` automatically inherits `name`, `age`, `contactNumber`, and their getters/setters from `Person`, avoiding code duplication and adding `guestId`.

**Q12. What is the purpose of the `super` keyword in `Guest.java`?**
- **Answer:** `super` is used in two ways in `Guest.java`:
  1. `super(name, age, contactNumber);` in the `Guest` constructor calls the parent `Person` constructor to initialize inherited fields.
  2. `super.displayDetails();` inside `Guest.displayDetails()` calls the parent class method so we don't have to rewrite the print statements for name, age, and contact number.

**Q13. How is Runtime Polymorphism (Dynamic Method Dispatch) demonstrated in your project?**
- **Answer:** In `HotelManagementSystem.addGuest()`, I wrote:
  ```java
  Person personReference = newGuest;
  personReference.displayDetails();
  ```
  Here, `personReference` has the compile-time type `Person`, but at runtime it points to a `Guest` object. Because `Guest` overrides `displayDetails()`, the JVM dynamically dispatches the call to `Guest.displayDetails()` at runtime.

---

## 6. Abstract Class vs Interface

**Q14. What is the difference between your abstract class `Person` and your interface `Billable`?**
- **Answer:**
  - **`Person` (Abstract Class):** Represents an **IS-A** identity hierarchy (`Guest` IS-A `Person`). It can have instance variables (`name`, `age`), a constructor, concrete methods (`displayDetails()`), and abstract methods (`getRole()`). You cannot write `new Person(...)`.
  - **`Billable` (Interface):** Represents a **CAN-DO** capability contract (`Booking` is billable). Its variables (`GST_RATE = 0.12`) are implicitly `public static final` constants, and its methods (`calculateBill()`) define what a class must do without storing instance state. A class can only `extend` one class, but it can `implement` multiple interfaces.

---

## 7. Method Overloading vs Method Overriding

**Q15. Explain the difference between Method Overloading and Method Overriding using examples from your code.**
- **Answer:**
  - **Method Overloading (Compile-Time Polymorphism):** Same method name with **different parameter lists** within the **same class**. Example in `Booking.java`:
    1. `calculateBill()` (no parameters)
    2. `calculateBill(double discountPercent)` (1 parameter)
    3. `calculateBill(double discountPercent, double extraFoodCharges)` (2 parameters)
  - **Method Overriding (Runtime Polymorphism):** Same method name, **same parameter list**, and same return type in a **parent-child class relationship**. Example: `Guest` overrides `displayDetails()` and `getRole()` from `Person`.

---

## 8. Arrays and Object Collections

**Q16. Why did you use a fixed-size array `Room[]` for rooms, but an `ArrayList<Booking>` for bookings?**
- **Answer:**
  - A hotel building has a fixed number of physical rooms (8 rooms in our hotel), so a fixed-size array `Room[] rooms = new Room[8]` is simple and memory-efficient.
  - Bookings and guests are created and cancelled dynamically while the program runs. Since a standard array cannot change its size after creation, I used `ArrayList<Booking>` and `ArrayList<Guest>`, which resize automatically as elements are added or removed.

---

## 9. Exception Handling and File Handling

**Q17. What happens if a user types `"abc"` when the menu asks for a room number or menu choice?**
- **Answer:** In `Main.readIntInput()`, `Integer.parseInt(rawInput)` is wrapped inside a `try-catch` block that catches `NumberFormatException`. Instead of crashing with a stack trace, the `catch` block prints `[INVALID INPUT]` and prompts the user to enter a valid number again.

**Q18. How does the program save and load bookings from the text file?**
- **Answer:**
  - **Saving (`saveBookingsToFile()`):** Uses `FileWriter` wrapped in `BufferedWriter` to write each booking's `toFileString()` (comma-separated values) line by line into `data/bookings.txt`.
  - **Loading (`loadBookingsFromFile()`):** Uses `FileReader` wrapped in `BufferedReader` inside a `while ((line = reader.readLine()) != null)` loop, splits each line by `,`, parses the numbers using `Integer.parseInt()` / `Double.parseDouble()`, recreates the `Guest` and `Booking` objects, and marks active rooms as occupied.
  - Both methods use `try-catch-finally` so that the file stream is always closed inside the `finally` block even if an `IOException` occurs.

---

## 10. Recursion and `final` Keyword

**Q19. Where did you use Recursion in your project, and what is its base case?**
- **Answer:** In `HotelManagementSystem.calculateTotalRevenueRecursive(int index)`:
  - **Purpose:** Calculates the sum of `totalCost` across all bookings in the `bookings` list.
  - **Base Case:** `if (index >= bookings.size()) return 0.0;` — stops the recursive calls when the index reaches the end of the list.
  - **Recursive Case:** `return bookings.get(index).getTotalCost() + calculateTotalRevenueRecursive(index + 1);` — adds the current booking's bill to the recursive sum of the remaining bookings.

**Q20. What does the `final` keyword do in your code?**
- **Answer:** In `Booking.FIXED_SERVICE_FEE = 100.0` and `HotelManagementSystem.HOTEL_NAME = "Sunrise Grand Hotel"`, `final` makes the variable a constant whose value cannot be changed once assigned. (If `final` is applied to a method, it cannot be overridden; if applied to a class, it cannot be extended.)

---

## 11. Nested Classes & Multithreading (Separate Demo Class)

**Q21. What is the difference between the Static Nested Class and Non-Static Inner Class in `SyllabusConceptsDemo.java`?**
- **Answer:**
  - `HotelTaxRules` is a **`static` nested class**: it can be instantiated without creating an outer `SyllabusConceptsDemo` object (`new SyllabusConceptsDemo.HotelTaxRules()`), and it cannot access non-static outer fields.
  - `BranchBadge` is a **non-static inner class**: it requires an instance of the outer class (`outerObject.new BranchBadge(...)`) and can directly access the outer object's private field `hotelBranchName`.

**Q22. Explain how `Runnable`, `start()`, `join()`, `synchronized`, `wait()`, and `notify()` work in `SyllabusConceptsDemo.java`.**
- **Answer:**
  - `ReceptionTask` and `HousekeepingTask` implement the `Runnable` interface and define their work inside `run()`.
  - `start()` creates a new thread of execution and invokes `run()` asynchronously.
  - `join()` makes the `main` thread wait until both threads complete before printing the final summary.
  - `synchronized` ensures only one thread at a time can execute methods on the shared `SharedRoomCleaningMonitor` object (preventing race conditions).
  - `wait()` pauses the `Reception-Thread` and releases the monitor lock while `!isRoomCleaned`. Once `Housekeeping-Thread` finishes cleaning the room, it sets `isRoomCleaned = true` and calls `notify()` to wake up the waiting `Reception-Thread`.

---

## 12. Code Walkthrough Questions ("Explain This Line")

**Q23. Explain how `calculateBill(double discountPercent)` works in `Booking.java`.**
- **Answer:**
  1. `double baseRoomCharge = room.getPricePerNight() * numberOfNights;` multiplies the room's nightly rate by the number of nights stayed.
  2. `double discountAmount = baseRoomCharge * (discountPercent / 100.0);` computes the discount in rupees.
  3. `double discountedBase = baseRoomCharge - discountAmount;` subtracts the discount.
  4. `double taxAmount = discountedBase * GST_RATE;` adds 12% GST from the `Billable` interface.
  5. `return discountedBase + taxAmount + FIXED_SERVICE_FEE;` returns the final bill including the Rs. 100 fixed service fee.

**Q24. Explain what happens step-by-step when a booking is cancelled (`cancelBooking(int bookingId)`).**
- **Answer:**
  1. We loop through `bookings` using a `for` loop.
  2. We check `if (b.getBookingId() == bookingId)`.
  3. If found and the guest has not already checked out (`!b.isCheckedOut()`), we call `b.getRoom().setAvailable(true)` so the room becomes vacant again.
  4. We remove the booking from the `ArrayList` using `bookings.remove(i)` and return `true`.
