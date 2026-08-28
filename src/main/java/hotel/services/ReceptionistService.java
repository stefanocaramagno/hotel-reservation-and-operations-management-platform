package hotel.services;

import hotel.models.Receptionist;
import hotel.repositories.ReceptionistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ReceptionistService {

    // Attributi
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder passwordEncoder;

    // Costruttori
    @Autowired
    public ReceptionistService(ReceptionistRepository receptionistRepository, PasswordEncoder passwordEncoder) {
        this.receptionistRepository = receptionistRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo per la Registrazione del Receptionist
    public void registerReceptionist(Receptionist receptionist) {
        String password = receptionist.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);

        receptionist.setPassword(encryptedPassword);
        
        receptionistRepository.save(receptionist);
    }
    
}
