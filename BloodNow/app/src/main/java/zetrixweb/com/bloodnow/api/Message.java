package zetrixweb.com.bloodnow.api;

public class Message {
    String to;
    NotifyData notification;

    public Message(String to, NotifyData notification) {
        this.to = to;
        this.notification = notification;
    }

}