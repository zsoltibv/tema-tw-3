package com.lab.model.service;

import com.lab.model.config.util.Status;
import com.lab.model.model.DaysOff;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.LeaveRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {
    private LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public List<DaysOff> getDaysOffForEmployeeId(Long id) {
        return leaveRequestRepository.findAllByUserId(id);
    }

    public Optional<DaysOff> addDayOff(UserEntity user, String startDate, String endDate) {
        DaysOff daysOff = new DaysOff();
        daysOff.setUser(user);
        daysOff.setStartDate(LocalDate.parse(startDate));
        daysOff.setEndDate(LocalDate.parse(endDate));
        daysOff.setStatus(Status.PENDING);
        leaveRequestRepository.save(daysOff);
        return Optional.of(daysOff);
    }

    public void updateLeaveStatusAndDates(Long leaveRequestId, String status, LocalDate startDate, LocalDate endDate) {
        DaysOff leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new EntityNotFoundException("LeaveRequest not found with id: " + leaveRequestId));

        // Update the leave request status, start date, and end date
        leaveRequest.setStatus(Status.fromString(status));
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);

        // Save the updated leave request
        leaveRequestRepository.save(leaveRequest);
    }
}
