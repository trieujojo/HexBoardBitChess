package logic;

import java.util.*;

public class MagicNumberFinder {
    private static long[][] magicNumbersRook; //TODO use this one instead
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
            HexCell n = check.getN();
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
            HexCell n = check.getNe();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getNe();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
            n = check.getSw();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getSw();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
        }else{ // NW SE
            HexCell n = check.getNw();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getNw();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
            n = check.getSe();
            while(n!=null&&!currentBoard.getIndexBit(n.getIndex())){
                attackPattern.toggle(n.getIndex());
                n=n.getSe();
            }
            if(n!=null) attackPattern.toggle(n.getIndex());
        }
        allBlockers[tableInd]=new BitBoard91(currentBoard);
        allAttackers[tableInd++]=attackPattern;
    }



    private static void helperBishop(int i, BitBoard91 allBlockers, BitBoard91 currentBoard, BitBoard91[] allBlockersB, BitBoard91[] allAttackersB){
        if(allBlockers.getIndexAfter(i)!=-1){
            //don't toggle
            helperBishop(allBlockers.getIndexAfter(i), allBlockers, currentBoard,allBlockersB,allAttackersB);
            //toggle
            currentBoard.toggle(allBlockers.getIndexAfter(i));
            helperBishop(allBlockers.getIndexAfter(i), allBlockers, currentBoard,allBlockersB,allAttackersB);
            //undo toggle
            currentBoard.toggle(allBlockers.getIndexAfter(i));
        }else{
            //evaluate
            evaluateBishop(new BitBoard91(currentBoard),allBlockersB,allAttackersB);
        }
    }

    public static void evaluateBishop(BitBoard91 currentBoard, BitBoard91[] allBlockersB, BitBoard91[] allAttackersB){
        BitBoard91 attackPattern = new BitBoard91(); // will contain the attack pattern for current board config! includes attackable pieces too
        HexCell check = lut.getCellFromIndex(currentCell);
        HexCell n = check.getUr();
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
        allBlockersB[tableInd]= currentBoard;
        allAttackersB[tableInd++]= attackPattern;

//        temp.put(currentBoard,attackPattern);
    }

    public static void main(String[] args){
        Random r = new Random(); lut = HexBoardLUT.getSingleton();
        BitBoard91[] allBlockers, allAttackers;
        long startTime= System.currentTimeMillis();
        GameMasks attackPatterns = GameMasks.getSingleton();

        // ROOK
        int skip=0;
        magicNumbersRook = new long[3][91];
        BitBoard91[][] rookBlockers = attackPatterns.getRookBlockers();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 91; i++) {  //algo: for every possible blocker combination -> find if random hash work
                skip=0;
                allBlockers=new BitBoard91[(int)Math.pow(2,rookBlockers[j][i].bitCount())];
                allAttackers=new BitBoard91[(int)Math.pow(2,rookBlockers[j][i].bitCount())];
                 currentAxis=j;currentCell=i;
                // find all possible blocker pattern and the corresponding attack pattern with recursion
                tableInd=0;// index to populate allblockers and all attackers
                helperRook(0,rookBlockers[j][i],new BitBoard91(),allBlockers,allAttackers);
                // find the magic number in a while loop
                boolean found = false;
                BitBoard91[] testTable = new BitBoard91[1024]; // Stores attack bitboards
                int[] usedAt = new int[1024];     // Stores which candidate filled this slot
                long magicCandidate=0;
                int epochID =0;
                while(!found){
                    epochID++;
                    if(rookBlockers[j][i].bitCount()>=7) magicCandidate = r.nextLong() & r.nextLong()& r.nextLong();// generate a magic number candidate
                    else magicCandidate = r.nextLong() & r.nextLong() ;
                    boolean fail = false;
                    for (int k = 0; k < allBlockers.length; k++) {
                        int hashKey =(int) ((allBlockers[k].getXOR() * magicCandidate)>>>(65-rookBlockers[j][i].bitCount()-skip));
                        if(usedAt[hashKey]!=epochID){ // check if we are at the same epoch
                            usedAt[hashKey]=epochID; // slot is marked as used for this epoch
                            testTable[hashKey]=allAttackers[k];// put relevant attacker in this hash
                        }else if(!testTable[hashKey].equals(allAttackers[k])){ //wrong collision
                            // if it's not equal to the right attack pattern
                            fail =true;
                            break;
                        }
                    }
                    if(!fail) found=true;
                    if(epochID>500_000) skip=1;
                }
                magicNumbersRook[j][i]=magicCandidate;
            }
        }

        //BISHOP
        BitBoard91[] allBlockersB, allAttackersB;
        BitBoard91[] bishop= attackPatterns.getBishopBlockers();

        bishopMagicNumbers = new long[91];
        for (int i = 0; i < 91; i++) {
            currentCell=i;
            allBlockersB = new BitBoard91[(int)Math.pow(2,bishop[i].bitCount())];
            allAttackersB = new BitBoard91[(int)Math.pow(2,bishop[i].bitCount())];
            tableInd=0;// index to populate allblockers and all attackers
            //find all blcokers combination with helper and populate attackpatternbishoptemp
            helperBishop(0, bishop[i], new BitBoard91(),allBlockersB, allAttackersB);
            //find magic number and populate
            boolean found = false;long magicCandidate=0;
            BitBoard91[] testTable = new BitBoard91[1024]; // Stores attack bitboards
            int[] usedAt = new int[1024];     // Stores which candidate filled this slot
            boolean fail ;
            magicCandidate=0;
            int epochID =0;skip=0;
            while(!found){
                epochID++;
                fail=false;
                magicCandidate = r.nextLong() & r.nextLong() & r.nextLong() ;
                for (int j = 0; j < allBlockersB.length; j++) {
                    int hashKey =(int) ((allBlockersB[j].getXOR() * magicCandidate)>>>(65-bishop[i].bitCount()-skip));
                    if(usedAt[hashKey]!=epochID){
                        usedAt[hashKey]=epochID;
                        testTable[hashKey]=allAttackersB[j];
                    }else if(testTable[hashKey]!= allAttackersB[j]){
                        fail=true;
                        break;
                    }
                }
                if(!fail)
                    found=true;
                if(epochID>500_000) skip=1;
            }
            bishopMagicNumbers[i]=magicCandidate;
        }
        System.out.println("algo done in: "+(System.currentTimeMillis()-startTime)+"ms");
        MagicWriter.writeLongArrayBishop(bishopMagicNumbers);
        MagicWriter.writeLongArrayRook(magicNumbersRook);

    }

