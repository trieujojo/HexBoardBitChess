package logic;

import java.util.HashMap;
import java.util.Random;

public class MagicNumberFinder {
    private long[][] rookMagicNumbers;
    private static int currentAxis;
    private static int currentCell;
    private static HexBoardLUT lut;


    private static void helper(int i, BitBoard91 allBlockers, BitBoard91 currentBoard, HashMap<BitBoard91,BitBoard91> tempArray){
        if(allBlockers.getIndex(i)!=-1){
            //don't toggle
            helper(allBlockers.getIndex(i), allBlockers, currentBoard,tempArray);
            //toggle
            currentBoard.toggle(allBlockers.getIndex(i));
            helper(allBlockers.getIndex(i), allBlockers, currentBoard,tempArray);
        }else{
            //evaluate
            evaluate(currentBoard,tempArray);
        }
    }

    private static void evaluate(BitBoard91 currentBoard, HashMap<BitBoard91,BitBoard91> temporaryArray) {
        BitBoard91 attackPattern = new BitBoard91(currentCell);
        if(currentAxis==0){
            if(lut.getCoordinateFromIndex(currentCell))
        }else if(currentAxis==1){

        }else{

        }
    }


    public static void main(String[] args){
        Random r = new Random(); lut = HexBoardLUT.getSingleton();
        HashMap<BitBoard91, BitBoard91> attackPatternsRookTemp;
        long[][] magicNumbersRook;
        boolean searching=true, found=false;

        GameMasks attackPatterns = GameMasks.getSingleton();
        // ROOK
        magicNumbersRook = new long[3][91];
        BitBoard91[][] rookBlockers = attackPatterns.getRookBlockers();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 91; i++) {  //algo: for every possible blocker combination -> find if random hash work
                attackPatternsRookTemp= new HashMap<>();
                BitBoard91 current = new BitBoard91(rookBlockers[j][i]); currentAxis=j;currentCell=i;
                // find all possible blocker pattern and the corresponding attack pattern with recursion
                helper(0,current,new BitBoard91(),attackPatternsRookTemp);
                // find the magic number in a while loop
                BitBoard91 attackPattern = new BitBoard91(),blockerPattern = new BitBoard91();
                long magic = r.nextLong() & r.nextLong();
                for (int k = 0; k < rookBlockers[j][i].bitCount(); k++) {
                    //check attack pattern
                    blockerPattern.toggle(current.getFirstIndex());
                }
            }
        }


    }
}
