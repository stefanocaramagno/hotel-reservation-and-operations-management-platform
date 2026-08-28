package hotel.services;

import hotel.models.CleaningStaff;
import hotel.repositories.CleaningStaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CleaningStaffService {

    // Attributi
    private final CleaningStaffRepository cleaningStaffRepository;
    private final PasswordEncoder passwordEncoder;

    // Costruttori
    @Autowired
    public CleaningStaffService(CleaningStaffRepository cleaningStaffRepository, PasswordEncoder passwordEncoder) {
        this.cleaningStaffRepository = cleaningStaffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo per la Registrazione del Cleaning Staff
    public void registerCleaningStaff(CleaningStaff cleaningStaff) {
        String password = cleaningStaff.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);

        cleaningStaff.setPassword(encryptedPassword);
        
        cleaningStaffRepository.save(cleaningStaff);
    }

}
