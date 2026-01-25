package logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class MagicWriterTest {
    @Test
    public void writingReadingTest(){
        long[] bmn = new long[91];
        for (int i = 0; i < 91; i++) {
            bmn[i]=91-i;
        }
//        MagicWriter.writeLongArrayBishop(bmn);
        long[] result = MagicWriter.readLongArrayBishop();
        for (int i = 0; i < 91; i++) {
            Assertions.assertEquals(bmn[i],result[i]);
        }
    }
}
