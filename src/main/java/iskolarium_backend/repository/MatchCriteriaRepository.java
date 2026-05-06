package iskolarium_backend.repository;

import iskolarium_backend.entity.MatchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchCriteriaRepository extends JpaRepository<MatchCriteria, Long> {
}