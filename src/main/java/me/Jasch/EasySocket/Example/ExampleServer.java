package me.Jasch.EasySocket.Example;

import me.Jasch.EasySocket.EasySocket;
import me.Jasch.EasySocket.Event.IEvent;
import me.Jasch.EasySocket.Exceptions.UnknownConnectionIDException;
import me.Jasch.EasySocket.Message.Message;

import java.net.InetSocketAddress;

/**
 * An example implementation on how to use EasySocket
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class ExampleServer {
    private static EasySocket es;

    public static void main(String[] args) {
        InetSocketAddress addr = new InetSocketAddress("localhost", 8000);
        es = new EasySocket(addr, "testabc");
        IEvent testEvent = new TestEvent();
        es.getHandler().registerEvent("test", testEvent);
        es.start();
    }

    public static void something(String cID) {
        Message toSend = null;
        try {
            toSend = es.getHandler().createEventMessage("test", cID, "FUCK YEAH!");
        } catch (UnknownConnectionIDException e) {
            // nothing here.
        }
        es.sendEvent(toSend);
    }
}

class TestEvent implements IEvent {

    @Override
    public void handleEvent(Message msg) {
        System.out.println(msg.payload);
        ExampleServer.something(msg.cID);
    }

    @Override
    public String stringifyPayload(Object payload) {
        return payload.toString();
    }
}