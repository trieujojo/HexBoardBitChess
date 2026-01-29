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
        wPawn = new BitBoard91(HexBoardLUT.getSingleton().getPosition("b1").getIndex(),HexBoardLUT.getSingleton().getPosition("c2").getIndex(),
                HexBoardLUT.getSingleton().getPosition("d3").getIndex(),HexBoardLUT.getSingleton().getPosition("e4").getIndex(),
                HexBoardLUT.getSingleton().getPosition("f5").getIndex(),HexBoardLUT.getSingleton().getPosition("g4").getIndex(),
                HexBoardLUT.getSingleton().getPosition("h3").getIndex(), HexBoardLUT.getSingleton().getPosition("i2").getIndex(),
                HexBoardLUT.getSingleton().getPosition("k1").getIndex());
        bPawn = new BitBoard91(HexBoardLUT.getSingleton().getPosition("b7").getIndex(),HexBoardLUT.getSingleton().getPosition("c7").getIndex(),
                HexBoardLUT.getSingleton().getPosition("d7").getIndex(),HexBoardLUT.getSingleton().getPosition("e7").getIndex(),
                HexBoardLUT.getSingleton().getPosition("f7").getIndex(),HexBoardLUT.getSingleton().getPosition("g7").getIndex(),
                HexBoardLUT.getSingleton().getPosition("h7").getIndex(), HexBoardLUT.getSingleton().getPosition("i7").getIndex(),
                HexBoardLUT.getSingleton().getPosition("k7").getIndex());
        wKing = new BitBoard91(HexBoardLUT.getSingleton().getPosition("g1").getIndex());
        bKing = new BitBoard91(HexBoardLUT.getSingleton().getPosition("g10").getIndex());
        wQueen = new BitBoard91(HexBoardLUT.getSingleton().getPosition("e1").getIndex());
        bQueen = new BitBoard91(HexBoardLUT.getSingleton().getPosition("e10").getIndex());
        wBishop = new BitBoard91(HexBoardLUT.getSingleton().getPosition("f1").getIndex(),HexBoardLUT.getSingleton().getPosition("f2").getIndex(),HexBoardLUT.getSingleton().getPosition("f3").getIndex());
        bBishop = new BitBoard91(HexBoardLUT.getSingleton().getPosition("f11").getIndex(),HexBoardLUT.getSingleton().getPosition("f10").getIndex(),HexBoardLUT.getSingleton().getPosition("f9").getIndex());
        wRook = new BitBoard91();
        wRook.toggle(HexBoardLUT.getSingleton().getPosition("c1").getIndex());
        wRook.toggle(HexBoardLUT.getSingleton().getPosition("i1").getIndex());
        bRook = new BitBoard91();
        bRook.toggle(HexBoardLUT.getSingleton().getPosition("c8").getIndex());
        bRook.toggle(HexBoardLUT.getSingleton().getPosition("i8").getIndex());
        wKnight=new BitBoard91();
        wKnight.toggle(HexBoardLUT.getSingleton().getPosition("d1").getIndex());
        wKnight.toggle(HexBoardLUT.getSingleton().getPosition("h1").getIndex());
        bKnight=new BitBoard91();
        bKnight.toggle(HexBoardLUT.getSingleton().getPosition("d9").getIndex());
        bKnight.toggle(HexBoardLUT.getSingleton().getPosition("h9").getIndex());

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
        int index = HexBoardLUT.getSingleton().getPosition(pos).getIndex();
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
