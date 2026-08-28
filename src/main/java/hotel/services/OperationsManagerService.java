package hotel.services;

import hotel.models.OperationsManager;
import hotel.repositories.OperationsManagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OperationsManagerService {

    // Attributi
    private final OperationsManagerRepository operationsManagerRepository;
    private final PasswordEncoder passwordEncoder;

    // Costruttori
    @Autowired
    public OperationsManagerService(OperationsManagerRepository operationsManagerRepository, PasswordEncoder passwordEncoder) {
        this.operationsManagerRepository = operationsManagerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo per la Registrazione dell'Operations Manager
    public void registerOperationsManager(OperationsManager operationsManager) {
        String password = operationsManager.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);

        operationsManager.setPassword(encryptedPassword);
        
        operationsManagerRepository.save(operationsManager);
    }
    
}
