package logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class MagicNumberFinder {
    private long[][] rookMagicNumbers; //TODO use this one instead
    private static long[] bishopMagicNumbers;
    private static int currentAxis;
    private static int currentCell;
    private static HexBoardLUT lut;
    private static int tableInd;


    private static void helperRook(int i, BitBoard91 allCurrentBlockers, BitBoard91 currentBoard, BitBoard91[] allBlockers, BitBoard91[] allAttackers){
        if(allCurrentBlockers.getIndexAfter(i)!=-1){
            //don't toggle
            helperRook(allCurrentBlockers.getIndexAfter(i), allCurrentBlockers, currentBoard,allBlockers,allAttackers);
            //toggle
            currentBoard.toggle(allCurrentBlockers.getIndexAfter(i));
            helperRook(allCurrentBlockers.getIndexAfter(i), allCurrentBlockers, currentBoard,allBlockers,allAttackers);
            //undo toggle
            currentBoard.toggle(allCurrentBlockers.getIndexAfter(i));
        }else{
            //evaluate
            evaluateRook(currentBoard,allBlockers,allAttackers);
        }
    }

    private static void evaluateRook(BitBoard91 currentBoard, BitBoard91[] allBlockers, BitBoard91[] allAttackers) {
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
        allBlockers[tableInd]=new BitBoard91(currentBoard);
        allAttackers[tableInd++]=attackPattern;
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
        temp.put(currentBoard,attackPattern);
    }

    public static void main(String[] args){
        Random r = new Random(); lut = HexBoardLUT.getSingleton();
//        HashMap<BitBoard91, BitBoard91> attackPatternsRookTemp;
        BitBoard91[] allBlockers, allAttackers;

        long[][] magicNumbersRook;
        long startTime= System.currentTimeMillis();

        GameMasks attackPatterns = GameMasks.getSingleton();

        // ROOK

        magicNumbersRook = new long[3][91];
        BitBoard91[][] rookBlockers = attackPatterns.getRookBlockers();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 91; i++) {  //algo: for every possible blocker combination -> find if random hash work
                allBlockers=new BitBoard91[(int)Math.pow(2,rookBlockers[j][i].bitCount())];
                allAttackers=new BitBoard91[(int)Math.pow(2,rookBlockers[j][i].bitCount())];
                 currentAxis=j;currentCell=i;
                // find all possible blocker pattern and the corresponding attack pattern with recursion
                tableInd=0;// index to populate allblockers and all attackers
                helperRook(0,rookBlockers[j][i],new BitBoard91(),allBlockers,allAttackers);
//                System.out.println(rookBlockers[j][i);
                // find the magic number in a while loop
                boolean found = false;
                BitBoard91[] testTable = new BitBoard91[1024]; // Stores attack bitboards
                int[] usedAt = new int[1024];     // Stores which candidate filled this slot
                long magicCandidate=0;int success=0;
                int epochID =0;
                while(!found){
                    epochID++;
                    if(rookBlockers[j][i].bitCount()>=7) magicCandidate = r.nextLong() & r.nextLong()& r.nextLong();// generate a magic number candidate
                    else magicCandidate = r.nextLong() & r.nextLong() ;
                    boolean fail = false;
                    for (int k = 0; k < allBlockers.length; k++) {
                        int hashKey =(int) ((allBlockers[k].getXOR() * magicCandidate)>>>(65-rookBlockers[j][i].bitCount()));
//                        System.out.println(hashKey);
                        if(usedAt[hashKey]!=epochID){ // check if we are at the same epoch
                            usedAt[hashKey]=epochID; // slot is marked as used for this epoch
                            testTable[hashKey]=allAttackers[k];// put relevant attacker in this hash
                        }else if(!testTable[hashKey].equals(allAttackers[k])){ //wrong collision
                            // if it's not equal to the right attack pattern
//                            System.out.println("number of success out of 16:"+success);
                            success=0;
                            fail =true;
                            break;
                        }else success++;
                    }

                    if(success>0)
                        System.out.println("Number of success so far: "+success);
                    if(!fail) found=true;
                    if(found) {
                        System.out.println("found in "+epochID);
                        System.out.println(allBlockers.length);

                    }
                }
                magicNumbersRook[j][i]=magicCandidate;
            }
        }

        System.out.println("yayyyyyyyy");
/*

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
//            epochID=0;
            while(!found){
//                epochID++;
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
//                    System.out.print(" =>found in "+epochID);
                    System.out.println(" -- final: " +hashTableBishop.size() + " ==");
                }

            }
            bishopMagicNumbers[i]=magicCandidate;
        }
        System.out.println("algo done in: "+(System.currentTimeMillis()-startTime)+"ms");

 */
    }


//    public static void search(BitBoard91[][] rookBlockers,BitBoard91[] allAttackers, BitBoard91[] allBlockers, int j, int i, long r1,long r2, long r3, long r4){
//        boolean found = false;
//        BitBoard91[] testTable = new BitBoard91[1024]; // Stores attack bitboards
//        int[] usedAt = new int[1024];     // Stores which candidate filled this slot
//        int success=0;
//        int epochID =0;
//        while(!found){
//            epochID++;
//            boolean fail = false;
//            for (int k = 0; k < allBlockers.length; k++) {
//                int hashKey =(int) ((allBlockers[k].getXOR() * magicCandidate)>>>(65-rookBlockers[j][i].bitCount()));
////                        System.out.println(hashKey);
//                if(usedAt[hashKey]!=epochID){ // check if we are at the same epoch
//                    usedAt[hashKey]=epochID; // slot is marked as used for this epoch
//                    testTable[hashKey]=allAttackers[k];// put relevant attacker in this hash
//                }else if(!testTable[hashKey].equals(allAttackers[k])){ //wrong collision
//                    // if it's not equal to the right attack pattern
////                            System.out.println("number of success out of 16:"+success);
//                    success=0;
//                    fail =true;
//                }else success++;
//            }
//
//            if(success>0)
//                System.out.println("Number of success so far: "+success);
//            if(!fail) found=true;
//            if(found) {
//                System.out.println("found in "+epochID);
//                System.out.println(allBlockers.length);
//
//            }
//        }
//    }
}
