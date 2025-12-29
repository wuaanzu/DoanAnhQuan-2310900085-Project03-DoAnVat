package com.Daq.Mikifood.K23CNT1.security;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DaqStaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong database
        DaqStaff staff = staffRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        // Convert DaqStaff sang UserDetails
        // Role trong DB: "ADMIN" hoặc "USER"
        // Spring Security sẽ tự thêm prefix "ROLE_"
        return User.builder()
                .username(staff.getUsername())
                .password(staff.getPassword())
                .roles(staff.getRole()) // "ADMIN" -> "ROLE_ADMIN", "USER" -> "ROLE_USER"
                .build();
    }
}
