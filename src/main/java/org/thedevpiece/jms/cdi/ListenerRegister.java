package org.thedevpiece.jms.cdi;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
@ApplicationScoped
public class ListenerRegister {
    @Inject
    @JmsListener(destination = "")
    private Instance<MessageListener> messageListeners;

    @Inject
    @Any
    private Instance<ExceptionListener> exceptionListeners;

    @Inject
    @JmsContext
    private ConnectionFactory connectionFactory;

    @Inject
    @JmsContext
    private InitialContext initialContext;

    public void register() throws NamingException, JMSException {
        for (MessageListener messageListener : messageListeners) {
            JmsListener jmsListener = messageListener.getClass().getAnnotation(JmsListener.class);

            if(jmsListener == null){
                jmsListener = messageListener.getClass().getSuperclass().getAnnotation(JmsListener.class);
            }

            final Destination destination = (Destination) initialContext.lookup(jmsListener.destination());

            Connection connection =  connectionFactory.createConnection();

            if(!jmsListener.exceptionListener().equals(ExceptionListener.class)){
                connection.setExceptionListener(exceptionListeners.select(jmsListener.exceptionListener()).get());
            }

            final Session session = connection.createSession(false, jmsListener.session().value);
            final MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(messageListener);

            connection.start();
        }
    }

    @Override
    public String toString() {
        return "ListenerRegister{" +
                "messageListeners=" + messageListeners +
                ", exceptionListeners=" + exceptionListeners +
                ", connectionFactory=" + connectionFactory +
                ", initialContext=" + initialContext +
                '}';
    }
}
