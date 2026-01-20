package logic;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HexBoardLUT {
    private static HexBoardLUT singleton;
    private final HashMap<String, HexCell> board;
    private final String[] cellList;

    public static HexBoardLUT getSingleton(){
        if(singleton==null) singleton=new HexBoardLUT();
        return singleton;
    }

    private HexBoardLUT() {
        cellList=new String[91];
        board = new HashMap<>();
        int limit = 6;int index=0;int indexMagic=0;
        boolean halfway = false;
        for (char a = 'a'; a <= 'l'; a++) {
            if (a != 'j') {
                for (int i = 0; i < limit; i++) {
                    HexCell currentCell;
                    if(i>0&&i<limit-1&&a>'a'&&a<'l') {
                        currentCell = new HexCell("" + a + (i + 1), index, indexMagic);
                    }else
                        currentCell = new HexCell("" + a + (i + 1), index);

                    cellList[index++]=currentCell.getPosition();
                    board.put(currentCell.getPosition(), currentCell);
                }
                if (limit < 11) {
                    if (!halfway) limit++;
                    else limit--;
                } else {
                    halfway = true;
                    limit--;
                }
            }
        }
        connectCellsDirect();
        connectCellsDiagonal();
    }

    private void connectCellsDirect() {
        String range = "abcdefghikl";
        int limit = 6;
        boolean halfway = false;
        for (int i = 0; i < range.length(); i++) {
            for (int j = 0; j < limit; j++) {
                HexCell current = board.get("" + range.charAt(i) + (j + 1));
                if (j > 0) {
                    HexCell s = board.get("" + range.charAt(i) + (j));
                    current.setS(s);
                }
                if (!halfway) {
                    if (i > 0) { // on the right of a column
                        if (j > 0) { //above first row
                            HexCell sw = board.get("" + range.charAt(i - 1) + (j));
                            current.setSw(sw);
                        }
                        if (j < limit - 1) { //below upper row
                            HexCell nw = board.get("" + range.charAt(i - 1) + (j + 1));
                            current.setNw(nw);
                        }
                    }
                } else { // pass halfway
                    HexCell sw = board.get("" + range.charAt(i - 1) + (j + 1));
                    current.setSw(sw);
                    HexCell nw = board.get("" + range.charAt(i - 1) + (j + 2));
                    current.setNw(nw);
                }
            }
            if (limit < 11) {
                if (!halfway) limit++;
                else limit--;
            } else {
                halfway = true;
                limit--;
            }
        }
    }

    private void connectCellsDiagonal() {
        String range = "abcdefghikl";
        int limit = 6;
        boolean halfway = false;
        for (int i = 0; i < range.length(); i++) {
            for (int j = 0; j < limit; j++) {
                HexCell current = board.get("" + range.charAt(i) + (j + 1));
                if (current.getNe() != null) {
                    current.setR(current.getNe().getSe());
                    current.setUr(current.getNe().getN());
                }
                if (current.getN() != null && current.getN().getNw() != null) current.setUl(current.getN().getNw());
            }
            if (limit < 11) {
                if (!halfway) limit++;
                else limit--;
            } else {
                halfway = true;
                limit--;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        HexCell starting = board.get("f11");
        while (starting != null) {
            writeLine(sb, starting);
            starting = starting.getSw();
        }
        starting = board.get("b6");
        while (starting.getS() != null) {
            writeLine(sb, starting);
            starting = starting.getSw();
            writeLine(sb, starting);
            starting = starting.getSe();
        }
        while (starting != null) {
            writeLine(sb, starting);
            starting = starting.getSe();
        }
        return sb.toString();
    }

    private void writeLine(StringBuilder sb, HexCell c) {
        int repeat = c.getPosition().charAt(0) - 97;
        if (c.getPosition().charAt(0) > 'j') repeat--;
        sb.append("    ".repeat(repeat));
        while (c != null) {
            sb.append(c).append("      ");
            c = c.getR();
        }
        sb.append("\n");
    }

    public List<HexCell> getAllCells() {
        return board.values().stream().sorted().toList();
    }

    //may return null if string format is wrong
    public HexCell getPosition(String pos) {
        return board.get(pos);
    }

    public String getCoordinateFromIndex(int i){
        return cellList[i];
    }

    public HexCell getCellFromIndex(int i){
        return board.get(cellList[i]);
    }


    public static void main(String[] args) {
        HexBoardLUT b = new HexBoardLUT();
        System.out.println(b);
        Random r = new Random();
        BitBoard91 test = new BitBoard91(r.nextLong()&r.nextLong(), r.nextLong()&r.nextLong());
        int i = test.getIndexAfter(0);
        while(test.getIndexAfter(i)!=-1){
            System.out.println(i);
            i=test.getIndexAfter(i);
        }

//        int i=0;
//        for(String a: b.cellList) System.out.print((i++)+":"+(b.getPosition(a))+" ");
//        HexCell cell = b.getPosition("f11");
//        while (cell.getSe() != null) {
//            System.out.println(cell);
//            cell = cell.getSe();
//        }
    }
}