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
            //undo toggle
            currentBoard.toggle(allBlockers.getIndexAfter(i));
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
        temporaryArray.put(new BitBoard91(currentBoard),attackPattern);
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
            //undo toggle
            currentBoard.toggle(allBlockers.getIndexAfter(i));
        }else{
            //evaluate
            evaluateBishop(new BitBoard91(currentBoard),tempArray);
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
//        System.out.println("\nCurrent board and attack pattern for :" + currentCell);
//        System.out.println(currentBoard);
//        System.out.println(attackPattern);
        temp.put(currentBoard,attackPattern);
    }

    public static void main(String[] args){
        Random r = new Random(); lut = HexBoardLUT.getSingleton();
        HashMap<BitBoard91, BitBoard91> attackPatternsRookTemp;
        HashMap<Short, BitBoard91> attackPatternsRookFinal;
        long[][] magicNumbersRook;
        long startTime= System.currentTimeMillis();
        BitBoard91[][] rookMask = GameMasks.getSingleton().getRookBlockers();

        GameMasks attackPatterns = GameMasks.getSingleton();

        // ROOK
        int numLoopTEST=0;
        magicNumbersRook = new long[3][91];
        BitBoard91[][] rookBlockers = attackPatterns.getRookBlockers();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 91; i++) {  //algo: for every possible blocker combination -> find if random hash work
                attackPatternsRookTemp= new HashMap<>();
                BitBoard91 currentBlockers = new BitBoard91(rookBlockers[j][i]); currentAxis=j;currentCell=i;
                // find all possible blocker pattern and the corresponding attack pattern with recursion
                helperRook(0,currentBlockers,new BitBoard91(),attackPatternsRookTemp);
                System.out.print("initial size: " + attackPatternsRookTemp.size());
                // find the magic number in a while loop
                boolean found = false, go = true;
                attackPatternsRookFinal=new HashMap<>();
                long magic=0;
                numLoopTEST =0;
                while(!found){
                    numLoopTEST++;
                    go=true;
//                    System.out.print(attackPatternsRookTemp.size()+" - ");
                    magic = r.nextLong() & r.nextLong()& r.nextLong(); // generate a magic number candidate
                    for (BitBoard91 blockerPattern : attackPatternsRookTemp.keySet()){
                        short hashKey =(short) ((blockerPattern.getXOR() * magic)>>>(64-currentBlockers.bitCount()));
                        if(!attackPatternsRookFinal.containsKey(hashKey)){ // if not in final
                            attackPatternsRookFinal.put(hashKey,attackPatternsRookTemp.get(blockerPattern)); //put key in
                        }else if(!attackPatternsRookFinal.get(hashKey).equals(attackPatternsRookTemp.get(blockerPattern))){
                            // if it's not equal to the right attack pattern
                            go=false;
                            break;
                        }
                    }
//                    System.out.println(attackPatternsRookFinal.size());
                    if(go) found=true;
                    else attackPatternsRookFinal=new HashMap<>();
                    if(found) {
                        System.out.print("found in "+numLoopTEST);
                        System.out.println("== final size:"+attackPatternsRookFinal.size()+" - ");
                    }
                }
                magicNumbersRook[j][i]=magic;
            }
        }




        //BISHOP
        HashMap<BitBoard91,BitBoard91> attackPatternBishopTemp ;
        BitBoard91[] bishop= attackPatterns.getBishopBlockers();

        bishopMagicNumbers = new long[91];
        for (int i = 0; i < 91; i++) {
            currentCell=i;
            attackPatternBishopTemp= new HashMap<>();
            //find all blcokers combination with helper and populate attackpatternbishoptemp
            helperBishop(0, bishop[i], new BitBoard91(),attackPatternBishopTemp);
            //find magic number and populate
            boolean found = false,go;long magicCandidate=0;
            HashMap<Short, BitBoard91> hashTableBishop = new HashMap<Short, BitBoard91>();
            System.out.print("searching...initial size: "+attackPatternBishopTemp.size());
            numLoopTEST=0;
            while(!found){
                numLoopTEST++;
                go=true;
//                System.out.print(attackPatternBishopTemp.size()+" - ");
                magicCandidate = r.nextLong() & r.nextLong() & r.nextLong() ;
//                System.out.println(Long.bitCount(magicCandidate));
                for(BitBoard91 bp:attackPatternBishopTemp.keySet()){
                    short hashkey = (short)((bp.getXOR()*magicCandidate)>>>(64-bishop[i].bitCount()));
                    if(!hashTableBishop.containsKey(hashkey)){
                        hashTableBishop.put(hashkey, attackPatternBishopTemp.get(bp));
                    }else if(!hashTableBishop.get(hashkey).equals(attackPatternBishopTemp.get(bp))){
                        go=false;
                        break;
                    }
                }
                if(go) found = true;
                else hashTableBishop = new HashMap<Short, BitBoard91>();
                if(found) {
//                    System.out.print( hashTableBishop.size() + " ==");
                    System.out.print(" =>found in "+numLoopTEST);
                    System.out.println(" -- final: " +hashTableBishop.size() + " ==");
                }

            }
            bishopMagicNumbers[i]=magicCandidate;
        }

        System.out.println("algo done in: "+(System.currentTimeMillis()-startTime)+"ms");

    }
}
