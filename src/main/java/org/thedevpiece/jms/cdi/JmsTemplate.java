package org.thedevpiece.jms.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;

import java.io.Serializable;
import java.util.Map;

import static org.thedevpiece.jms.cdi.Utils.doThrow;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
@ApplicationScoped
public class JmsTemplate {
    @Inject
    private JmsContextHelper contextHelper;

    @Inject
    @JmsContext
    private ConnectionFactory connectionFactory;

    public void send(String destination, String message){
        Destination d = contextHelper.lookup(destination);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            final TextMessage jmsMessage = session.createTextMessage(message);
            producer = send(d, session, producer, jmsMessage);
        } catch (JMSException e) {
            rollback(session, e);
        } finally {
            close(connection, session, producer);
        }
    }

    public void send(String destination, Serializable message){
        Destination d = contextHelper.lookup(destination);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            final ObjectMessage jmsMessage = session.createObjectMessage(message);
            producer = send(d, session, producer, jmsMessage);
        } catch (JMSException e) {
            rollback(session, e);
        } finally {
            close(connection, session, producer);
        }
    }

    public void send(String destination, byte [] message){
        Destination d = contextHelper.lookup(destination);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            final BytesMessage jmsMessage = session.createBytesMessage();
            jmsMessage.writeBytes(message);
            producer = send(d, session, producer, jmsMessage);
        } catch (JMSException e) {
            rollback(session, e);
        } finally {
            close(connection, session, producer);
        }
    }


    public void send(String destination, Map<String, Object> message){
        Destination d = contextHelper.lookup(destination);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            final MapMessage jmsMessage = session.createMapMessage();

            for (Map.Entry<String, Object> entry : message.entrySet()) {
                jmsMessage.setObject(entry.getKey(), entry.getValue());
            }

            producer = send(d, session, producer, jmsMessage);
        } catch (JMSException e) {
            rollback(session, e);
        } finally {
            close(connection, session, producer);
        }
    }

    public void send(String destination, Long message){
        send(destination, message);
    }

    public void send(String destination, Integer message){
        send(destination, message);
    }

    private MessageProducer send(Destination d, Session session, MessageProducer producer, Message textMessage) throws JMSException {
        producer = session.createProducer(d);
        producer.send(textMessage);
        session.commit();
        return producer;
    }

    private void close(Connection connection, Session session, MessageProducer producer) {
        if(connection != null){
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                Utils.doThrow(e);
            }
        }

        if(session != null){
            try {
                session.close();
            } catch (JMSException e) {
                Utils.doThrow(e);
            }
        }

        if(producer != null){
            try {
                producer.close();
            } catch (JMSException e) {
                Utils.doThrow(e);
            }
        }
    }

    private void rollback(Session session, JMSException e) {
        doThrow(e);
        if(session != null) {
            try {
                session.rollback();
            } catch (JMSException e1) {
                Utils.doThrow(e);
            }
        }
    }
}
