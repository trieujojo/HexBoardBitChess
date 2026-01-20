package logic;

import java.util.HashMap;
import java.util.Random;

public class MagicNumberFinder {
    private long[][] rookMagicNumbers; //TODO use this one instead
    private static long[] bishopMagicNumbers;
    private static int currentAxis;
    private static int currentCell;
    private static HexBoardLUT lut;


    private static void helperRook(int i, BitBoard91 allBlockers, BitBoard91 currentBoard, HashMap<BitBoard91,BitBoard91> tempArray){
        if(allBlockers.getIndexAfter(i)!=-1){
            //don't toggle
            helperRook(allBlockers.getIndexAfter(i), allBlockers, currentBoard,tempArray);
            //toggle
            currentBoard.toggle(allBlockers.getIndexAfter(i));
            helperRook(allBlockers.getIndexAfter(i), allBlockers, currentBoard,tempArray);
        }else{
            //evaluate
            evaluateRook(currentBoard,tempArray);
        }
    }

    private static void evaluateRook(BitBoard91 currentBoard, HashMap<BitBoard91,BitBoard91> temporaryArray) {
        BitBoard91 attackPattern = new BitBoard91(); // will contain the attack pattern for current board config! includes attackable pieces too
        HexCell check = lut.getCellFromIndex(currentCell);
        if(currentAxis==0){ /// NORTH SOUTH
            HexCell n = check;
            n=n.getN();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getN();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());

            n = check;
            n=n.getS();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getS();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
        }else if(currentAxis==1){ // NE SW
            HexCell n = check;
            n=n.getNe();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getNe();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());

            n = check;
            n=n.getSw();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getSw();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
        }else{ // NW SE
            HexCell n = check;
            n=n.getNw();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getNw();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());

            n = check;
            n=n.getSe();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getSe();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
        }
        temporaryArray.put(currentBoard,attackPattern);
