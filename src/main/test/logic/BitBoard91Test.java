package logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BitBoard91Test {

    @Test
    public void indexTest(){
        BitBoard91 bb = new BitBoard91(4,16,67,77);
        Assertions.assertEquals(4, bb.getIndex(2));
        Assertions.assertEquals(16, bb.getIndex(4));
        Assertions.assertEquals(67, bb.getIndex(17));
        Assertions.assertEquals(-1, bb.getIndex(77));
    }

    @Test
    public void indexTest2(){
        BitBoard91 bb = new BitBoard91(63,77,90, 91);
        Assertions.assertEquals(63, bb.getIndex(2));
        Assertions.assertEquals(77, bb.getIndex(63));
        Assertions.assertEquals(67, bb.getIndex(17));
        Assertions.assertEquals(90, bb.getIndex(77));
        Assertions.assertEquals(-1, bb.getIndex(91));
    }
}
