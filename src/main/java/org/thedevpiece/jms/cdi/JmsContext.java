package org.thedevpiece.jms.cdi;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface JmsContext {
}
