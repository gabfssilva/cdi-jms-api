package org.thedevpiece.jms.cdi;

import org.apache.activemq.broker.BrokerService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
public class TestJmsProducer {

    @Produces
    @ApplicationScoped
    public BrokerService brokerService() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("fred");
        broker.addConnector("tcp://localhost:61616");
        broker.setVmConnectorURI(URI.create("vm://localhost"));
        broker.start();
        return broker;
    }

    @Produces
    @JmsContext
    public InitialContext initialContext() throws NamingException {
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put("java.naming.provider.url", "vm://localhost");
        properties.put("connectionFactoryNames", "connectionFactory, queueConnectionFactory, topicConnectionFactory");
        properties.put("queue.MyQueue", "queue.MyQueue");
        properties.put("topic.MyTopic", "topic.MyTopic");

        return new InitialContext(properties);
    }
    @Produces
    @JmsContext
    public ConnectionFactory connectionFactory(@JmsContext InitialContext initialContext) throws NamingException {
        return (ConnectionFactory) initialContext.lookup("connectionFactory");
    }
}
