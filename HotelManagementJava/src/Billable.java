/**
 * Billable.java
 *
 * PURPOSE:
 * An interface that defines a contract for any hotel service or booking that can generate a bill.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. Interface (`public interface Billable`)
 * 2. Interface Constants (implicitly `public static final`)
 * 3. Abstract Method Contract (implicitly `public abstract`)
 */
public interface Billable {

    // Constant in an interface is implicitly `public static final`
    double GST_RATE = 0.12; // 12% GST (Goods and Services Tax)

    // Any class implementing Billable must provide the implementation for calculateBill()
    double calculateBill();
}
