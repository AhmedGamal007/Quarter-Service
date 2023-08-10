package com.branch.quarter.repository;

import com.branch.quarter.model.Quarter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public interface QuarterRepository extends JpaRepository<Quarter,Long> {

    @Query(value = "SELECT * FROM quarter q WHERE q.active = true AND q.id = ?1",nativeQuery = true)
    Quarter findQuarterById(Long id);
    @Query(value = "SELECT * FROM quarter q WHERE q.active = true",
            countQuery = "SELECT COUNT(*) FROM quarter q WHERE q.active=true",
            nativeQuery = true)
    List<Quarter> findAllByActiveIsTrue(Pageable pageable);
    @Query(value = "SELECT COUNT(*) FROM quarter q WHERE q.active=true",nativeQuery = true)
    Integer getPageCount();
    @Query(value = "SELECT * FROM quarter q WHERE (q.from_date <= ?2 AND q.to_date >= ?1)AND q.active=true AND q.id <> ?3 LIMIT 1", nativeQuery = true)
    Quarter findByFromDateLessThanEqualAndToDateGreaterThanEqualAndIsActiveIsTrueAndIdIsNot(LocalDate fromDate, LocalDate toDate, Long id);
     @Query(value = "SELECT * FROM quarter q WHERE (q.from_date <= ?2 AND q.to_date >= ?1)AND q.active=true LIMIT 1", nativeQuery = true)
    Quarter findByFromDateLessThanEqualAndToDateGreaterThanEqualAndIsActiveIsTrue(LocalDate fromDate, LocalDate toDate);
    @Modifying
    @Transactional
    @Query(value = "UPDATE quarter q SET q.active=false WHERE q.id=?1" , nativeQuery = true)
    void softDeleteQuarterById(Long quarterId);
    @Query(value = "SELECT q.active FROM quarter q WHERE q.active=true AND q.quarter_name = ?1 AND q.id != ?2 LIMIT 1", nativeQuery = true )
    Object existsByQuaraterNameAndActiveIsTrue(String quarterName,Long id);
    @Query(value = "SELECT q.active FROM quarter q WHERE q.active=true AND q.quarter_name = ?1 LIMIT 1 ", nativeQuery = true )
    Object existsByQuaraterNameAndActiveIsTrue(String quarterName);
    @Modifying
    @Query(value = "UPDATE quarter q SET q.status=false WHERE q.id=?1",nativeQuery = true)
    void closeQuarterById(Long id);
}
