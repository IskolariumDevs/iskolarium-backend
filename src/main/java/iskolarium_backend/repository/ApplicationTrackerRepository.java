package iskolarium_backend.repository;

import iskolarium_backend.entity.ApplicationTracker;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.entity.UserAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationTrackerRepository extends JpaRepository<ApplicationTracker, Long> {
    List<ApplicationTracker> findByUserAccount_AccountId(Long accountId);

    boolean existsByUserAccountAndScholarship(UserAccount userAccount, Scholarship scholarship);
}