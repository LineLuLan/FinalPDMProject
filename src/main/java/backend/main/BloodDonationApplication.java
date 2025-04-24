package backend.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BloodDonationApplication {
    public static void main(String[] args) {
        // Test SQL connection before starting Spring Boot
        try {
            backend.main.swingtool.ConnectSQL.getConnection().close();
            System.out.println("[INIT] MySQL connection OK!");
        } catch (Exception e) {
            System.err.println("[INIT] MySQL connection FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        SpringApplication.run(BloodDonationApplication.class, args);
    }
}

