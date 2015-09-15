package org.thedevpiece.jms.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
@ApplicationScoped
public class JmsContextHelper {
    @Inject
    @JmsContext
    private InitialContext initialContext;

    public <T> T lookup(String jndi){
        try {
            return (T) initialContext.lookup(jndi);
        } catch (NamingException e) {
            Utils.doThrow(e);
            return null;
        }
    }
}
