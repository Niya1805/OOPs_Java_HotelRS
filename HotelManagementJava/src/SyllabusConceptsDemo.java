/**
 * SyllabusConceptsDemo.java
 *
 * PURPOSE:
 * A standalone, beginner-friendly demonstration file for syllabus topics that are
 * best kept separate from the main console booking workflow so the primary application
 * stays simple and reliable:
 *   1. Java Pass-by-Value (Primitive value vs Object reference copy)
 *   2. Static Nested Class vs Non-Static Inner Class
 *   3. Multithreading (`Runnable`, `Thread`, `start()`, `join()`, Thread Priority)
 *   4. Thread Synchronization (`synchronized` method) and Inter-Thread Communication (`wait()` & `notify()`)
 *
 * HOW TO RUN THIS DEMO SEPARATELY:
 *   javac -d bin src/*.java
 *   java -cp bin SyllabusConceptsDemo
 */
public class SyllabusConceptsDemo {

    // =========================================================================
    // 1. PASS-BY-VALUE DEMONSTRATION (Primitives vs Object References)
    // =========================================================================

    /**
     * Java is ALWAYS Pass-by-Value.
     * - When passing a primitive (`int`), a copy of the number is passed. Modifying the parameter
     *   inside the method does NOT change the caller's variable.
     * - When passing an object (`Room`), a copy of the reference (memory address) is passed by value.
     *   Using that reference to modify the object's fields DOES update the same object in memory,
     *   but reassigning the parameter to a `new Room(...)` only changes the local copy of the reference.
     */
    public static void demonstratePassByValue() {
        System.out.println("\n=== 1. JAVA PASS-BY-VALUE DEMONSTRATION ===");

        int nights = 3;
        Room demoRoom = new Room(105, "Single", 1500.0);

        System.out.println("Before method call -> nights = " + nights
                + ", demoRoom price = Rs. " + demoRoom.getPricePerNight());

        modifyValues(nights, demoRoom);

        System.out.println("After method call  -> nights = " + nights + " (primitive unchanged!)");
        System.out.println("After method call  -> demoRoom price = Rs. " + demoRoom.getPricePerNight()
                + " (object state updated through reference copy!)");
    }

    private static void modifyValues(int nightsCopy, Room roomRefCopy) {
        // Modifying the copy of the primitive variable does NOT affect `nights` in the caller
        nightsCopy = 10;

        // Modifying the object's internal state through the copied reference DOES affect the original object
        roomRefCopy.setPricePerNight(2000.0);

        // Reassigning the local reference variable to a brand-new Room does NOT affect the caller's variable
        roomRefCopy = new Room(999, "Suite", 9000.0);
    }

    // =========================================================================
    // 2. STATIC NESTED CLASS VS NON-STATIC INNER CLASS DEMONSTRATION
    // =========================================================================

    private String hotelBranchName = "Sunrise Grand - Mumbai Branch";

    /**
     * STATIC NESTED CLASS:
     * Declared with `static`. Does NOT need an instance of `SyllabusConceptsDemo` to be created.
     * Cannot access non-static field `hotelBranchName` directly.
     */
    public static class HotelTaxRules {
        public void showTaxInfo() {
            System.out.println("[Static Nested Class] Standard Hotel GST Rate is: " + (Billable.GST_RATE * 100) + "%");
        }
    }

    /**
     * NON-STATIC INNER CLASS:
     * Declared without `static`. Belongs to a specific instance of `SyllabusConceptsDemo`
     * and CAN directly access outer class instance variables like `hotelBranchName`.
     */
    public class BranchBadge {
        private String staffName;

        public BranchBadge(String staffName) {
            this.staffName = staffName;
        }

        public void printBadge() {
            // Directly accesses `hotelBranchName` from the outer class object
            System.out.println("[Non-Static Inner Class] Staff: " + staffName + " | Branch: " + hotelBranchName);
        }
    }

    public static void demonstrateNestedAndInnerClasses() {
        System.out.println("\n=== 2. NESTED vs INNER CLASS DEMONSTRATION ===");

        // Creating a Static Nested Class object (no outer object required)
        SyllabusConceptsDemo.HotelTaxRules taxRules = new SyllabusConceptsDemo.HotelTaxRules();
        taxRules.showTaxInfo();

        // Creating a Non-Static Inner Class object (requires an outer class object first)
        SyllabusConceptsDemo outerObject = new SyllabusConceptsDemo();
        SyllabusConceptsDemo.BranchBadge badge = outerObject.new BranchBadge("Rohan Verma");
        badge.printBadge();
    }

    // =========================================================================
    // 3. MULTITHREADING, SYNCHRONIZATION, AND WAIT/NOTIFY DEMONSTRATION
    // =========================================================================

