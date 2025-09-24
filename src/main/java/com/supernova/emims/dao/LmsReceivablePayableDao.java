package com.supernova.emims.dao;

import com.supernova.emims.entity.LmsReceivablePayableDtl17557;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface for LMS_RECEIVABLEPAYBLE_DTL_17557 operations
 * Provides data access methods for EMI receivable details
 *
 * Sonar-compliant: Proper interface design and documentation
 */
public interface LmsReceivablePayableDao {

    /**
     * Save a new receivable record
     * @param receivable the receivable entity to save
     * @return the saved entity with generated ID
     */
    LmsReceivablePayableDtl17557 save(LmsReceivablePayableDtl17557 receivable);

    /**
     * Find receivable by ID
     * @param id the receivable ID
     * @return Optional containing the receivable if found
     */
    Optional<LmsReceivablePayableDtl17557> findById(Long id);

    /**
     * Find all receivables for a loan account
     * @param loanAccountNo the loan account number
     * @return list of receivables for the account
     */
    List<LmsReceivablePayableDtl17557> findByLoanAccountNo(String loanAccountNo);

    /**
     * Find the latest receivable for a loan account
     * @param loanAccountNo the loan account number
     * @return Optional containing the latest receivable if found
     */
    Optional<LmsReceivablePayableDtl17557> findLatestByLoanAccountNo(String loanAccountNo);

    /**
     * Check if a loan account exists
     * @param loanAccountNo the loan account number
     * @return true if account exists, false otherwise
     */
    boolean existsByLoanAccountNo(String loanAccountNo);

    /**
     * Delete all receivables for a loan account
     * @param loanAccountNo the loan account number
     */
    void deleteByLoanAccountNo(String loanAccountNo);

    /**
     * Get total pending amount for a loan account
     * @param loanAccountNo the loan account number
     * @return total pending amount
     */
    java.math.BigDecimal getTotalPendingAmount(String loanAccountNo);
}
