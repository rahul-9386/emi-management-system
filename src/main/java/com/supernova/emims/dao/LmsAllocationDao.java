package com.supernova.emims.dao;

import com.supernova.emims.entity.LmsAllocationDtl17557;
import java.util.List;

/**
 * DAO interface for LMS_ALLOCATION_DTL_17557_ operations
 * Provides data access methods for payment allocations
 *
 * Sonar-compliant: Proper interface design and documentation
 */
public interface LmsAllocationDao {

    /**
     * Save a new allocation record
     * @param allocation the allocation entity to save
     * @return the saved entity with generated ID
     */
    LmsAllocationDtl17557 save(LmsAllocationDtl17557 allocation);

    /**
     * Find all allocations for a loan account
     * @param loanAccountNo the loan account number
     * @return list of allocations for the account
     */
    List<LmsAllocationDtl17557> findByLoanAccountNo(String loanAccountNo);

    /**
     * Find allocations by allocation type
     * @param allocatedTo the allocation type (Penalty or EMI)
     * @return list of allocations for the specified type
     */
    List<LmsAllocationDtl17557> findByAllocatedTo(String allocatedTo);

    /**
     * Get total allocated amount for a loan account
     * @param loanAccountNo the loan account number
     * @return total allocated amount
     */
    java.math.BigDecimal getTotalAllocatedAmount(String loanAccountNo);

    /**
     * Get total allocated amount by type for a loan account
     * @param loanAccountNo the loan account number
     * @param allocatedTo the allocation type
     * @return total allocated amount for the specified type
     */
    java.math.BigDecimal getTotalAllocatedAmountByType(String loanAccountNo, String allocatedTo);

    /**
     * Delete all allocations for a loan account
     * @param loanAccountNo the loan account number
     */
    void deleteByLoanAccountNo(String loanAccountNo);
}