/*
    public static long geneticS(int rookBlockersMaskNumBit,BitBoard91[] allAttackers, BitBoard91[] allBlockers, int j, int i, long r1,long r2, long r3, long r4){
        boolean found = false;
        BitBoard91[] testTable = new BitBoard91[1024]; // Stores attack bitboards
        int[] usedAt = new int[1024];     // Stores which candidate filled this slot
        int success=0;
        int epochID =0;

        return r1;
    }

    public static void search(int epochID){
            epochID++;
            boolean fail = false;
            for (int k = 0; k < allBlockers.length; k++) {
                int hashKey =(int) ((allBlockers[k].getXOR() * magicCandidate)>>>(65-rookBlockersMaskNumBit));
//                        System.out.println(hashKey);
                if(usedAt[hashKey]!=epochID){ // check if we are at the same epoch
                    usedAt[hashKey]=epochID; // slot is marked as used for this epoch
                    testTable[hashKey]=allAttackers[k];// put relevant attacker in this hash
                }else if(!testTable[hashKey].equals(allAttackers[k])){ //wrong collision
                    // if it's not equal to the right attack pattern
//                            System.out.println("number of success out of 16:"+success);
                    success=0;
                    fail =true;
                }else success++;
            }

            if(success>0)
                System.out.println("Number of success so far: "+success);
            if(!fail) found=true;
    }

 */
}
