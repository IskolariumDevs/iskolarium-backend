package iskolarium_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import iskolarium_backend.entity.Scholarship;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
@Query("SELECT s FROM Scholarship s JOIN s.matchCriteria c WHERE c.minGwa >= :userGwa " +
       "AND (s.city = :city OR s.city = 'National' OR s.city IS NULL) " +
       "AND (s.incomeBracket = :income OR s.incomeBracket = 'All' OR s.incomeBracket IS NULL)")
List<Scholarship> findRecommended(
    @Param("userGwa") Double userGwa,
    @Param("income") String income,
    @Param("city") String city);
}
