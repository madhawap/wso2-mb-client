package org.wso2.samples.jms;

import org.apache.log4j.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * WSO2 MB Queue Publisher
 */

public class Main {

    private static final String QUEUE_NAME = "mapdataqueue";
    static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
        // Step 1. Create an initial context to perform the JNDI lookup.
        InitialContext initialContext = ClientHelper.getInitialContextBuilder("admin", "admin", "localhost",
                "5675").withQueue(QUEUE_NAME).build();

        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(ClientHelper
                .CONNECTION_FACTORY);

        // Step 2. Create a JMS Connection
        Connection connection = connectionFactory.createConnection();

        // Step 3. Start the Connection
        connection.start();

        // Step 4. Create a JMS Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Step 5. perform a lookup on the Queue
        Queue queue = (Queue) initialContext.lookup(QUEUE_NAME);

        // Step 6. Create a JMS Message Producer
        MessageProducer messageProducer = session.createProducer(queue);

        // Step 7. Sample queue message
        TextMessage textMessage = session.createTextMessage("Hello this is from Madhawa Perera");

        // Step 8. Publishing the message to the queue
        messageProducer.send(textMessage);

        // Step 9. Close JMS Message Producer
        messageProducer.close();

        // Step 10. Close JMS resources
        connection.close();

        // Step 11. Also the initialContext
        initialContext.close();

        log.info("Message publisher closed");

        Thread.sleep(1000);
        System.exit(0);
    }
}
