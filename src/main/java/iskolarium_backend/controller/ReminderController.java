package iskolarium_backend.controller;

import iskolarium_backend.dto.ReminderRequestDto;
import iskolarium_backend.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/set")
    public ResponseEntity<String> setReminder(@RequestBody ReminderRequestDto dto) {
        try {
            reminderService.createReminder(dto.getAccountId(), dto.getScholarshipId(), dto.getNotificationDate());
            return ResponseEntity.ok("Reminder set successfully for " + dto.getNotificationDate());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}