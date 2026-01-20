package logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BitBoard91Test {

    @Test
    public void indexTest(){
        BitBoard91 bb = new BitBoard91(4,16,67,77);
        Assertions.assertEquals(4, bb.getIndexAfter(2));
        Assertions.assertEquals(16, bb.getIndexAfter(4));
        Assertions.assertEquals(67, bb.getIndexAfter(16));
        Assertions.assertEquals(-1, bb.getIndexAfter(77));
    }

    @Test
    public void indexTest2(){
        BitBoard91 bb = new BitBoard91(63,77,90, 91);
        Assertions.assertEquals(63, bb.getIndexAfter(2));
        Assertions.assertEquals(77, bb.getIndexAfter(63));
        Assertions.assertEquals(63, bb.getIndexAfter(17));
        Assertions.assertEquals(90, bb.getIndexAfter(77));
        Assertions.assertEquals(-1, bb.getIndexAfter(91));
    }
}
