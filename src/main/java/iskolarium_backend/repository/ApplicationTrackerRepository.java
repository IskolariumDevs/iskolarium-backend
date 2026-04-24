package iskolarium_backend.repository;

import iskolarium_backend.entity.ApplicationTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationTrackerRepository extends JpaRepository<ApplicationTracker, Long> {
}