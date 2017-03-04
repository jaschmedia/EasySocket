package me.Jasch.EasySocket.WebSocket;

import org.junit.Test;

import java.util.HashMap;

import static me.Jasch.EasySocket.WebSocket.WSUtils.generateConnectionId;
import static org.junit.Assert.assertTrue;

/**
 * @author jasch
 */
public class WSUtilsTest {

    private boolean isAlphanumeric(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }
        return true;
    }

    @Test
    public void testConnectionIdGeneration() {
        HashMap<String, Connection> conns = new HashMap<>();
        String testStr = generateConnectionId(conns);
        assertTrue("cID length", testStr.length() == 8);
        assertTrue("cID composition", isAlphanumeric(testStr));
    }
}
