package hotel.services;

import hotel.models.Customer;
import hotel.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    // Attributi
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    // Costruttori
    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo per la Registrazione del Customer
    public void registerCustomer(Customer customer) {
        String password = customer.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);

        customer.setPassword(encryptedPassword);
        
        customerRepository.save(customer);
    }

}
