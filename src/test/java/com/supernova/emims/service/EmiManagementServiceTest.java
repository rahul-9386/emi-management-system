package com.supernova.emims.service;

import com.supernova.emims.dao.LmsReceivablePayableDao;
import com.supernova.emims.dao.LmsReceiptPaymentDao;
import com.supernova.emims.dao.LmsAllocationDao;
import com.supernova.emims.entity.LmsReceivablePayableDtl17557;
import com.supernova.emims.entity.LmsReceiptPaymentDtl17557;
import com.supernova.emims.entity.LmsAllocationDtl17557;
import com.supernova.emims.service.impl.EmiManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmiManagementService
 * Tests business logic with mocked dependencies
 *
 * Sonar-compliant: Proper test structure and assertions
 */
@ExtendWith(MockitoExtension.class)
public class EmiManagementServiceTest {

    @Mock
    private LmsReceivablePayableDao receivableDao;

    @Mock
    private LmsReceiptPaymentDao receiptDao;

    @Mock
    private LmsAllocationDao allocationDao;

    @InjectMocks
    private EmiManagementServiceImpl emiManagementService;

    private LmsReceivablePayableDtl17557 testReceivable;
    private LmsReceiptPaymentDtl17557 testReceipt;
    private LmsAllocationDtl17557 testAllocation;

    @BeforeEach
    void setUp() {
        // Create test data
        testReceivable = new LmsReceivablePayableDtl17557();
        testReceivable.setReceivableId(1L);
        testReceivable.setLoanAccountNo("TEST123");
        testReceivable.setPendingEmiAmount(new BigDecimal("1000.00"));
        testReceivable.setPenaltyCharges(new BigDecimal("50.00"));
        testReceivable.setTotalAmount(new BigDecimal("1050.00"));
        testReceivable.setCreatedDate(new Date());

        testReceipt = new LmsReceiptPaymentDtl17557();
        testReceipt.setReceiptId(1L);
        testReceipt.setLoanAccountNo("TEST123");
        testReceipt.setPaymentAmount(new BigDecimal("500.00"));
        testReceipt.setPaymentMode("Cash");
        testReceipt.setPaymentDate(new Date());

        testAllocation = new LmsAllocationDtl17557();
        testAllocation.setAllocationId(1L);
        testAllocation.setLoanAccountNo("TEST123");
        testAllocation.setAllocatedTo("Penalty");
        testAllocation.setAllocatedAmount(new BigDecimal("50.00"));
        testAllocation.setAllocationDate(new Date());
    }

    @Test
    void testValidateLoanAccount_ValidAccount() {
        // Given
        when(receivableDao.existsByLoanAccountNo("TEST123")).thenReturn(true);

        // When
        boolean result = emiManagementService.validateLoanAccount("TEST123");

        // Then
        assertTrue(result);
        verify(receivableDao).existsByLoanAccountNo("TEST123");
    }

    @Test
    void testValidateLoanAccount_InvalidAccount() {
        // Given
        when(receivableDao.existsByLoanAccountNo("INVALID")).thenReturn(false);

        // When
        boolean result = emiManagementService.validateLoanAccount("INVALID");

        // Then
        assertFalse(result);
        verify(receivableDao).existsByLoanAccountNo("INVALID");
    }

    @Test
    void testCalculateEmiDetails_Success() {
        // Given
        when(receivableDao.findLatestByLoanAccountNo("TEST123")).thenReturn(Optional.of(testReceivable));

        // When
        EmiManagementService.EmiDetails result = emiManagementService.calculateEmiDetails("TEST123");

        // Then
        assertNotNull(result);
        assertEquals("TEST123", result.getLoanAccountNo());
        assertEquals(new BigDecimal("1000.00"), result.getPendingEmiAmount());
        assertEquals(new BigDecimal("50.00"), result.getPenaltyCharges());
        assertEquals(new BigDecimal("1050.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateEmiDetails_AccountNotFound() {
        // Given
        when(receivableDao.findLatestByLoanAccountNo("INVALID")).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> emiManagementService.calculateEmiDetails("INVALID")
        );

        assertEquals("No EMI details found for loan account: INVALID", exception.getMessage());
    }

    @Test
    void testProcessPayment_Success() {
        // Given
        when(receivableDao.findLatestByLoanAccountNo("TEST123")).thenReturn(Optional.of(testReceivable));
        when(receiptDao.save(any(LmsReceiptPaymentDtl17557.class))).thenReturn(testReceipt);

        // When
        LmsReceiptPaymentDtl17557 result = emiManagementService.processPayment(
                "TEST123",
                new BigDecimal("500.00"),
                "Cash"
        );

        // Then
        assertNotNull(result);
        assertEquals("TEST123", result.getLoanAccountNo());
        assertEquals(new BigDecimal("500.00"), result.getPaymentAmount());
        assertEquals("Cash", result.getPaymentMode());

        verify(receivableDao).findLatestByLoanAccountNo("TEST123");
        verify(receiptDao).save(any(LmsReceiptPaymentDtl17557.class));
        verify(allocationDao, times(2)).save(any(LmsAllocationDtl17557.class));
    }

    @Test
    void testProcessPayment_ZeroAmount() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> emiManagementService.processPayment("TEST123", BigDecimal.ZERO, "Cash")
        );

        assertEquals("Payment amount must be greater than zero", exception.getMessage());
    }

    @Test
    void testProcessPayment_NegativeAmount() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> emiManagementService.processPayment("TEST123", new BigDecimal("-100.00"), "Cash")
        );

        assertEquals("Payment amount must be greater than zero", exception.getMessage());
    }

    @Test
    void testGetAllocationDetails_Success() {
        // Given
        List<LmsAllocationDtl17557> allocations = new ArrayList<>();
        allocations.add(testAllocation);
        when(allocationDao.findByLoanAccountNo("TEST123")).thenReturn(allocations);

        // When
        List<LmsAllocationDtl17557> result = emiManagementService.getAllocationDetails("TEST123");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TEST123", result.get(0).getLoanAccountNo());
        assertEquals("Penalty", result.get(0).getAllocatedTo());

        verify(allocationDao).findByLoanAccountNo("TEST123");
    }

    @Test
    void testGetPaymentHistory_Success() {
        // Given
        List<LmsReceiptPaymentDtl17557> payments = new ArrayList<>();
        payments.add(testReceipt);
        when(receiptDao.findByLoanAccountNo("TEST123")).thenReturn(payments);

        // When
        List<LmsReceiptPaymentDtl17557> result = emiManagementService.getPaymentHistory("TEST123");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TEST123", result.get(0).getLoanAccountNo());
        assertEquals("Cash", result.get(0).getPaymentMode());

        verify(receiptDao).findByLoanAccountNo("TEST123");
    }

    @Test
    void testGetAllocationDetails_EmptyList() {
        // Given
        when(allocationDao.findByLoanAccountNo("TEST123")).thenReturn(new ArrayList<>());

        // When
        List<LmsAllocationDtl17557> result = emiManagementService.getAllocationDetails("TEST123");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(allocationDao).findByLoanAccountNo("TEST123");
    }

    @Test
    void testGetPaymentHistory_EmptyList() {
        // Given
        when(receiptDao.findByLoanAccountNo("TEST123")).thenReturn(new ArrayList<>());

        // When
        List<LmsReceiptPaymentDtl17557> result = emiManagementService.getPaymentHistory("TEST123");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(receiptDao).findByLoanAccountNo("TEST123");
    }
}
