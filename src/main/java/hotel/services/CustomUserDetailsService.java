package hotel.services;

import hotel.models.CleaningStaff;
import hotel.models.Customer;
import hotel.models.OperationsManager;
import hotel.models.Receptionist;
import hotel.repositories.CustomerRepository;
import hotel.repositories.ReceptionistRepository;
import hotel.repositories.OperationsManagerRepository;
import hotel.repositories.CleaningStaffRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Attributi
    private final CustomerRepository customerRepository;
    private final ReceptionistRepository receptionistRepository;
    private final OperationsManagerRepository operationsManagerRepository;
    private final CleaningStaffRepository cleaningStaffRepository;

    // Costruttore
    public CustomUserDetailsService(
            CustomerRepository customerRepository,
            ReceptionistRepository receptionistRepository,
            OperationsManagerRepository operationsManagerRepository,
            CleaningStaffRepository cleaningStaffRepository) {
        this.customerRepository = customerRepository;
        this.receptionistRepository = receptionistRepository;
        this.operationsManagerRepository = operationsManagerRepository;
        this.cleaningStaffRepository = cleaningStaffRepository;
    }

    // Metodo per Caricare i dettagli dell'utente in base all'Email cercando nei repository con conseguente Assegnazione del loro Ruolo.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Cerca il Customer
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            addFullNameToAuthentication(customer.getFullName());
            return buildUserDetails(customer.getEmail(), customer.getPassword(), "ROLE_CUSTOMER");
        }

        // Cerca il Receptionist
        Receptionist receptionist = receptionistRepository.findByEmail(email);
        if (receptionist != null) {
            addFullNameToAuthentication(receptionist.getFullName());
            return buildUserDetails(receptionist.getEmail(), receptionist.getPassword(), "ROLE_RECEPTIONIST");
        }

        // Cerca il Cleaning Staff
        CleaningStaff cleaningStaff = cleaningStaffRepository.findByEmail(email);
        if (cleaningStaff != null) {
            addFullNameToAuthentication(cleaningStaff.getFullName());
            return buildUserDetails(cleaningStaff.getEmail(), cleaningStaff.getPassword(), "ROLE_CLEANING_STAFF");
        }

        // Cerca l'Operations Manager
        OperationsManager operationsManager = operationsManagerRepository.findByEmail(email);
        if (operationsManager != null) {
            addFullNameToAuthentication(operationsManager.getFullName());
            return buildUserDetails(operationsManager.getEmail(), operationsManager.getPassword(), "ROLE_OPERATIONS_MANAGER");
        }

        // Se non trova l'utente, lancia un'eccezione
        throw new UsernameNotFoundException("User with email '" + email + "' not found");
    }

    /**
     * Metodo per costruire un oggetto UserDetails
     * @param email L'email dell'utente
     * @param password La password dell'utente
     * @param role Il ruolo associato
     * @return Un'istanza di UserDetails
     */
    private UserDetails buildUserDetails(String email, String password, String role) {
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                List.of(new SimpleGrantedAuthority(role))
        );
    }

    /**
     * Aggiunge il fullName come dettaglio nel contesto di autenticazione.
     * @param fullName Nome completo dell'utente.
     */
    private void addFullNameToAuthentication(String fullName) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuth != null) {
            Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                    currentAuth.getPrincipal(),
                    currentAuth.getCredentials(),
                    currentAuth.getAuthorities()
            );

            ((UsernamePasswordAuthenticationToken) updatedAuth).setDetails(fullName);

            SecurityContextHolder.getContext().setAuthentication(updatedAuth);
        }
    }
    
}
