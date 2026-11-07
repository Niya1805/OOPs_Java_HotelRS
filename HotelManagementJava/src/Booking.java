/**
 * Booking.java
 *
 * PURPOSE:
 * Connects a `Guest` with a `Room` for a given number of nights and calculates the bill.
 * Implements the `Billable` interface.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. Interface Implementation (`implements Billable`)
 * 2. Object References / Composition (a `Booking` has a `Guest` object and a `Room` object)
 * 3. `final` Keyword (`FIXED_SERVICE_FEE` constant)
 * 4. Method Overloading (`calculateBill()` with 0, 1, and 2 parameters)
 * 5. Type Conversion and Explicit Type Casting (`double` to `int` for loyalty points)
 * 6. Encapsulation (`private` fields with `public` getters/setters)
 */
public class Booking implements Billable {

    // `final` constant: fixed hotel service fee added to every booking
    public static final double FIXED_SERVICE_FEE = 100.0;

    // Private instance variables
    private int bookingId;
    private Guest guest;          // Object reference to Guest
    private Room room;            // Object reference to Room
    private int numberOfNights;
    private double totalCost;
    private boolean isCheckedOut; // false while guest is staying, true after checkout

    /**
     * Constructor to create a new booking.
     * Demonstrates passing objects (`Guest` and `Room`) as parameters.
     */
    public Booking(int bookingId, Guest guest, Room room, int numberOfNights) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.numberOfNights = numberOfNights;
        this.isCheckedOut = false;
        // Calculate initial bill using standard calculation
        this.totalCost = calculateBill();
    }

    /**
     * Overloaded Constructor used when loading saved bookings from the text file.
     */
    public Booking(int bookingId, Guest guest, Room room, int numberOfNights, double totalCost, boolean isCheckedOut) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.numberOfNights = numberOfNights;
        this.totalCost = totalCost;
        this.isCheckedOut = isCheckedOut;
    }

    // =========================================================================
    // METHOD OVERLOADING DEMONSTRATION: Three versions of `calculateBill`
    // =========================================================================

    /**
     * Version 1 (No parameters - implements Billable interface):
     * Calculates standard bill = (Room Price * Nights) + 12% GST + Fixed Service Fee.
     */
    @Override
    public double calculateBill() {
        double baseRoomCharge = room.getPricePerNight() * numberOfNights; // Implicit int-to-double conversion
        double taxAmount = baseRoomCharge * GST_RATE;
        return baseRoomCharge + taxAmount + FIXED_SERVICE_FEE;
    }

    /**
     * Version 2 (1 parameter - Overloaded Method):
     * Calculates bill after applying a percentage discount on the base room charge.
     */
    public double calculateBill(double discountPercent) {
        double baseRoomCharge = room.getPricePerNight() * numberOfNights;
        double discountAmount = baseRoomCharge * (discountPercent / 100.0);
        double discountedBase = baseRoomCharge - discountAmount;
        double taxAmount = discountedBase * GST_RATE;
        return discountedBase + taxAmount + FIXED_SERVICE_FEE;
    }

    /**
     * Version 3 (2 parameters - Overloaded Method):
     * Calculates bill with both a percentage discount AND additional food/room-service charges.
     */
    public double calculateBill(double discountPercent, double extraFoodCharges) {
        double discountedTotal = calculateBill(discountPercent);
        return discountedTotal + extraFoodCharges;
    }

    /**
     * Demonstrates EXPLICIT TYPE CASTING (`double` to `int`):
     * Calculates integer loyalty points earned by the guest (1 point per Rs. 100 spent).
     */
    public int calculateLoyaltyPoints() {
        double exactPoints = totalCost / 100.0;
        // Explicit narrowing type cast from double to int (truncates decimal part)
        int pointsEarned = (int) exactPoints;
        return pointsEarned;
    }

    /**
     * Displays complete booking details.
     */
    public void displayBookingDetails() {
        String bookingStatus = isCheckedOut ? "Checked Out (Completed)" : "Active (Checked In)";
        System.out.println("------------------------------------------------------------");
        System.out.println("Booking ID      : " + bookingId);
        System.out.println("Booking Status  : " + bookingStatus);
        System.out.println("Guest ID & Name : " + guest.getGuestId() + " - " + guest.getName()
                + " (Age: " + guest.getAge() + ", Contact: " + guest.getContactNumber() + ")");
        System.out.println("Room Details    : Room " + room.getRoomNumber()
                + " (" + room.getRoomType() + ", Floor " + room.getFloorCode() + ")");
        System.out.println("Price Per Night : Rs. " + String.format("%.2f", room.getPricePerNight()));
        System.out.println("Nights Stayed   : " + numberOfNights);
        System.out.println("Total Bill      : Rs. " + String.format("%.2f", totalCost)
                + " (Includes 12% GST & Rs. " + FIXED_SERVICE_FEE + " Service Fee)");
        System.out.println("Loyalty Points  : " + calculateLoyaltyPoints() + " pts");
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Converts booking data into a comma-separated line for saving to a text file.
     * Format: bookingId,guestId,guestName,guestAge,guestContact,roomNumber,nights,totalCost,isCheckedOut
     */
    public String toFileString() {
        // Replace any commas in guest name or contact to keep CSV format safe
        String safeName = guest.getName().replace(",", " ");
        String safeContact = guest.getContactNumber().replace(",", " ");
        return bookingId + ","
                + guest.getGuestId() + ","
                + safeName + ","
                + guest.getAge() + ","
                + safeContact + ","
                + room.getRoomNumber() + ","
                + numberOfNights + ","
                + totalCost + ","
                + isCheckedOut;
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.isCheckedOut = checkedOut;
    }
}
