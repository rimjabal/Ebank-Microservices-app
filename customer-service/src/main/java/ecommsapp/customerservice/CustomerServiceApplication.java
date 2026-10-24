package ecommsapp.customerservice;

import ecommsapp.customerservice.entities.Customer;
import ecommsapp.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            List.of("Marouane", "Rim", "Yassine").forEach(name -> {
                Customer customer = Customer.builder()
                        .name(name)
                        .email(name.toLowerCase() + "@gmail.com")
                        .build();
                customerRepository.save(customer);
            });
        };
    }
}