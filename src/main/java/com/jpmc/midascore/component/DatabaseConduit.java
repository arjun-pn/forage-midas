package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.model.Incentive;
import com.jpmc.midascore.model.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    public DatabaseConduit(UserRepository userRepository,
                           TransactionRecordRepository transactionRecordRepository,
                           RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    public void process(Transaction transaction) {
        Optional<UserRecord> senderOpt = userRepository.findById(Long.parseLong(transaction.getSender()));
        Optional<UserRecord> recipientOpt = userRepository.findById(Long.parseLong(transaction.getRecipient()));

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        float amount = transaction.getAmount();

        if (sender.getBalance() < amount) return;

        // 🟢 Incentive API call is skipped/commented
        // Incentive incentive = restTemplate.postForObject(
        //        "http://localhost:8080/incentive", transaction, Incentive.class);
        // float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0;

        float incentiveAmount = 0;

        // 💰 Update balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        // 📝 Save transaction record with incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentiveAmount);
        transactionRecordRepository.save(record);

        // ✅ Log processed transaction
        System.out.println("Processed transaction from " + sender.getName() + " to " + recipient.getName()
                + " | Amount: " + amount + " | Incentive: " + incentiveAmount);

        // ✅ Print Wilbur's balance if involved in this transaction
        if (sender.getName().equals("Wilbur") || recipient.getName().equals("Wilbur")) {
            float wilburBalance = sender.getName().equals("Wilbur") ? sender.getBalance() : recipient.getBalance();
            System.out.println("✅ Wilbur's final balance: " + wilburBalance);
        }
    }
}

