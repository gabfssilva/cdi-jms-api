package org.thedevpiece.jms.cdi;

import org.apache.activemq.broker.BrokerService;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author Gabriel Francisco - gabfssilva@gmail.com
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({InitializeBeans.class, JmsContextHelper.class, ListenerRegister.class, JmsTemplate.class, TestJmsProducer.class, MyQueueListenerSample.class})
public class ListenerRegisterTest {
    @Inject
    private JmsTemplate jmsTemplate;

    @Inject
    private BrokerService brokerService;

    @Test
    public void sendMessageTest() throws InterruptedException {
        System.out.println(brokerService.toString());
        jmsTemplate.send("MyQueue", "message test");
        Thread.sleep(1000l);
    }
}