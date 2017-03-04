package me.Jasch.EasySocket.Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jasch
 */
public class MTypeTest {
    @Test
    public void testDiscriminators() {
        assertEquals("ACK", "ACK", MType.ACK.discriminator);
        assertEquals("NAC", "NAC", MType.NAC.discriminator);
        assertEquals("ATH", "ATH", MType.ATH.discriminator);
        assertEquals("ERR", "ERR", MType.ERR.discriminator);
        assertEquals("CIS", "CIS", MType.CIS.discriminator);
        assertEquals("PRT", "PRT", MType.PRT.discriminator);
        assertEquals("RTL", "RTL", MType.RTL.discriminator);
        assertEquals("PNG", "PNG", MType.PNG.discriminator);
        assertEquals("POG", "POG", MType.POG.discriminator);
        assertEquals("EVT", "EVT", MType.EVT.discriminator);
    }
}
