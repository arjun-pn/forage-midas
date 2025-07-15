package com.jpmc.midascore.kafka;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private final DatabaseConduit databaseConduit;

    @Autowired
    public TransactionListener(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        databaseConduit.process(transaction); // <-- this updates balances and records to DB
    }
}
