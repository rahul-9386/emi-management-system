package com.supernova.emims.service.impl;

import com.supernova.emims.dao.LmsReceivablePayableDao;
import com.supernova.emims.dao.LmsReceiptPaymentDao;
import com.supernova.emims.dao.LmsAllocationDao;
import com.supernova.emims.entity.LmsReceivablePayableDtl17557;
import com.supernova.emims.entity.LmsReceiptPaymentDtl17557;
import com.supernova.emims.entity.LmsAllocationDtl17557;
import com.supernova.emims.service.EmiManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Service implementation for EMI Management operations
 * Contains business logic for EMI calculations, payments, and allocations
 *
 * Sonar-compliant: Proper transaction management and error handling
 */
@Service
@Transactional
public class EmiManagementServiceImpl implements EmiManagementService {

    private static final Logger logger = LoggerFactory.getLogger(EmiManagementServiceImpl.class);
    private static final BigDecimal DAILY_PENALTY_RATE = new BigDecimal("10.00");

    private final LmsReceivablePayableDao receivableDao;
    private final LmsReceiptPaymentDao receiptDao;
    private final LmsAllocationDao allocationDao;

    public EmiManagementServiceImpl(LmsReceivablePayableDao receivableDao,
                                  LmsReceiptPaymentDao receiptDao,
                                  LmsAllocationDao allocationDao) {
        this.receivableDao = receivableDao;
        this.receiptDao = receiptDao;
        this.allocationDao = allocationDao;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateLoanAccount(String loanAccountNo) {
        logger.info("Validating loan account: {}", loanAccountNo);
        return receivableDao.existsByLoanAccountNo(loanAccountNo);
    }

    @Override
    @Transactional(readOnly = true)
    public EmiDetails calculateEmiDetails(String loanAccountNo) {
        logger.info("Calculating EMI details for loan account: {}", loanAccountNo);

        // Get the latest receivable record
        LmsReceivablePayableDtl17557 latestReceivable = receivableDao.findLatestByLoanAccountNo(loanAccountNo)
                .orElseThrow(() -> new IllegalArgumentException("No EMI details found for loan account: " + loanAccountNo));

        // Calculate penalty if EMI is delayed (simplified logic)
        BigDecimal penaltyCharges = calculatePenalty(latestReceivable.getPendingEmiAmount(),
                                                   latestReceivable.getCreatedDate());

        BigDecimal totalAmount = latestReceivable.getPendingEmiAmount().add(penaltyCharges);

        return new EmiDetails(loanAccountNo, latestReceivable.getPendingEmiAmount(),
                            penaltyCharges, totalAmount);
    }

    @Override
    public LmsReceiptPaymentDtl17557 processPayment(String loanAccountNo, BigDecimal paymentAmount, String paymentMode) {
        logger.info("Processing payment for loan account: {}, Amount: {}, Mode: {}",
                   loanAccountNo, paymentAmount, paymentMode);

        // Validate payment amount
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        // Get EMI details
        EmiDetails emiDetails = calculateEmiDetails(loanAccountNo);

        // Create receipt
        LmsReceiptPaymentDtl17557 receipt = new LmsReceiptPaymentDtl17557(
                loanAccountNo, paymentAmount, paymentMode, new Date());
        receipt = receiptDao.save(receipt);

        // Perform allocation
        performAllocation(loanAccountNo, paymentAmount, emiDetails);

        logger.info("Payment processed successfully. Receipt ID: {}", receipt.getReceiptId());
        return receipt;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsAllocationDtl17557> getAllocationDetails(String loanAccountNo) {
        logger.info("Getting allocation details for loan account: {}", loanAccountNo);
        return allocationDao.findByLoanAccountNo(loanAccountNo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsReceiptPaymentDtl17557> getPaymentHistory(String loanAccountNo) {
        logger.info("Getting payment history for loan account: {}", loanAccountNo);
        return receiptDao.findByLoanAccountNo(loanAccountNo);
    }

    /**
     * Calculate penalty charges based on delay
     * @param pendingAmount the pending EMI amount
     * @param createdDate the date when EMI was created
     * @return calculated penalty amount
     */
    private BigDecimal calculatePenalty(BigDecimal pendingAmount, Date createdDate) {
        // Simplified penalty calculation - ₹10 per day if delayed
        // In real implementation, this would calculate actual days delayed
        long daysDelayed = 5; // Assuming 5 days delay for demo

        if (daysDelayed > 0) {
            return DAILY_PENALTY_RATE.multiply(new BigDecimal(daysDelayed));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Perform payment allocation with priority: Penalty -> EMI
     * @param loanAccountNo the loan account number
     * @param paymentAmount the total payment amount
     * @param emiDetails the EMI details
     */
    private void performAllocation(String loanAccountNo, BigDecimal paymentAmount, EmiDetails emiDetails) {
        BigDecimal remainingAmount = paymentAmount;
        List<LmsAllocationDtl17557> allocations = new ArrayList<>();

        // Allocate to penalty first
        if (emiDetails.getPenaltyCharges().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal penaltyAllocation = remainingAmount.min(emiDetails.getPenaltyCharges());
            allocations.add(new LmsAllocationDtl17557(loanAccountNo, "Penalty",
                                                    penaltyAllocation, new Date()));
            remainingAmount = remainingAmount.subtract(penaltyAllocation);
        }

        // Allocate remaining to EMI
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal emiAllocation = remainingAmount.min(emiDetails.getPendingEmiAmount());
            allocations.add(new LmsAllocationDtl17557(loanAccountNo, "EMI",
                                                    emiAllocation, new Date()));
        }

        // Save all allocations
        for (LmsAllocationDtl17557 allocation : allocations) {
            allocationDao.save(allocation);
        }

        logger.info("Allocation completed for {} allocations", allocations.size());
    }
}
