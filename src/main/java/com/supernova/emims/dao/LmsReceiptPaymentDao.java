package com.supernova.emims.dao;

import com.supernova.emims.entity.LmsReceiptPaymentDtl17557;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface for LMS_RECEIPT_PAYMENT_DTL_17557 operations
 * Provides data access methods for payment receipts
 *
 * Sonar-compliant: Proper interface design and documentation
 */
public interface LmsReceiptPaymentDao {

    /**
     * Save a new receipt record
     * @param receipt the receipt entity to save
     * @return the saved entity with generated ID
     */
    LmsReceiptPaymentDtl17557 save(LmsReceiptPaymentDtl17557 receipt);

    /**
     * Find receipt by ID
     * @param id the receipt ID
     * @return Optional containing the receipt if found
     */
    Optional<LmsReceiptPaymentDtl17557> findById(Long id);

    /**
     * Find all receipts for a loan account
     * @param loanAccountNo the loan account number
     * @return list of receipts for the account
     */
    List<LmsReceiptPaymentDtl17557> findByLoanAccountNo(String loanAccountNo);

    /**
     * Find receipts by payment mode
     * @param paymentMode the payment mode
     * @return list of receipts with the specified payment mode
     */
    List<LmsReceiptPaymentDtl17557> findByPaymentMode(String paymentMode);

    /**
     * Get total payment amount for a loan account
     * @param loanAccountNo the loan account number
     * @return total payment amount
     */
    java.math.BigDecimal getTotalPaymentAmount(String loanAccountNo);

    /**
     * Delete receipt by ID
     * @param id the receipt ID
     */
    void deleteById(Long id);
}
