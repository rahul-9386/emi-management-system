package com.supernova.emims.service;

import com.supernova.emims.entity.LmsReceivablePayableDtl17557;
import com.supernova.emims.entity.LmsReceiptPaymentDtl17557;
import com.supernova.emims.entity.LmsAllocationDtl17557;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for EMI Management operations
 * Provides business logic for EMI calculations, payments, and allocations
 *
 * Sonar-compliant: Proper interface design and documentation
 */
public interface EmiManagementService {

    /**
     * Validate loan account number
     * @param loanAccountNo the loan account number to validate
     * @return true if account exists, false otherwise
     */
    boolean validateLoanAccount(String loanAccountNo);

    /**
     * Calculate EMI details for a loan account
     * @param loanAccountNo the loan account number
     * @return EMI details with pending amount and penalty
     */
    EmiDetails calculateEmiDetails(String loanAccountNo);

    /**
     * Process payment for a loan account
     * @param loanAccountNo the loan account number
     * @param paymentAmount the payment amount
     * @param paymentMode the payment mode
     * @return the generated receipt
     */
    LmsReceiptPaymentDtl17557 processPayment(String loanAccountNo, BigDecimal paymentAmount, String paymentMode);

    /**
     * Get allocation details for a loan account
     * @param loanAccountNo the loan account number
     * @return list of allocations
     */
    List<LmsAllocationDtl17557> getAllocationDetails(String loanAccountNo);

    /**
     * Get payment history for a loan account
     * @param loanAccountNo the loan account number
     * @return list of payment receipts
     */
    List<LmsReceiptPaymentDtl17557> getPaymentHistory(String loanAccountNo);

    /**
     * Inner class to hold EMI calculation details
     */
    class EmiDetails {
        private String loanAccountNo;
        private BigDecimal pendingEmiAmount;
        private BigDecimal penaltyCharges;
        private BigDecimal totalAmount;

        public EmiDetails() {}

        public EmiDetails(String loanAccountNo, BigDecimal pendingEmiAmount, BigDecimal penaltyCharges, BigDecimal totalAmount) {
            this.loanAccountNo = loanAccountNo;
            this.pendingEmiAmount = pendingEmiAmount;
            this.penaltyCharges = penaltyCharges;
            this.totalAmount = totalAmount;
        }

        // Getters and Setters
        public String getLoanAccountNo() { return loanAccountNo; }
        public void setLoanAccountNo(String loanAccountNo) { this.loanAccountNo = loanAccountNo; }

        public BigDecimal getPendingEmiAmount() { return pendingEmiAmount; }
        public void setPendingEmiAmount(BigDecimal pendingEmiAmount) { this.pendingEmiAmount = pendingEmiAmount; }

        public BigDecimal getPenaltyCharges() { return penaltyCharges; }
        public void setPenaltyCharges(BigDecimal penaltyCharges) { this.penaltyCharges = penaltyCharges; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    }
}
