# cdi-jms-api

This api helps you register listeners and send message using JMS, running on top of CDI.

```java
@ApplicationScoped
@JmsListener(destination = "MyQueue")
public class MyQueueListenerSample implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
```

```java
 @Inject
private JmsTemplate jmsTemplate;

public void sendMessage() {
    jmsTemplate.send("MyQueue", "message test");
}
```
