package com.lab.model.repository;

import com.lab.model.model.DaysOff;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<DaysOff, Long> {
    List<DaysOff> findAllByUserId(Long userId);
}
