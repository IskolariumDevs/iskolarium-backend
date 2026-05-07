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
           "LEFT JOIN c.eligibleStrands st " + // <-- Changed to match Line 40
           "WHERE c.minGwa >= :userGwa " +
           "AND (c.eligibleCities = :city OR c.eligibleCities = 'National' OR c.eligibleCities IS NULL) " + // <-- Changed to match Line 25
           "AND (c.incomeBracket = :income OR c.incomeBracket = 'All' OR c.incomeBracket IS NULL) " +
           "AND (st = :userStrand OR st = 'All' OR st IS NULL)")
    List<Scholarship> findRecommended(
        @Param("userGwa") Double userGwa,
        @Param("income") String income,
        @Param("city") String city,
        @Param("userStrand") String userStrand);
}