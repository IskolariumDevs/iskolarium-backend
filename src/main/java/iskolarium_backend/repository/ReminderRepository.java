package iskolarium_backend.repository;

import iskolarium_backend.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByNotificationDateLessThanEqualAndIsSentFalse(LocalDate date);
}