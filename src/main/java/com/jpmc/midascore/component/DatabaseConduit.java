package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.model.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void process(Transaction transaction) {
        Optional<UserRecord> senderOpt = userRepository.findById(Long.parseLong(transaction.getSender()));
        Optional<UserRecord> recipientOpt = userRepository.findById(Long.parseLong(transaction.getRecipient()));

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        float amount = transaction.getAmount();

        if (sender.getBalance() < amount) return;

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(sender, recipient, amount);
        transactionRecordRepository.save(record);

        System.out.println("Processed transaction from " + sender.getName() + " to " + recipient.getName() + " | Amount: " + amount);

        // Log Waldorf's balance if he is involved
        if ("waldorf".equalsIgnoreCase(sender.getName()) || "waldorf".equalsIgnoreCase(recipient.getName())) {
            UserRecord waldorf = "waldorf".equalsIgnoreCase(sender.getName()) ? sender : recipient;
            System.out.println("Waldorf's current balance: " + waldorf.getBalance());
        }
    }
}