//        System.out.println("current board + attack pattern for :" + currentCell);
//        System.out.println(currentBoard);
//        System.out.println(attackPattern);
//        System.out.println();
    }



    private static void helperBishop(int i, BitBoard91 allBlockers, BitBoard91 currentBoard, HashMap<BitBoard91,BitBoard91> tempArray){
        if(allBlockers.getIndexAfter(i)!=-1){
            //don't toggle
            helperBishop(allBlockers.getIndexAfter(i), allBlockers, currentBoard,tempArray);
            //toggle
            currentBoard.toggle(allBlockers.getIndexAfter(i));
            helperBishop(allBlockers.getIndexAfter(i), allBlockers, currentBoard,tempArray);
        }else{
            //evaluate
            evaluateBishop(currentBoard,tempArray);
        }
    }

    public static void evaluateBishop(BitBoard91 currentBoard, HashMap<BitBoard91,BitBoard91> temp){
        BitBoard91 attackPattern = new BitBoard91(); // will contain the attack pattern for current board config! includes attackable pieces too
        HexCell check = lut.getCellFromIndex(currentCell);
        HexCell n = check;
        n=n.getUr();
        while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
            attackPattern.toggle(n.getIndex());
            n=n.getUr();
        }
        if(n!=null) attackPattern.toggle(n.getIndex());

        n=check.getDr();
        while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
            attackPattern.toggle(n.getIndex());
            n=n.getDr();
        }
        if(n!=null) attackPattern.toggle(n.getIndex());

        n=check.getR();
        while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
            attackPattern.toggle(n.getIndex());
            n=n.getR();
        }
        if(n!=null) attackPattern.toggle(n.getIndex());

        n=check.getDl();
        while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
            attackPattern.toggle(n.getIndex());
            n=n.getDl();
        }
        if(n!=null) attackPattern.toggle(n.getIndex());

        n=check.getUl();
        while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
            attackPattern.toggle(n.getIndex());
            n=n.getUl();
        }
        if(n!=null) attackPattern.toggle(n.getIndex());

        n=check.getL();
        while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
            attackPattern.toggle(n.getIndex());
            n=n.getL();
        }
        if(n!=null) attackPattern.toggle(n.getIndex());

        temp.put(currentBoard,attackPattern);
    }

    public static void main(String[] args){
        Random r = new Random(); lut = HexBoardLUT.getSingleton();
        HashMap<BitBoard91, BitBoard91> attackPatternsRookTemp;
        HashMap<Long, BitBoard91> attackPatternsRookFinal;
        long[][] magicNumbersRook;
        BitBoard91[][] rookMask = GameMasks.getSingleton().getRookBlockers();

        GameMasks attackPatterns = GameMasks.getSingleton();
        int max = 0;
        for (int i = 0; i < 3; i++) {
            for(BitBoard91 bb : GameMasks.getSingleton().getRookBlockers()[i])
                if(bb.bitCount()>max) max = bb.bitCount();
        }
        System.out.println(max);
        max=0;
        for(BitBoard91 bb: GameMasks.getSingleton().getBishopBlockers())
            if(bb.bitCount()>max) max= bb.bitCount();
        System.out.println(max);

        // ROOK
        /*
        magicNumbersRook = new long[3][91];
        BitBoard91[][] rookBlockers = attackPatterns.getRookBlockers();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 91; i++) {  //algo: for every possible blocker combination -> find if random hash work
                attackPatternsRookTemp= new HashMap<>();
                BitBoard91 current = new BitBoard91(rookBlockers[j][i]); currentAxis=j;currentCell=i;
                // find all possible blocker pattern and the corresponding attack pattern with recursion
                helperRook(0,current,new BitBoard91(),attackPatternsRookTemp);
                // find the magic number in a while loop
                boolean found = false, go = true;
                attackPatternsRookFinal=new HashMap<>();
                long magic=0;
                while(!found){
                    magic = r.nextLong() & r.nextLong();
                    for (BitBoard91 blockerPattern : attackPatternsRookTemp.keySet()){
                        long hashKey = ((blockerPattern).getXOR()) * magic; // TODO wrong rookMask?
                        if(!attackPatternsRookFinal.containsKey(hashKey)){//TODO use short instead of long
                            attackPatternsRookFinal.put(hashKey,attackPatternsRookTemp.get(blockerPattern));
                        }else if(!attackPatternsRookFinal.get(hashKey).equals(attackPatternsRookTemp.get(blockerPattern))){
                            go=false;
                            break;
                        }
                    }
                    if(go) found=true;
                }
                magicNumbersRook[j][i]=magic;
            }
        }

         */


        //BISHOP
        HashMap<BitBoard91,BitBoard91> attackPatternBishopTemp = new HashMap<>();
        BitBoard91[] bishop= attackPatterns.getBishopBlockers();
        for (int i = 0; i < 91; i++) {
            currentCell=i; bishopMagicNumbers = new long[91];
            //find all blcokers combination with helper and populate attackpatternbishoptemp
            helperBishop(0, bishop[i], new BitBoard91(),attackPatternBishopTemp);
            //find magic number and populate
            boolean found = false, go = true;
            HashMap<Short, BitBoard91> hashTableBishop = new HashMap<Short, BitBoard91>();
            System.out.println("searching...");
            while(!found){
                go=true;
                long magicCandidate = r.nextLong() & r.nextLong() & r.nextLong() & r.nextLong();
//                System.out.println(Long.bitCount(magicCandidate));
                for(BitBoard91 bp:attackPatternBishopTemp.keySet()){
                    short hashkey = (short)(bp.getXOR()*magicCandidate>>>64-12);
                    if(!hashTableBishop.containsKey(hashkey)){
                        hashTableBishop.put(hashkey, attackPatternBishopTemp.get(bp));
                    }else if(hashTableBishop.get(hashkey).equals(attackPatternBishopTemp.get(bp))){
                        go=false;
                        break;
                    }
                }
                if(go) found = true;
                if(found) System.out.println("found one");
            }
        }

        System.out.println();

    }
}
