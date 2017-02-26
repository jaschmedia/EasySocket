package me.Jasch.EasySocket.MType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jasch
 */
public class MTypeTest {
    @Test
    public void testDiscriminators() {
        assertEquals("ACK", "ACK", MType.ACK.getDiscrminator());
        assertEquals("NAC", "NAC", MType.NAC.getDiscrminator());
        assertEquals("ATH", "ATH", MType.ATH.getDiscrminator());
        assertEquals("ERR", "ERR", MType.ERR.getDiscrminator());
        assertEquals("CIS", "CIS", MType.CIS.getDiscrminator());
        assertEquals("PRT", "PRT", MType.PRT.getDiscrminator());
        assertEquals("RTL", "RTL", MType.RTL.getDiscrminator());
        assertEquals("PNG", "PNG", MType.PNG.getDiscrminator());
        assertEquals("POG", "POG", MType.POG.getDiscrminator());
        assertEquals("EVT", "EVT", MType.EVT.getDiscrminator());
    }
}
