package hotel; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement 
public class HotelManagementSystemApp {
    public static void main(String[] args) {
        SpringApplication.run(HotelManagementSystemApp.class, args);
    }
}
