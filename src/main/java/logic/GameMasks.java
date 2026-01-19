package logic;

/*
    here will be some static variables that computed pseudo legal moves
 */
public class GameMasks {
    private static GameMasks singleton;
    private BitBoard91 board91;
    private BitBoard91[] whitePawnPush;
    private BitBoard91[] blackPawnPush;
    private BitBoard91[] knightAttack;
    private BitBoard91[] kingAttack;
    private BitBoard91[][] rookBlockers;
    private BitBoard91[] bishopAttack;

    private GameMasks(){
        init();
    }

    public static GameMasks getSingleton(){
        if(singleton==null) singleton=new GameMasks();
        return singleton;
    }

    public void init(){
        HexBoardLUT LUT= HexBoardLUT.getSingleton();

        board91=new BitBoard91();
        for (int i = 0; i < 91; i++) board91.toggle(i);

        whitePawnPush=new BitBoard91[91];
        for (int i = 0; i < 91; i++) {
            whitePawnPush[i]=new BitBoard91();
            HexCell current = LUT.getPosition(LUT.getCoordinateFromIndex(i));
            String position = current.getPosition(); char col = current.getCol(); int row = current.getRow();
            if((position.equals("c1")||position.equals("i1"))||
                    ((col=='d'||col=='h')&&row<3)||((col=='e'||col=='g')&&row<4)||
                    (row=='f'&&col<5)) continue;
            if(((col=='a'||col=='l')&&row==6)||((col=='b'||col=='k')&&row==7)||((col=='c'||col=='i')&&row==8)||
                    ((col=='d'||col=='h')&&row==9)||((col=='e'||col=='g')&&row==10)||(col=='f'&&row==11)) continue;
//            System.out.print(current.getPosition());
            whitePawnPush[i].toggle(current.getN().getIndex());
            if(position.equals("b1")||position.equals("c2")||position.equals("d3")||
                    position.equals("e4")||position.equals("f5")||position.equals("g4")
                            ||position.equals("h3")||position.equals("i2")||position.equals("k1"))
                whitePawnPush[i].toggle(current.getN().getN().getIndex());
        }

        blackPawnPush=new BitBoard91[91];
        for (int i = 0; i < 91; i++) {
            blackPawnPush[i]=new BitBoard91();
            HexCell current = LUT.getPosition(LUT.getCoordinateFromIndex(i));
            int row = current.getRow();
            if(row>7||row==1) continue;
            blackPawnPush[i].toggle(current.getS().getIndex());
            if (row==7)
                blackPawnPush[i].toggle(current.getS().getS().getIndex());
        }

        knightAttack = new BitBoard91[91];
        for (int i = 0; i < 91; i++) {
            knightAttack[i]=new BitBoard91();
            HexCell current = LUT.getPosition(LUT.getCoordinateFromIndex(i));
            if(current.getUr()!=null){
                if(current.getUr().getN()!=null) knightAttack[i].toggle(current.getUr().getN().getIndex());
                if(current.getUr().getNe()!=null) knightAttack[i].toggle(current.getUr().getNe().getIndex());
            }
            if(current.getR()!=null){
                if(current.getR().getNe()!=null) knightAttack[i].toggle(current.getR().getNe().getIndex());
                if(current.getR().getSe()!=null) knightAttack[i].toggle(current.getR().getSe().getIndex());
            }
            if(current.getDr()!=null){
                if(current.getDr().getS()!=null) knightAttack[i].toggle(current.getDr().getS().getIndex());
                if(current.getDr().getSe()!=null) knightAttack[i].toggle(current.getDr().getSe().getIndex());
            }
            if(current.getDl()!=null){
                if(current.getDl().getS()!=null) knightAttack[i].toggle(current.getDl().getS().getIndex());
                if(current.getDl().getSw()!=null) knightAttack[i].toggle(current.getDl().getSw().getIndex());
            }
            if(current.getL()!=null){
                if(current.getL().getNw()!=null) knightAttack[i].toggle(current.getL().getNw().getIndex());
                if(current.getL().getSw()!=null) knightAttack[i].toggle(current.getL().getSw().getIndex());
            }
            if(current.getUl()!=null){
                if(current.getUl().getNw()!=null) knightAttack[i].toggle(current.getUl().getNw().getIndex());
                if(current.getUl().getN()!=null) knightAttack[i].toggle(current.getUl().getN().getIndex());
            }
        }

        rookBlockers =new BitBoard91[3][91];
        for (int i = 0; i < 91; i++) {
            rookBlockers[0][i]=new BitBoard91();
            rookBlockers[1][i]=new BitBoard91();
            rookBlockers[2][i]=new BitBoard91();
            HexCell current = LUT.getPosition(LUT.getCoordinateFromIndex(i));
            HexCell dir = current.getN();
            while(dir.getN()!=null) {
                rookBlockers[0][i].toggle(dir.getIndex());
                dir = dir.getN();
            }
            dir=current.getS();
            while(dir.getS()!=null) {
                rookBlockers[0][i].toggle(dir.getIndex());
                dir = dir.getS();
            }
            dir=current.getNe();
            while(dir.getNe()!=null) {
                rookBlockers[1][i].toggle(dir.getIndex());
                dir = dir.getNe();
            }
            dir=current.getSw();
            while(dir.getSw()!=null) {
                rookBlockers[1][i].toggle(dir.getIndex());
                dir = dir.getSw();
            }
            dir=current.getSe();
            while(dir.getSe()!=null) {
                rookBlockers[2][i].toggle(dir.getIndex());
                dir = dir.getSe();
            }
            dir=current.getNw();
            while(dir.getNw()!=null) {
                rookBlockers[2][i].toggle(dir.getIndex());
                dir = dir.getNw();
            }
        }

        bishopAttack =new BitBoard91[91];
        for (int i = 0; i < 91; i++) {
            bishopAttack[i]=new BitBoard91();
            HexCell current = LUT.getPosition(LUT.getCoordinateFromIndex(i));
            HexCell dir = current.getUr();
            while(dir!=null) {
                bishopAttack[i].toggle(dir.getIndex());
                dir = dir.getUr();
            }
            dir=current.getR();
            while(dir!=null) {
                bishopAttack[i].toggle(dir.getIndex());
                dir = dir.getR();
            }
            dir=current.getDr();
            while(dir!=null) {
                bishopAttack[i].toggle(dir.getIndex());
                dir = dir.getDr();
            }
            dir=current.getDl();
            while(dir!=null) {
                bishopAttack[i].toggle(dir.getIndex());
                dir = dir.getDl();
            }
            dir=current.getL();
            while(dir!=null) {
                bishopAttack[i].toggle(dir.getIndex());
                dir = dir.getL();
            }
            dir=current.getUl();
            while(dir!=null) {
                bishopAttack[i].toggle(dir.getIndex());
                dir = dir.getUl();
            }
        }

        kingAttack = new BitBoard91[91];
        for (int i = 0; i < 91; i++) {
            kingAttack[i]=new BitBoard91();
            HexCell current = LUT.getPosition(LUT.getCoordinateFromIndex(i));
            if(current.getN()!=null) kingAttack[i].toggle(current.getN().getIndex());
            if(current.getNe()!=null) kingAttack[i].toggle(current.getNe().getIndex());
            if(current.getNw()!=null) kingAttack[i].toggle(current.getNw().getIndex());
            if(current.getS()!=null) kingAttack[i].toggle(current.getS().getIndex());
            if(current.getSe()!=null) kingAttack[i].toggle(current.getSe().getIndex());
            if(current.getSw()!=null) kingAttack[i].toggle(current.getSw().getIndex());

            if(current.getUl()!=null) kingAttack[i].toggle(current.getUl().getIndex());
            if(current.getUr()!=null) kingAttack[i].toggle(current.getUr().getIndex());
            if(current.getDl()!=null) kingAttack[i].toggle(current.getDl().getIndex());
            if(current.getDr()!=null) kingAttack[i].toggle(current.getDr().getIndex());
            if(current.getL()!=null) kingAttack[i].toggle(current.getL().getIndex());
            if(current.getR()!=null) kingAttack[i].toggle(current.getR().getIndex());
        }
    }

    public BitBoard91 getBoard91() {
        return board91;
    }

    public BitBoard91[] getWhitePawnPush() {
        return whitePawnPush;
    }

    public BitBoard91[] getBlackPawnPush() {
        return blackPawnPush;
    }

    public BitBoard91[] getKnightAttack() {
        return knightAttack;
    }

    public BitBoard91[] getKingAttack() {
        return kingAttack;
    }

    public BitBoard91[][] getRookBlockers() {
        return rookBlockers;
    }

    public BitBoard91[] getBishopAttack() {
        return bishopAttack;
    }
}
