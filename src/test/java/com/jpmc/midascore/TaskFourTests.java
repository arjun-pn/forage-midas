package com.jpmc.midascore;

import com.jpmc.midascore.KafkaProducer;
import com.jpmc.midascore.FileLoader;
import com.jpmc.midascore.UserPopulator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
})
public class TaskFourTests {

    private static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_four_verifier() throws InterruptedException {
        // Step 1: Populate test users
        userPopulator.populate();

        // Step 2: Load all transactions from test file
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");

        // Step 3: Send each transaction to Kafka topic
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Step 4: Wait for processing
        Thread.sleep(3000);

        // Step 5: Notify developer to check Wilbur's balance
        logger.info("----------------------------------------------------------");
        logger.info("✅ All transactions sent. Use debugger or console to inspect Wilbur's final balance.");
        logger.info("----------------------------------------------------------");

        // Infinite loop for debugging
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}
