import java.util.Scanner;

/**
 * Main.java
 *
 * PURPOSE:
 * Entry point of the console-based Hotel Management System.
 * Displays a numbered menu, reads user input using `Scanner`, validates input
 * with `try-catch` blocks, and calls methods on `HotelManagementSystem`.
 *
 * OOP & PROGRAMMING CONCEPTS DEMONSTRATED:
 * 1. `main()` method and creating the controller object (`HotelManagementSystem`)
 * 2. `do-while` loop for repeated menu execution
 * 3. `switch-case` statement for menu navigation
 * 4. `if-else` conditional validation
 * 5. Exception Handling (`try-catch-finally`) for invalid numeric input (`NumberFormatException`)
 */
public class Main { 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HotelManagementSystem hotel = new HotelManagementSystem();

        System.out.println("============================================================");
        System.out.println("   WELCOME TO " + HotelManagementSystem.HOTEL_NAME.toUpperCase());
        System.out.println("   Object-Oriented Programming (Java) Console Project");
        System.out.println("============================================================");

        int choice = 0;

        // `do-while` loop guarantees the menu is shown at least once and repeats until choice == 10
        do {
            printMenu();
            choice = readIntInput(scanner, "Enter your choice (1-10): ");

            // `switch` statement routes the user's menu choice to the matching feature
            switch (choice) {
                case 1:
                    // 1. Display Rooms
                    hotel.displayAllRooms();
                    break;

                case 2:
                    // 2. Add Guest
                    handleAddGuest(scanner, hotel);
                    break;

                case 3:
                    // 3. Book a Room
                    handleBookRoom(scanner, hotel);
                    break;

                case 4:
                    // 4. View Bookings
                    hotel.viewAllBookings();
                    break;

                case 5:
                    // 5. Cancel Booking
                    int cancelId = readIntInput(scanner, "Enter Booking ID to cancel: ");
                    hotel.cancelBooking(cancelId);
                    break;

                case 6:
                    // 6. Check Out
                    int checkOutId = readIntInput(scanner, "Enter Booking ID for Check-Out: ");
                    hotel.checkOut(checkOutId);
                    break;

                case 7:
                    // 7. Calculate Bill (Demonstrates Method Overloading)
                    handleCalculateBill(scanner, hotel);
                    break;

                case 8:
                    // 8. Search Booking by ID
                    handleSearchBooking(scanner, hotel);
                    break;

                case 9:
                    // 9. Save and Load Data
                    handleSaveAndLoadMenu(scanner, hotel);
                    break;

                case 10:
                    // 10. Exit
                    System.out.println("\nSaving current bookings before exit...");
                    hotel.saveBookingsToFile();
                    System.out.println("Thank you for using " + HotelManagementSystem.HOTEL_NAME + "! Goodbye.");
                    break;

                default:
                    System.out.println("\n[WARNING] Invalid menu choice! Please enter a number between 1 and 10.");
                    break;
            }

        } while (choice != 10);

