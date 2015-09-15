package org.thedevpiece.jms.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.jms.JMSException;
import javax.naming.NamingException;

public class InitializeBeans {
    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object o, ListenerRegister listenerRegister) throws JMSException, NamingException {
        listenerRegister.register();
    }
}