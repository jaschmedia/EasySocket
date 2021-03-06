package me.Jasch.EasySocket.Message;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import me.Jasch.EasySocket.Exceptions.NoConnectionIDException;
import me.Jasch.EasySocket.Exceptions.UnknownConnectionIDException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;


/**
 * Test the message object.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class MessageTest {

    private int numTest = 10; // number of tests per MType.

    /**
     * Test the creation of messages without a payload.
     * @throws UnknownConnectionIDException This is expected to happen.
     */
    @Test(expected= UnknownConnectionIDException.class)
    public void createFromMtCid() throws UnknownConnectionIDException {
        String cID;
        String expect;
        for (MType mt : MType.values()) {
            for (int i = 0; i < numTest; i++) {
                cID = RandomStringUtils.randomAlphanumeric(8);
                expect = mt.discriminator + cID;
                assertEquals(expect, new Message(mt, cID).toString());
            }
        }
    }

    /**
     * Test the creation of messages with a payload.
     * @throws UnknownConnectionIDException This is expected to happen.
     */
    @Test(expected = UnknownConnectionIDException.class)
    public void createFromMtCidPl() throws UnknownConnectionIDException {
        String cID;
        String payload;
        String expect;
        Random gen = new Random();
        for (MType mt : MType.values()) {
            for (int i = 0; i < numTest; i++) {
                cID = RandomStringUtils.randomAlphanumeric(8);
                payload = RandomStringUtils.random(gen.nextInt(100),
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                "0123456789!\"§$%&/()=?`'+*~#'-_.:,;<>^{[]}\\");
                expect = mt.discriminator + cID + payload;
                assertEquals(expect, new Message(mt, cID, payload).toString());
            }
        }
    }

    /**
     * Test the creation of messages from a string.
     * @throws InvalidMTypeException Possible exception.
     * @throws NoConnectionIDException Possible exception.
     * @throws UnknownConnectionIDException This is expected to happen.
     */
    @Test(expected = UnknownConnectionIDException.class)
    public void createFromString() throws InvalidMTypeException, NoConnectionIDException, UnknownConnectionIDException {
        String cID;
        String payload;
        String expect;
        Random gen = new Random();
        for (MType mt : MType.values()) {
            for (int i = 0; i < numTest; i++) {
                cID = RandomStringUtils.randomAlphanumeric(8);
                payload = RandomStringUtils.random(gen.nextInt(100),
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                "0123456789!\"§$%&/()=?`'+*~#'-_.:,;<>^{[]}\\");
                expect = mt.discriminator + cID + payload;
                assertEquals(expect, new Message(expect).toString());
            }
        }
    }

}
