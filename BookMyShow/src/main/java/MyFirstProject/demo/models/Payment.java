package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents a payment transaction for a booking.
 *
 * Table: payment
 *
 * Multiple payments can exist for one booking:
 * - Initial payment attempt
 * - Retry after failure
 * - Partial payments (e.g., pay 50% now, 50% later)
 * - Refunds (negative amount or separate refund record)
 *
 * Example scenario:
 * Booking#1 (₹900):
 *   - Payment#1: ₹900, FAILED (card declined)
 *   - Payment#2: ₹900, SUCCESS (used different card)
 */
@Getter
@Setter
@Entity
public class Payment extends BaseModel {

    /**
     * Payment method used (Credit Card, Debit Card, UPI, etc.).
     *
     * @Enumerated(ORDINAL): Stores as integer (0, 1, 2...)
     *
     * Available methods:
     * - CREDIT_CARD (0)
     * - DEBIT_CARD (1)
     *
     * Production would have more:
     * - UPI, NET_BANKING, WALLET (Paytm, PhonePe)
     */
    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod paymentMethod;

    /**
     * Status of the payment (Success, Failed, Pending).
     *
     * @Enumerated(ORDINAL): Stores as integer
     *
     * Possible values:
     * - SUCCESS (0): Payment completed successfully
     * - FAILED (1): Payment failed (insufficient funds, declined, etc.)
     *
     * Production would have more:
     * - PENDING: Payment initiated, awaiting confirmation
     * - REFUNDED: Money returned to customer
     * - CANCELLED: Payment cancelled by user
     */
    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;

    /**
     * Payment gateway used (Razorpay, PayPal, Stripe, etc.).
     *
     * @Enumerated(ORDINAL): Stores as integer
     *
     * Available providers:
     * - RAZORPAY (0): Popular in India
     * - PAYPAL (1): International payments
     *
     * Production would have more:
     * - STRIPE, PAYTM, PHONEPE, GOOGLE_PAY
     *
     * Why track provider:
     * - Different providers have different fees
     * - Reconciliation with provider statements
     * - Analytics on which provider works best
     */
    @Enumerated(EnumType.ORDINAL)
    private PaymentProvider paymentProvider;

    /**
     * Payment amount in smallest currency unit (paisa/cents).
     *
     * Examples:
     * - ₹900 stored as: 900
     * - $10.50 stored as: 1050 (cents)
     *
     * Why int and not double:
     * - Avoids floating point precision errors
     * - Money calculations should always use integers
     *
     * For refunds:
     * Could be negative or stored as separate refund record
     */
    private int amount;

    /**
     * Reference number from payment gateway.
     *
     * Also known as:
     * - Transaction ID
     * - Payment ID
     * - Gateway reference
     *
     * Used for:
     * - Reconciliation with payment gateway
     * - Tracking in gateway dashboard
     * - Customer support and dispute resolution
     * - Refund processing
     *
     * Example formats:
     * - Razorpay: "pay_29QQoUBi66xm2f"
     * - PayPal: "PAYID-ABCD1234"
     *
     * Note: Currently int, should be String in production
     * as reference numbers can have letters
     */
    private int referenceNumber;

    /**
     * When the payment was made.
     *
     * Used for:
     * - Payment history display
     * - Transaction reports
     * - Reconciliation with bank statements
     * - Audit trails
     *
     * Different from createdAt:
     * - createdAt: When record was created in our DB
     * - timeStamp: Actual payment time from gateway
     */
    private Date timeStamp;
}
