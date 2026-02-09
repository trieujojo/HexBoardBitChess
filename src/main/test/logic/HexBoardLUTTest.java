package logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HexBoardLUTTest {
    @Test
    public void cellConnectionPathTof11Test(){
        HexCell a1 = HexBoardLUT.getSingleton().getPosition("a1");
        HexCell f1 = HexBoardLUT.getSingleton().getPosition("f1");
        while(a1.getUr()!=null) a1=a1.getUr();
        while(f1.getN()!=null) f1=f1.getN();
        Assertions.assertEquals(a1,f1);

        while(a1.getDl()!=null) a1=a1.getDl();
        while(f1.getS()!=null) f1=f1.getS();
        Assertions.assertEquals(HexBoardLUT.getSingleton().getPosition("a1"),a1);
        Assertions.assertEquals(HexBoardLUT.getSingleton().getPosition("f1"),f1);
    }

    @Test
    public void cellConnectionPathTofa6Test(){
        HexCell l6 = HexBoardLUT.getSingleton().getPosition("l6");
        HexCell f11 = HexBoardLUT.getSingleton().getPosition("f11");
        while(l6.getL()!=null) l6=l6.getL();
        while(f11.getSw()!=null) f11=f11.getSw();
        Assertions.assertEquals(l6,f11);

        while(l6.getR()!=null) l6=l6.getR();
        while(f11.getNe()!=null) f11=f11.getNe();
        Assertions.assertEquals(HexBoardLUT.getSingleton().getPosition("l6"),l6);
        Assertions.assertEquals(HexBoardLUT.getSingleton().getPosition("f11"),f11);
    }
}
