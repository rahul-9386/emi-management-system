package com.supernova.emims.dao.impl;

import com.supernova.emims.dao.LmsAllocationDao;
import com.supernova.emims.entity.LmsAllocationDtl17557;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Hibernate DAO implementation for LMS_ALLOCATION_DTL_17557_ operations
 * Uses Hibernate ORM for database operations
 *
 * Sonar-compliant: Proper exception handling and resource management
 */
@Repository
@Transactional
public class LmsAllocationDaoImpl implements LmsAllocationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LmsAllocationDtl17557 save(LmsAllocationDtl17557 allocation) {
        if (allocation.getAllocationId() == null) {
            entityManager.persist(allocation);
            return allocation;
        } else {
            return entityManager.merge(allocation);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsAllocationDtl17557> findByLoanAccountNo(String loanAccountNo) {
        TypedQuery<LmsAllocationDtl17557> query = entityManager.createQuery(
                "SELECT a FROM LmsAllocationDtl17557 a WHERE a.loanAccountNo = :loanAccountNo ORDER BY a.allocationDate DESC",
                LmsAllocationDtl17557.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsAllocationDtl17557> findByAllocatedTo(String allocatedTo) {
        TypedQuery<LmsAllocationDtl17557> query = entityManager.createQuery(
                "SELECT a FROM LmsAllocationDtl17557 a WHERE a.allocatedTo = :allocatedTo ORDER BY a.allocationDate DESC",
                LmsAllocationDtl17557.class);
        query.setParameter("allocatedTo", allocatedTo);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAllocatedAmount(String loanAccountNo) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(a.allocatedAmount), 0) FROM LmsAllocationDtl17557 a WHERE a.loanAccountNo = :loanAccountNo",
                BigDecimal.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAllocatedAmountByType(String loanAccountNo, String allocatedTo) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(a.allocatedAmount), 0) FROM LmsAllocationDtl17557 a WHERE a.loanAccountNo = :loanAccountNo AND a.allocatedTo = :allocatedTo",
                BigDecimal.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        query.setParameter("allocatedTo", allocatedTo);
        return query.getSingleResult();
    }

    @Override
    public void deleteByLoanAccountNo(String loanAccountNo) {
        TypedQuery<LmsAllocationDtl17557> query = entityManager.createQuery(
                "SELECT a FROM LmsAllocationDtl17557 a WHERE a.loanAccountNo = :loanAccountNo",
                LmsAllocationDtl17557.class);
        query.setParameter("loanAccountNo", loanAccountNo);

        List<LmsAllocationDtl17557> allocations = query.getResultList();
        for (LmsAllocationDtl17557 allocation : allocations) {
            entityManager.remove(allocation);
        }
    }
}
