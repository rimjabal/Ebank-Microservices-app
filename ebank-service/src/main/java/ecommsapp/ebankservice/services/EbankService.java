package ecommsapp.ebankservice.services;

import ecommsapp.ebankservice.entities.BankAccount;
import ecommsapp.ebankservice.feign.CustomerRestClient;
import ecommsapp.ebankservice.model.Customer;
import ecommsapp.ebankservice.repository.BankAccountRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EbankService {
    private final BankAccountRepository accountRepository;
    private final CustomerRestClient customerRestClient;

    public EbankService(BankAccountRepository accountRepository, CustomerRestClient customerRestClient) {
        this.accountRepository = accountRepository;
        this.customerRestClient = customerRestClient;
    }

@McpTool(description = "get all bann accounts")
    public List<BankAccount> getAllBankAccounts() {

        return accountRepository.findAll();

    }
@McpTool(description = "Get a bank account by id")
    public BankAccount getBankAccountById(
            @McpToolParam(description = "the bank account id") String id) {
        BankAccount bankAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }
    @McpTool(description = "save a new bank account")
    public BankAccount save(
            @McpToolParam(description = "The bank account to save(balance,type,customerId)") BankAccount bankAccount) {
        try {
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return accountRepository.save(bankAccount);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }


}

}