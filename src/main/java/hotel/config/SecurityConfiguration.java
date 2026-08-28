package hotel.config;

import hotel.models.CleaningStaff;
import hotel.models.Customer;
import hotel.repositories.CustomerRepository;
import hotel.repositories.CleaningStaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityConfiguration {

    // Attributi
    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final CleaningStaffRepository cleaningStaffRepository;

    // Costruttore
    public SecurityConfiguration(CustomerRepository customerRepository, CleaningStaffRepository cleaningStaffRepository) {
        this.customerRepository = customerRepository;
        this.cleaningStaffRepository = cleaningStaffRepository;
    }

    // Bean per la Codifica delle Password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configurazione della Catena di Filtri di Sicurezza
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            
                // Aggiungi una Regola Esplicita per la Home
                .requestMatchers("/").permitAll()
                .requestMatchers("/home").permitAll()
                
                // Regole di Autorizzazione per i Ruoli
                .requestMatchers("/customer/customerAccess").permitAll()
                .requestMatchers("/api/hotelManagementSystem/registerCustomer").permitAll()
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
    
                .requestMatchers("/receptionist/receptionistAccess").permitAll()
                .requestMatchers("/api/hotelManagementSystem/registerReceptionist").permitAll()
                .requestMatchers("/receptionist/**").hasRole("RECEPTIONIST")
    
                .requestMatchers("/operationsManager/operationsManagerAccess").permitAll()
                .requestMatchers("/api/hotelManagementSystem/registerOperationsManager").permitAll()
                .requestMatchers("/operationsManager/**").hasRole("OPERATIONS_MANAGER")
    
                .requestMatchers("/cleaningStaff/cleaningStaffAccess").permitAll()
                .requestMatchers("/api/hotelManagementSystem/registerCleaningStaff").permitAll()
                .requestMatchers("/cleaningStaff/**").hasRole("CLEANING_STAFF")

                // Accesso Pubblico alle Risorse Statiche
                .requestMatchers("/static/**", "/app/**", "/dist/**", "/asset/**", "/component/**").permitAll()
    
                // Richiedi Autenticazione per tutte le altre Richieste
                .anyRequest().authenticated()
            )
    
            // Disabilita CSRF per richieste API REST
            .csrf(csrf -> csrf.disable()) 

            // Configura Login unico con Success Handler personalizzato
            .formLogin(form -> form
                .loginPage("/home")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/home?error=true")
                .permitAll()
            )

            // Configurazione per il Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            
            // Configurazione per la Gestione delle Sessioni
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
    
        return http.build();
    }

    // Definisce un bean che gestisce il reindirizzamento degli utenti dopo il login in base al loro ruolo
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {

            // URL di default per il reindirizzamento dopo il login
            String redirectUrl = "/home";

            // Ottiene il ruolo dell'utente autenticato
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            // Ottiene l'oggetto utente autenticato dalla sessione di Spring Security
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            // Inizializza il nome completo dell'utente con il valore predefinito "Guest"
            final String[] fullName = {"Guest"};

            // Verifica se il nome utente è disponibile
            if (user.getUsername() != null) {

                // Cerca un cliente nel database usando l'email dell'utente autenticato
                Customer customer = customerRepository.findByEmail(user.getUsername());
                if (customer != null) {

                    // Se il cliente esiste, aggiorna il nome completo
                    fullName[0] = customer.getFullName();
                }

                // Cerca un membro dello staff delle pulizie nel database usando l'email dell'utente autenticato
                CleaningStaff cleaningStaff = cleaningStaffRepository.findByEmail(user.getUsername());
                if (cleaningStaff != null) {

                    // Se il membro dello staff esiste, aggiorna il nome completo
                    fullName[0] = cleaningStaff.getFullName();
                }
            }

            // Crea un oggetto WebAuthenticationDetails personalizzato per memorizzare il nome completo dell'utente
            WebAuthenticationDetails details = new WebAuthenticationDetails(request) {
                @Override
                public String toString() {
                    return fullName[0]; // Restituisce il nome completo dell'utente come dettaglio dell'autenticazione
                }
            };

            // Crea un nuovo oggetto Authentication con le stesse credenziali ma aggiornato con i nuovi dettagli
            Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), // Mantiene il principale originale (utente autenticato)
                    authentication.getCredentials(), // Mantiene le credenziali originali (es. password)
                    authentication.getAuthorities() // Mantiene i ruoli dell'utente
            );

            // Imposta i dettagli personalizzati sull'oggetto Authentication aggiornato
            ((UsernamePasswordAuthenticationToken) updatedAuth).setDetails(details);

            // Aggiorna il contesto di sicurezza di Spring Security con il nuovo oggetto Authentication
            SecurityContextHolder.getContext().setAuthentication(updatedAuth);

            // Determina l'URL di reindirizzamento in base al ruolo dell'utente
            switch (role) {
                case "ROLE_CUSTOMER":
                    redirectUrl = "/customer/customerArea"; // Reindirizza l'utente con ruolo 'CUSTOMER'
                    break;
                case "ROLE_RECEPTIONIST":
                    redirectUrl = "/receptionist/receptionistArea"; // Reindirizza l'utente con ruolo 'RECEPTIONIST'
                    break;
                case "ROLE_OPERATIONS_MANAGER":
                    redirectUrl = "/operationsManager/operationsManagerArea"; // Reindirizza l'utente con ruolo 'OPERATIONS_MANAGER'
                    break;
                case "ROLE_CLEANING_STAFF":
                    redirectUrl = "/cleaningStaff/cleaningStaffArea"; // Reindirizza l'utente con ruolo 'CLEANING_STAFF'
                    break;
                default:
                    redirectUrl = "/home"; // Se il ruolo non è riconosciuto, reindirizza alla home
            }

            // Esegue il reindirizzamento dell'utente all'URL corrispondente al suo ruolo
            response.sendRedirect(redirectUrl);
        };
    }

}