    /**
     * Shared resource representing a room preparation workflow between two threads:
     * 1. `HousekeepingTask` cleans the room and calls `notify()`.
     * 2. `ReceptionCheckInTask` waits (`wait()`) until the room is cleaned before checking in the guest.
     */
    static class SharedRoomCleaningMonitor {
        private boolean isRoomCleaned = false;
        private int totalProcessedCount = 0;

        // `synchronized` method ensures only one thread can execute this at a time
        public synchronized void incrementCounter() {
            totalProcessedCount++;
        }

        public synchronized int getTotalProcessedCount() {
            return totalProcessedCount;
        }

        // Called by Reception thread: waits until housekeeping finishes cleaning
        public synchronized void waitForRoomCleaning(int roomNumber) {
            System.out.println("[Reception Thread] Checking if Room " + roomNumber + " is clean...");
            while (!isRoomCleaned) {
                try {
                    System.out.println("[Reception Thread] Room " + roomNumber + " is not ready yet. Waiting (wait())...");
                    wait(); // Releases the lock and waits until notify() is called
                } catch (InterruptedException e) {
                    System.out.println("[Reception Thread] Interrupted while waiting.");
                }
            }
            incrementCounter();
            System.out.println("[Reception Thread] Room " + roomNumber + " is clean! Guest check-in completed.");
        }

        // Called by Housekeeping thread: cleans the room and notifies waiting thread
        public synchronized void cleanRoom(int roomNumber) {
            System.out.println("[Housekeeping Thread] Cleaning Room " + roomNumber + "...");
            try {
                Thread.sleep(300); // Simulate short cleaning work (300 milliseconds)
            } catch (InterruptedException e) {
                System.out.println("[Housekeeping Thread] Cleaning interrupted.");
            }
            isRoomCleaned = true;
            incrementCounter();
            System.out.println("[Housekeeping Thread] Room " + roomNumber + " cleaning finished! Calling notify()...");
            notify(); // Wakes up the waiting Reception thread
        }
    }

    /**
     * Task implementing `Runnable` for the Reception thread.
     */
    static class ReceptionTask implements Runnable {
        private SharedRoomCleaningMonitor monitor;
        private int roomNumber;

        public ReceptionTask(SharedRoomCleaningMonitor monitor, int roomNumber) {
            this.monitor = monitor;
            this.roomNumber = roomNumber;
        }

        @Override
        public void run() {
            monitor.waitForRoomCleaning(roomNumber);
        }
    }

    /**
     * Task implementing `Runnable` for the Housekeeping thread.
     */
    static class HousekeepingTask implements Runnable {
        private SharedRoomCleaningMonitor monitor;
        private int roomNumber;

        public HousekeepingTask(SharedRoomCleaningMonitor monitor, int roomNumber) {
            this.monitor = monitor;
            this.roomNumber = roomNumber;
        }

        @Override
        public void run() {
            monitor.cleanRoom(roomNumber);
        }
    }

    public static void demonstrateMultithreading() {
        System.out.println("\n=== 3. MULTITHREADING & SYNCHRONIZATION DEMONSTRATION ===");

        SharedRoomCleaningMonitor sharedMonitor = new SharedRoomCleaningMonitor();

        // Create Thread objects by passing Runnable tasks
        Thread receptionThread = new Thread(new ReceptionTask(sharedMonitor, 202), "Reception-Thread");
        Thread housekeepingThread = new Thread(new HousekeepingTask(sharedMonitor, 202), "Housekeeping-Thread");

        // Demonstrate setting thread priorities
        receptionThread.setPriority(Thread.NORM_PRIORITY);
        housekeepingThread.setPriority(Thread.MAX_PRIORITY);

        // Start both threads using start() (which invokes run() in a separate thread of execution)
        receptionThread.start();
        housekeepingThread.start();

        // Use join() so the main thread waits for both worker threads to finish
        try {
            receptionThread.join();
            housekeepingThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting for worker threads.");
        }

        System.out.println("Both threads finished! Synchronized step counter = "
                + sharedMonitor.getTotalProcessedCount());
    }

    /**
     * Main method to run all three standalone syllabus demonstrations.
     */
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   ADDITIONAL SYLLABUS CONCEPTS DEMONSTRATION");
        System.out.println("============================================================");

        demonstratePassByValue();
        demonstrateNestedAndInnerClasses();
        demonstrateMultithreading();

        System.out.println("\n============================================================");
        System.out.println("   ALL DEMONSTRATIONS COMPLETED SUCCESSFULLY");
        System.out.println("============================================================");
    }
}
