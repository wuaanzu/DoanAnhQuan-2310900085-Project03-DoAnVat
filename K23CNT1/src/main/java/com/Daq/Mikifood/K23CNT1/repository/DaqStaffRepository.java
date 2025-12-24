package com.Daq.Mikifood.K23CNT1.repository;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaqStaffRepository extends JpaRepository<DaqStaff, Integer> {
    Optional<DaqStaff> findByPhone(String phone);
    List<DaqStaff> findByRole(String role);
}
