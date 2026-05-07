package iskolarium_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import iskolarium_backend.entity.Scholarship;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    @Query("SELECT DISTINCT s FROM Scholarship s " +
           "JOIN s.matchCriteria c " +
           "LEFT JOIN c.eligibleStrands st " +
           "WHERE c.minGwa >= :userGwa " +
           "AND (c.eligibleCities LIKE CONCAT('%', :city, '%') " +
           "     OR c.eligibleCities = 'All' OR c.eligibleCities IS NULL) " +
           "AND (c.targetUniversities = 'All' OR c.targetUniversities IS NULL " +
           "     OR c.targetUniversities LIKE CONCAT('%', :university, '%')) " +
           "AND (:userStrand IS NULL OR st IS NULL OR st = 'All' OR st = :userStrand)")
    List<Scholarship> findRecommended(
        @Param("userGwa") Double userGwa,
        @Param("city") String city,
        @Param("university") String university,
        @Param("userStrand") String userStrand);
}