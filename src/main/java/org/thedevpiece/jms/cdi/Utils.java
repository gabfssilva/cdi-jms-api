package org.thedevpiece.jms.cdi;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
public class Utils {
    public static void doThrow(Exception e) {
        Utils.<RuntimeException> doThrow0(e);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void doThrow0(Exception e) throws E {
        throw (E) e;
    }
}
