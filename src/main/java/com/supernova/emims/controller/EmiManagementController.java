package com.supernova.emims.controller;

import com.supernova.emims.entity.LmsReceiptPaymentDtl17557;
import com.supernova.emims.entity.LmsAllocationDtl17557;
import com.supernova.emims.service.EmiManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for EMI Management operations
 * Provides REST endpoints for EMI calculations, payments, and allocations
 *
 * Sonar-compliant: Proper REST design and error handling
 */
@RestController
@RequestMapping("/api/emi")
public class EmiManagementController {

    private static final Logger logger = LoggerFactory.getLogger(EmiManagementController.class);

    private final EmiManagementService emiManagementService;

    public EmiManagementController(EmiManagementService emiManagementService) {
        this.emiManagementService = emiManagementService;
    }

    /**
     * Validate loan account number
     * @param loanAccountNo the loan account number
     * @return ResponseEntity with validation result
     */
    @GetMapping("/validate/{loanAccountNo}")
    public ResponseEntity<Map<String, Object>> validateLoanAccount(@PathVariable String loanAccountNo) {
        logger.info("Validating loan account: {}", loanAccountNo);

        Map<String, Object> response = new HashMap<>();
        try {
            boolean isValid = emiManagementService.validateLoanAccount(loanAccountNo);

            response.put("success", true);
            response.put("valid", isValid);
            response.put("loanAccountNo", loanAccountNo);
            response.put("message", isValid ? "Loan account exists" : "Loan account does not exist");

            HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            logger.error("Error validating loan account: {}", loanAccountNo, e);
            response.put("success", false);
            response.put("message", "Error validating loan account: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Calculate EMI details for a loan account
     * @param loanAccountNo the loan account number
     * @return ResponseEntity with EMI details
     */
    @GetMapping("/calculate/{loanAccountNo}")
    public ResponseEntity<Map<String, Object>> calculateEmiDetails(@PathVariable String loanAccountNo) {
        logger.info("Calculating EMI details for loan account: {}", loanAccountNo);

        Map<String, Object> response = new HashMap<>();
        try {
            EmiManagementService.EmiDetails emiDetails = emiManagementService.calculateEmiDetails(loanAccountNo);

            response.put("success", true);
            response.put("loanAccountNo", emiDetails.getLoanAccountNo());
            response.put("pendingEmiAmount", emiDetails.getPendingEmiAmount());
            response.put("penaltyCharges", emiDetails.getPenaltyCharges());
            response.put("totalAmount", emiDetails.getTotalAmount());
            response.put("message", "EMI details calculated successfully");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid loan account: {}", loanAccountNo);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            logger.error("Error calculating EMI details for loan account: {}", loanAccountNo, e);
            response.put("success", false);
            response.put("message", "Error calculating EMI details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Process payment for a loan account
     * @param request the payment request containing loanAccountNo, paymentAmount, and paymentMode
     * @return ResponseEntity with payment result
     */
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentRequest request) {
        logger.info("Processing payment for loan account: {}", request.getLoanAccountNo());

        Map<String, Object> response = new HashMap<>();
        try {
            // Validate request
            if (request.getLoanAccountNo() == null || request.getLoanAccountNo().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Loan account number is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (request.getPaymentAmount() == null || request.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
                response.put("success", false);
                response.put("message", "Payment amount must be greater than zero");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (request.getPaymentMode() == null || request.getPaymentMode().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Payment mode is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Process payment
            LmsReceiptPaymentDtl17557 receipt = emiManagementService.processPayment(
                    request.getLoanAccountNo(),
                    request.getPaymentAmount(),
                    request.getPaymentMode()
            );

            response.put("success", true);
            response.put("receiptId", receipt.getReceiptId());
            response.put("loanAccountNo", receipt.getLoanAccountNo());
            response.put("paymentAmount", receipt.getPaymentAmount());
            response.put("paymentMode", receipt.getPaymentMode());
            response.put("paymentDate", receipt.getPaymentDate());
            response.put("message", "Payment processed successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid payment request: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            logger.error("Error processing payment", e);
            response.put("success", false);
            response.put("message", "Error processing payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get allocation details for a loan account
     * @param loanAccountNo the loan account number
     * @return ResponseEntity with allocation details
     */
    @GetMapping("/allocations/{loanAccountNo}")
    public ResponseEntity<Map<String, Object>> getAllocationDetails(@PathVariable String loanAccountNo) {
        logger.info("Getting allocation details for loan account: {}", loanAccountNo);

        Map<String, Object> response = new HashMap<>();
        try {
            List<LmsAllocationDtl17557> allocations = emiManagementService.getAllocationDetails(loanAccountNo);

            response.put("success", true);
            response.put("loanAccountNo", loanAccountNo);
            response.put("allocations", allocations);
            response.put("count", allocations.size());
            response.put("message", "Allocation details retrieved successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting allocation details for loan account: {}", loanAccountNo, e);
            response.put("success", false);
            response.put("message", "Error retrieving allocation details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get payment history for a loan account
     * @param loanAccountNo the loan account number
     * @return ResponseEntity with payment history
     */
    @GetMapping("/history/{loanAccountNo}")
    public ResponseEntity<Map<String, Object>> getPaymentHistory(@PathVariable String loanAccountNo) {
        logger.info("Getting payment history for loan account: {}", loanAccountNo);

        Map<String, Object> response = new HashMap<>();
        try {
            List<LmsReceiptPaymentDtl17557> payments = emiManagementService.getPaymentHistory(loanAccountNo);

            response.put("success", true);
            response.put("loanAccountNo", loanAccountNo);
            response.put("payments", payments);
            response.put("count", payments.size());
            response.put("message", "Payment history retrieved successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting payment history for loan account: {}", loanAccountNo, e);
            response.put("success", false);
            response.put("message", "Error retrieving payment history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Inner class for payment request
     */
    public static class PaymentRequest {
        private String loanAccountNo;
        private BigDecimal paymentAmount;
        private String paymentMode;

        public PaymentRequest() {}

        public PaymentRequest(String loanAccountNo, BigDecimal paymentAmount, String paymentMode) {
            this.loanAccountNo = loanAccountNo;
            this.paymentAmount = paymentAmount;
            this.paymentMode = paymentMode;
        }

        // Getters and Setters
        public String getLoanAccountNo() { return loanAccountNo; }
        public void setLoanAccountNo(String loanAccountNo) { this.loanAccountNo = loanAccountNo; }

        public BigDecimal getPaymentAmount() { return paymentAmount; }
        public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

        public String getPaymentMode() { return paymentMode; }
        public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    }
}
