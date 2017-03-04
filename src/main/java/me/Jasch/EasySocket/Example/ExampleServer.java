package me.Jasch.EasySocket.Example;

import me.Jasch.EasySocket.EasySocket;
import me.Jasch.EasySocket.Event.IEvent;
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
}

class TestEvent implements IEvent {

    @Override
    public void handleEvent(Message msg) {
        System.out.println(msg.payload);
    }

    @Override
    public String stringifyPayload(Object payload) {
        return payload.toString();
    }
}