package com.hiretrack.backend.scheduler;

import com.hiretrack.backend.entity.Drive;
import com.hiretrack.backend.entity.Student;
import com.hiretrack.backend.repository.DriveApplicationRepository;
import com.hiretrack.backend.repository.DriveRepository;
import com.hiretrack.backend.repository.StudentRepository;
import com.hiretrack.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final DriveRepository driveRepository;
    private final DriveApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    // Run at 8:00 AM every day
    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduleDriveReminders() {
        LocalDate today = LocalDate.now();
        List<Drive> allDrives = driveRepository.findAll();

        for (Drive drive : allDrives) {
            if (drive.getDriveDate() == null) continue;
            
            LocalDate driveDate = drive.getDriveDate();
            long daysBetween = ChronoUnit.DAYS.between(today, driveDate);
            
            if (daysBetween == 2 || daysBetween == 1 || daysBetween == 0) {
                String title = "Upcoming Drive: " + drive.getCompany().getName();
                String message = daysBetween == 0 ? "The drive is happening TODAY!" : "The drive is coming up in " + daysBetween + " day(s).";

                applicationRepository.findAll().stream()
                    .filter(app -> app.getDrive() != null && app.getDrive().getId().equals(drive.getId()))
                    .forEach(app -> {
                        notificationService.createNotification(
                            app.getStudent().getUser(), 
                            title, 
                            message, 
                            "DRIVE_REMINDER", 
                            true
                        );
                    });
            }
        }
    }

    // Run every Sunday at midnight
    @Scheduled(cron = "0 0 0 * * SUN")
    public void auditProfiles() {
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            if (student.getProfileCompletion() != null && student.getProfileCompletion() < 80.0) {
                notificationService.createNotification(
                    student.getUser(),
                    "Incomplete Profile",
                    "Your profile is only " + student.getProfileCompletion() + "% complete. Please complete it to become eligible for more drives.",
                    "SYSTEM_ALERT",
                    true
                );
            }
        }
    }
}