        scanner.close();
    }

    /**
     * Prints the main numbered console menu.
     */
    private static void printMenu() {
        System.out.println("\n-------------------- MAIN MENU --------------------");
        System.out.println("1. Display Rooms");
        System.out.println("2. Add Guest (or View Registered Guests)");
        System.out.println("3. Book a Room");
        System.out.println("4. View Bookings (with Recursive Revenue Total)");
        System.out.println("5. Cancel Booking");
        System.out.println("6. Check Out");
        System.out.println("7. Calculate Bill (with Discount / Extra Charges)");
        System.out.println("8. Search Booking by ID");
        System.out.println("9. Save or Load Bookings (File Handling)");
        System.out.println("10. Exit");
        System.out.println("---------------------------------------------------");
    }

    /**
     * Handles Option 2: Registering a new guest or viewing existing guests.
     */
    private static void handleAddGuest(Scanner scanner, HotelManagementSystem hotel) {
        System.out.println("\n--- Guest Management ---");
        System.out.println("1. Register a New Guest");
        System.out.println("2. View All Registered Guests");
        int subChoice = readIntInput(scanner, "Choose option (1 or 2): ");

        if (subChoice == 2) {
            hotel.displayAllGuests();
            return;
        }

        String name = readNonEmptyString(scanner, "Enter Guest Full Name: ");
        int age = readIntInput(scanner, "Enter Guest Age: ");
        if (age <= 0 || age > 120) {
            System.out.println("[WARNING] Invalid age entered. Setting default adult age (18).");
            age = 18;
        }
        String contact = readNonEmptyString(scanner, "Enter 10-digit Contact Number: ");
        hotel.addGuest(name, age, contact);
    }

    /**
     * Handles Option 3: Booking an available room for a new or existing guest.
     */
    private static void handleBookRoom(Scanner scanner, HotelManagementSystem hotel) {
        hotel.displayAllRooms();
        int roomNumber = readIntInput(scanner, "\nEnter Room Number to book: ");

        Room chosenRoom = hotel.findRoomByNumber(roomNumber);
        if (chosenRoom == null) {
            System.out.println("[ERROR] Room " + roomNumber + " does not exist.");
            return;
        }
        if (!chosenRoom.isAvailable()) {
            System.out.println("[ERROR] Room " + roomNumber + " is already occupied! Please choose an available room.");
            return;
        }

        int nights = readIntInput(scanner, "Enter Number of Nights to stay: ");
        if (nights <= 0) {
            System.out.println("[ERROR] Number of nights must be at least 1.");
            return;
        }

        System.out.println("\nDo you want to book for:");
        System.out.println("1. A New Guest");
        System.out.println("2. An Existing Registered Guest");
        int guestOption = readIntInput(scanner, "Enter option (1 or 2): ");

        Guest guestForBooking = null;
        if (guestOption == 2) {
            hotel.displayAllGuests();
            int guestId = readIntInput(scanner, "Enter Existing Guest ID: ");
            guestForBooking = hotel.findGuestById(guestId);
            if (guestForBooking == null) {
                System.out.println("[ERROR] Guest ID " + guestId + " not found.");
                return;
            }
        } else {
            String name = readNonEmptyString(scanner, "Enter Guest Full Name: ");
            int age = readIntInput(scanner, "Enter Guest Age: ");
            if (age <= 0 || age > 120) {
                age = 18;
            }
            String contact = readNonEmptyString(scanner, "Enter Contact Number: ");
            guestForBooking = hotel.addGuest(name, age, contact);
        }

        hotel.bookRoom(guestForBooking, roomNumber, nights);
    }

    /**
     * Handles Option 7: Calculating the bill using overloaded `calculateBill` methods.
     */
    private static void handleCalculateBill(Scanner scanner, HotelManagementSystem hotel) {
        int bookingId = readIntInput(scanner, "Enter Booking ID to calculate bill: ");
        Booking booking = hotel.findBookingById(bookingId);
        if (booking == null) {
            System.out.println("\n[ERROR] Booking ID " + bookingId + " not found.");
            return;
        }

        double discount = readDoubleInput(scanner, "Enter Discount Percentage (0 for no discount): ");
        if (discount < 0 || discount > 100) {
            System.out.println("[WARNING] Invalid discount. Resetting discount to 0%.");
            discount = 0.0;
        }

        double extraCharges = readDoubleInput(scanner, "Enter Extra Food/Room-Service Charges in Rs. (0 for none): ");
        if (extraCharges < 0) {
            System.out.println("[WARNING] Negative charges not allowed. Resetting to Rs. 0.");
            extraCharges = 0.0;
        }

        hotel.calculateAndDisplayBill(bookingId, discount, extraCharges);
    }

    /**
     * Handles Option 8: Searching for a specific booking by its ID.
     */
    private static void handleSearchBooking(Scanner scanner, HotelManagementSystem hotel) {
        int bookingId = readIntInput(scanner, "Enter Booking ID to search: ");
        Booking foundBooking = hotel.findBookingById(bookingId);
        if (foundBooking != null) {
            System.out.println("\n[FOUND] Booking Details:");
            foundBooking.displayBookingDetails();
        } else {
            System.out.println("\n[NOT FOUND] No booking exists with ID: " + bookingId);
        }
    }

    /**
     * Handles Option 9: Saving bookings to file or loading bookings from file.
     */
    private static void handleSaveAndLoadMenu(Scanner scanner, HotelManagementSystem hotel) {
        System.out.println("\n--- File Persistence Menu ---");
        System.out.println("1. Save Current Bookings to File (" + HotelManagementSystem.DATA_FILE_PATH + ")");
        System.out.println("2. Load Bookings from File (" + HotelManagementSystem.DATA_FILE_PATH + ")");
        int subChoice = readIntInput(scanner, "Enter choice (1 or 2): ");

        if (subChoice == 1) {
            hotel.saveBookingsToFile();
        } else if (subChoice == 2) {
            hotel.loadBookingsFromFile();
        } else {
            System.out.println("[WARNING] Invalid option selected.");
        }
    }

    // =========================================================================
    // INPUT VALIDATION HELPERS WITH EXCEPTION HANDLING (`try-catch-finally`)
    // =========================================================================

    /**
     * Safely reads an integer from the user.
     * Uses `try-catch-finally` to handle `NumberFormatException` if the user types letters.
     */
    private static int readIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String rawInput = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(rawInput);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[INVALID INPUT] '" + rawInput + "' is not a valid whole number. Please try again.");
            } finally {
                // `finally` block runs after `try` or `catch` (demonstrated here as part of exception handling)
            }
        }
    }

    /**
     * Safely reads a decimal (`double`) number from the user.
     */
    private static double readDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String rawInput = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(rawInput);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[INVALID INPUT] '" + rawInput + "' is not a valid number. Please try again.");
            }
        }
    }

    /**
     * Ensures the user does not enter a blank string for names or contact numbers.
     */
    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[INVALID INPUT] This field cannot be empty. Please try again.");
        }
    }
}
