package org.wso2.samples.jms;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class ClientHelper {
    /**
     * Full qualified class name of the andes initial context factory
     */
    public static final String ANDES_INITIAL_CONTEXT_FACTORY = "org.wso2.andes.jndi" +
            ".PropertiesFileInitialContextFactory";

    /**
     * Connection factory name used
     */
    public static final String CONNECTION_FACTORY = "ConnectionFactory";

    /**
     * XA connection factory name used.
     */

    public static InitialContextBuilder getInitialContextBuilder(String username,
                                                                 String password,
                                                                 String brokerHost,
                                                                 String port) {
        return new InitialContextBuilder(username, password, brokerHost, port);
    }

    public static class InitialContextBuilder {

        public static final String CONNECTION_FACTORY_PREFIX = "connectionfactory.";
        private final Properties contextProperties;
        private final String username;
        private final String password;
        private final String brokerHost;
        private final String port;

        public InitialContextBuilder(String username, String password, String brokerHost, String port) {
            this.username = username;
            this.password = password;
            this.brokerHost = brokerHost;
            this.port = port;
            contextProperties = new Properties();
            contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_INITIAL_CONTEXT_FACTORY);
            String connectionString = getBrokerConnectionString();
            contextProperties.put(CONNECTION_FACTORY_PREFIX + CONNECTION_FACTORY, connectionString);
        }

        public InitialContextBuilder withQueue(String queueName) {
            contextProperties.put("queue." + queueName, queueName);
            return this;
        }

        public InitialContext build() throws NamingException {
            return new InitialContext(contextProperties);
        }

        private String getBrokerConnectionString() {
            return "amqp://" + username + ":" + password + "@clientID/carbon?brokerlist='tcp://"
                    + brokerHost + ":" + port + "'";
        }
    }

}

