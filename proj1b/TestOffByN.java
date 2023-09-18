import org.junit.Test;
import static org.junit.Assert.*;
public class TestOffByN {
    @Test
    public void testOffByN() {
        OffByN odn = new OffByN(2);
        assertTrue(odn.equalChars('a', 'c'));
        assertFalse(odn.equalChars('a', 'a'));
        assertFalse(odn.equalChars('a', 'b'));
    }
}
