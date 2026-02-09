package logic;

import java.util.HashMap;

public class GameState {
    HashMap<String,BitBoard91> whitePieces;
    HashMap<String,BitBoard91> blackPieces;

    BitBoard91 wKing;
    BitBoard91 bKing;
    BitBoard91 wQueen;
    BitBoard91 bQueen;
    BitBoard91 wPawn;
    BitBoard91 bPawn;
    BitBoard91 wBishop;
    BitBoard91 bBishop;
    BitBoard91 wRook;
    BitBoard91 bRook;
    BitBoard91 wKnight;
    BitBoard91 bKnight;

    BitBoard91 white;
    BitBoard91 black;

    boolean whiteTurn;

    boolean[] castle;// wk wr bk br
    BitBoard91 enPassant; //record which position is possible for en passant


    public GameState(){
        whitePieces = new HashMap<>();
        blackPieces = new HashMap<>();
        HashMap<String, Integer> positionLU = HexBoardLUT.getSingleton().getPositionList();
        wPawn = new BitBoard91(positionLU.get("b1"),positionLU.get("c2"),
                positionLU.get("d3"),positionLU.get("e4"),
                positionLU.get("f5"),positionLU.get("g4"),
                positionLU.get("h3"), positionLU.get("i2"),
                positionLU.get("k1"));
        bPawn = new BitBoard91(positionLU.get("b7"),positionLU.get("c7"),
                positionLU.get("d7"),positionLU.get("e7"),
                positionLU.get("f7"),positionLU.get("g7"),
                positionLU.get("h7"), positionLU.get("i7"),
                positionLU.get("k7"));
        wKing = new BitBoard91(positionLU.get("g1"));
        bKing = new BitBoard91(positionLU.get("g10"));
        wQueen = new BitBoard91(positionLU.get("e1"));
        bQueen = new BitBoard91(positionLU.get("e10"));
        wBishop = new BitBoard91(positionLU.get("f1"),positionLU.get("f2"),positionLU.get("f3"));
        bBishop = new BitBoard91(positionLU.get("f11"),positionLU.get("f10"),positionLU.get("f9"));
        wRook = new BitBoard91();
        wRook.toggle(positionLU.get("c1"));
        wRook.toggle(positionLU.get("i1"));
        bRook = new BitBoard91();
        bRook.toggle(positionLU.get("c8"));
        bRook.toggle(positionLU.get("i8"));
        wKnight=new BitBoard91();
        wKnight.toggle(positionLU.get("d1"));
        wKnight.toggle(positionLU.get("h1"));
        bKnight=new BitBoard91();
        bKnight.toggle(positionLU.get("d9"));
        bKnight.toggle(positionLU.get("h9"));

        white=new BitBoard91();
        white.or(wBishop);white.or(wRook);white.or(wKing);white.or(wKnight);white.or(wPawn);white.or(wQueen);
        black = new BitBoard91();
        black.or(bBishop);black.or(bRook);black.or(bKing);black.or(bKnight);black.or(bPawn);black.or(bQueen);

        whitePieces.put("pawn",wPawn);
        whitePieces.put("king",wKing);
        whitePieces.put("queen",wQueen);
        whitePieces.put("rook",wRook);
        whitePieces.put("bishop",wBishop);
        whitePieces.put("knight",wKnight);

        blackPieces.put("pawn",bPawn);
        blackPieces.put("king",bKing);
        blackPieces.put("queen",bQueen);
        blackPieces.put("rook",bRook);
        blackPieces.put("bishop",bBishop);
        blackPieces.put("knight",bKnight);
    }

    public BitBoard91 getPossibleMovesFromPosition(String pos){
        int index = HexBoardLUT.getSingleton().getPositionList().get(pos);
        return getPossibleMovesFromPosition(index);
    }

    public BitBoard91 getPossibleMovesFromPosition(int pos){
        BitBoard91 possibleAttack = new BitBoard91();
        String piece = whichPieceOn(pos);
        if(piece.equals("king")){
            BitBoard91 overlap;
            if(whiteTurn) overlap = white.and(GameMasks.getSingleton().getKingAttack()[pos]);
            else overlap = black.and(GameMasks.getSingleton().getKingAttack()[pos]);
            return GameMasks.getSingleton().getKingAttack()[pos].xor(overlap);
        }
        else if (piece.equals("knight")) {
            BitBoard91 overlap;
            if(whiteTurn) overlap = white.and(GameMasks.getSingleton().getKnightAttack()[pos]);
            else overlap = black.and(GameMasks.getSingleton().getKnightAttack()[pos]);
            return GameMasks.getSingleton().getKnightAttack()[pos].xor(overlap);
        }else if(piece.equals("pawn")){
            if(whiteTurn){
                BitBoard91 blocking = white.and(GameMasks.getSingleton().getWhitePawnPush()[pos]);//normal move
                if(blocking.getFirstIndex()!=-1)
                    possibleAttack = GameMasks.getSingleton().getWhitePawnPush()[pos].xor(blocking);
                else
                    possibleAttack = GameMasks.getSingleton().getWhitePawnPush()[pos]; //todo pass by ref->clone
                possibleAttack=possibleAttack.or( black.and(GameMasks.getSingleton().getWhitePawnAttack()[pos]) ); // get white pawn attak method TODO
                if(enPassant.getFirstIndex()!=-1) possibleAttack.or(enPassant.and(GameMasks.getSingleton().getWhitePawnAttack()[pos]));
            }else{

            }
        }
        //// TODO complete this bit here////
        return possibleAttack;
    }


    public String whichPieceOn(int index){
        if(wKing.getIndexBit(index)||bKing.getIndexBit(index)) return "king";
        else if(wQueen.getIndexBit(index)||bQueen.getIndexBit(index)) return "queen";
        else if(wPawn.getIndexBit(index)||bPawn.getIndexBit(index)) return "pawn";
        else if(wRook.getIndexBit(index)||bRook.getIndexBit(index)) return "rook";
        else if(wBishop.getIndexBit(index)||bBishop.getIndexBit(index)) return "bishop";
        else if(wKnight.getIndexBit(index)||bKnight.getIndexBit(index)) return "knight";
        return "none";
    }

    public String whichColorOn(int index){
        if(white.getIndexBit(index)) return "white";
        else if(black.getIndexBit(index)) return "white";
        return "none";
    }
}
