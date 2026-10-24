package ecommsapp.ebankservice;

import ecommsapp.ebankservice.entities.BankAccount;
import ecommsapp.ebankservice.repository.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository) {
        return args -> {
            for (long i = 1; i <= 3; i++) {
                for (int j = 1; j <= 2; j++) {
                    BankAccount account = BankAccount.builder()
                            .id(UUID.randomUUID().toString())
                            .balance(10000 + Math.random() * 50000)
                            .createdAt(new Date())
                            .type(Math.random() > 0.5 ? "CURRENT_ACCOUNT" : "SAVINGS_ACCOUNT")
                            .customerId(i)
                            .build();
                    bankAccountRepository.save(account);
                }
            }
        };
    }
}