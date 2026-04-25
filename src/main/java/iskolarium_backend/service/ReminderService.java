package iskolarium_backend.service;

import iskolarium_backend.entity.Reminder;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.ReminderRepository;
import iskolarium_backend.repository.ScholarshipRepository;
import iskolarium_backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ScholarshipRepository scholarshipRepository;

    // user creates a reminder
    public Reminder createReminder(Long accountId, Long scholarshipId, LocalDate date) {
        UserAccount user = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        Reminder reminder = new Reminder();
        reminder.setUserAccount(user);
        reminder.setScholarship(scholarship);
        reminder.setNotificationDate(date);
        reminder.setIsSent(false);

        return reminderRepository.save(reminder);
    }

    // autumated background job to check for due reminders every day at 8 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void processDailyReminders() {
        System.out.println("⏰ Background Job Waking Up: Checking for due reminders...");

        // Get today's date
        LocalDate today = LocalDate.now();

        // Fetch all unsent reminders due today or earlier
        List<Reminder> dueReminders = reminderRepository.findByNotificationDateLessThanEqualAndIsSentFalse(today);

        for (Reminder reminder : dueReminders) {
            // this is where you trigger an Email (JavaMailSender) or SMS
            System.out.println("🔔 SENDING ALERT TO: " + reminder.getUserAccount().getEmail() + 
                               " | Scholarship ending soon: " + reminder.getScholarship().getTitle());

            // Mark as sent
            reminder.setIsSent(true);
            reminderRepository.save(reminder);
        }
        
        System.out.println("✅ Background Job Complete! Processed " + dueReminders.size() + " reminders.");
    }
}