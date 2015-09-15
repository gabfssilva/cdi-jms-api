# cdi-jms-api

This api helps you register listeners and send message using JMS, running on top of CDI.

####Configuration

```java
@Produces
@JmsContext
public InitialContext initialContext() {
    return new InitialContext(); //you need to create the InitialContext based on your JMS message broker.
}

@Produces
@JmsContext
public ConnectionFactory connectionFactory(@JmsContext InitialContext initialContext) throws NamingException {
   return (ConnectionFactory) initialContext.lookup("connectionFactory");
}
```


####Listener

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


####Sender

```java
 @Inject
private JmsTemplate jmsTemplate;

public void sendMessage() {
    jmsTemplate.send("MyQueue", "message test");
}
```
