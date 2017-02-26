package me.Jasch.EasySocket.MType;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import static java.lang.Math.random;
import static org.junit.Assert.assertEquals;

/**
 * @author jasch
 */
public class MTypeUtilsTest {
    @Test
    public void getMTypeTest() throws InvalidMTypeException {
        assertEquals("ACK", MType.ACK, MTypeUtils.getMType("ACK"));
        assertEquals("NAC", MType.NAC, MTypeUtils.getMType("NAC"));
        assertEquals("ATH", MType.ATH, MTypeUtils.getMType("ATH"));
        assertEquals("ERR", MType.ERR, MTypeUtils.getMType("ERR"));
        assertEquals("CIS", MType.CIS, MTypeUtils.getMType("CIS"));
        assertEquals("PRT", MType.PRT, MTypeUtils.getMType("PRT"));
        assertEquals("RTL", MType.RTL, MTypeUtils.getMType("RTL"));
        assertEquals("PNG", MType.PNG, MTypeUtils.getMType("PNG"));
        assertEquals("POG", MType.POG, MTypeUtils.getMType("POG"));
        assertEquals("EVT", MType.EVT, MTypeUtils.getMType("EVT"));
    }

    @Test
    public void errorMessageGenerators() {
        for (int i=1; i<20; i++) {
            String reason = RandomStringUtils.randomAlphanumeric(i);
            assertEquals("Error test", "ERR" + reason, MTypeUtils.error(reason));
        }

        String cID = "Aknea9sa";
        for (int i=1; i<20; i++) {
            String reason = RandomStringUtils.randomAlphanumeric(i);
            assertEquals("Error test", "ERR" + cID + reason, MTypeUtils.error(reason, cID));
        }
    }
}
