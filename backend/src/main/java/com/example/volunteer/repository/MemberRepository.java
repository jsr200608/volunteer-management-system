
package com.example.volunteer.repository;

import com.example.volunteer.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByStudentId(String studentId);
    Optional<Member> findByUserId(Long userId);
    
    Page<Member> findByCollege(String college, Pageable pageable);
    Page<Member> findByGrade(String grade, Pageable pageable);
    Page<Member> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT m FROM Member m WHERE m.name LIKE %:keyword% OR m.studentId LIKE %:keyword%")
    Page<Member> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT m FROM Member m WHERE " +
           "(:college IS NULL OR m.college = :college) AND " +
           "(:grade IS NULL OR m.grade = :grade) AND " +
           "(:status IS NULL OR m.status = :status)")
    Page<Member> findByFilters(@Param("college") String college, 
                               @Param("grade") String grade, 
                               @Param("status") String status, 
                               Pageable pageable);
}
