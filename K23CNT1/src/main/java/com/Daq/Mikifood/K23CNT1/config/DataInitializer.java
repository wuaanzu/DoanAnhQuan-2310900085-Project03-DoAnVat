package com.Daq.Mikifood.K23CNT1.config;

import com.Daq.Mikifood.K23CNT1.entity.DaqStaff;
import com.Daq.Mikifood.K23CNT1.entity.DaqBanner;
import com.Daq.Mikifood.K23CNT1.repository.DaqStaffRepository;
import com.Daq.Mikifood.K23CNT1.repository.DaqBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DaqStaffRepository staffRepository;

    @Autowired
    private DaqBannerRepository bannerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem đã có admin chưa
        if (staffRepository.findByUsername("admin").isEmpty()) {
            // Tạo admin account
            DaqStaff admin = new DaqStaff();
            admin.setStaffName("Administrator");
            admin.setPhone("0123456789");
            admin.setEmail("admin@mikifood.com");
            admin.setAddress("Hà Nội");
            admin.setRole("ADMIN");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));

            staffRepository.save(admin);
            System.out.println("✅ Đã tạo tài khoản admin:");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin123");
        }

        // Tạo user mẫu
        if (staffRepository.findByUsername("user").isEmpty()) {
            DaqStaff user = new DaqStaff();
            user.setStaffName("Nguyễn Văn A");
            user.setPhone("0987654321");
            user.setEmail("user@mikifood.com");
            user.setAddress("TP.HCM");
            user.setRole("USER");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));

            staffRepository.save(user);
            System.out.println("✅ Đã tạo tài khoản user:");
            System.out.println("   Username: user");
            System.out.println("   Password: user123");
        }

        // Tạo banner mẫu
        if (bannerRepository.count() == 0) {
            DaqBanner banner1 = new DaqBanner();
            banner1.setTitle("Khuyến mãi đặc biệt");
            banner1.setDescription("Giảm giá 20% cho tất cả sản phẩm");
            banner1.setImage("banner1.jpg");
            banner1.setLinkUrl("/products");
            banner1.setDisplayOrder(1);
            banner1.setIsActive(true);
            bannerRepository.save(banner1);

            DaqBanner banner2 = new DaqBanner();
            banner2.setTitle("Sản phẩm mới");
            banner2.setDescription("Khám phá các sản phẩm mới nhất của chúng tôi");
            banner2.setImage("banner2.jpg");
            banner2.setLinkUrl("/products");
            banner2.setDisplayOrder(2);
            banner2.setIsActive(true);
            bannerRepository.save(banner2);

            System.out.println("✅ Đã tạo 2 banner mẫu");
        }
    }
}
