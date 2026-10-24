
package ecommsapp.ebankservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ecommsapp.ebankservice.model.Customer;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
    @Id
    private String id;
    private Date createdAt;
    private double balance;
    private String type; // CURRENT_ACCOUNT or SAVINGS_ACCOUNT
    private Long customerId;

    @Transient
    private Customer customer;
}