package me.Jasch.EasySocket.Message;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jasch
 */
public class MessageUtilsTest {
    @Test
    public void getMTypeTest() throws InvalidMTypeException {
        assertEquals("ACK", MType.ACK, MessageUtils.getMType("ACK"));
        assertEquals("NAC", MType.NAC, MessageUtils.getMType("NAC"));
        assertEquals("ATH", MType.ATH, MessageUtils.getMType("ATH"));
        assertEquals("ERR", MType.ERR, MessageUtils.getMType("ERR"));
        assertEquals("CIS", MType.CIS, MessageUtils.getMType("CIS"));
        assertEquals("PRT", MType.PRT, MessageUtils.getMType("PRT"));
        assertEquals("RTL", MType.RTL, MessageUtils.getMType("RTL"));
        assertEquals("PNG", MType.PNG, MessageUtils.getMType("PNG"));
        assertEquals("POG", MType.POG, MessageUtils.getMType("POG"));
        assertEquals("EVT", MType.EVT, MessageUtils.getMType("EVT"));
    }

    @Test
    public void errorMessageGenerators() {
        for (int i=1; i<20; i++) {
            String reason = RandomStringUtils.randomAlphanumeric(i);
            assertEquals("Error test", "ERR" + reason, MessageUtils.error(reason));
        }

        String cID = "Aknea9sa";
        for (int i=1; i<20; i++) {
            String reason = RandomStringUtils.randomAlphanumeric(i);
            assertEquals("Error test", "ERR" + cID + reason, MessageUtils.error(reason, cID));
        }
    }
}
