package logic;


public class HexCell implements Comparable<HexCell> {
    /*
    this class will provide an object for a single cell on the board
    each cell will be connected to adjacent cell and also with diagonal cell
    additional information: color of the cell, chess piece on it, position (h1 for example)
    adjacent: n (north), ne(northeast) se(south east) s (south) etc
    diagonal: ur (up right) r (right) dr (down right)

    as only the left cell will be added to existing cells, we will only implement half of the methods
     */
    private HexCell n;
    private HexCell ne;
    private HexCell se;
    private HexCell s;
    private HexCell sw;
    private HexCell nw;

    private HexCell ur;
    private HexCell r;
    private HexCell dr;
    private HexCell dl;
    private HexCell l;
    private HexCell ul;

    private char col;
    private int row;

    private final int index;
    private int indexMagic;

    public HexCell(String position, int index){
        setPosition(position);
        this.index=index;
        this.indexMagic=-1;
    }

    public HexCell(String position, int index, int indexMagic){
        setPosition(position);
        this.index=index;
        this.indexMagic=indexMagic;
    }

    public int getIndexMagic(){
        return this.indexMagic;
    }

    public int getIndex() {
        return index;
    }

    public char getCol() {
        return col;
    }

    public int getRow() { // row as in the board we see !
        return row;
    }

    public HexCell getN() {
        return n;
    }

    public HexCell getNe() {
        return ne;
    }

    public HexCell getSe() {
        return se;
    }

    public HexCell getS() {
        return s;
    }

    public HexCell getSw() {
        return sw;
    }

    public HexCell getNw() {
        return nw;
    }

    public HexCell getUr() {
        return ur;
    }

    public HexCell getR() {
        return r;
    }

    public HexCell getDr() {
        return dr;
    }

    public HexCell getDl() {
        return dl;
    }

    public HexCell getL() {
        return l;
    }

    public HexCell getUl() {
        return ul;
    }

    public void setN(HexCell n) {
        this.n = n;
        n.s=this;
    }

    public void setNe(HexCell ne) {
        this.ne = ne;
        ne.sw=this;
    }

    public void setSe(HexCell se) {
        this.se = se;
        se.nw=this;
    }

    public void setS(HexCell s) {
        this.s = s;
        if(s!=null)
            s.n=this;
    }

    public void setSw(HexCell sw) {
        this.sw = sw;
        if(sw!=null)
            sw.ne=this;
    }

    public void setNw(HexCell nw) {
        this.nw = nw;
        if(nw!=null)
            nw.se=this;
    }

    public void setUr(HexCell ur) {
        this.ur = ur;
        if(ur!=null)
            ur.dl=this;
    }

    public void setR(HexCell r) {
        this.r = r;
        if(r!=null)
            r.l=this;
    }

    public void setDr(HexCell dr) {
        this.dr = dr;
        dr.ul=this;
    }

    public void setDl(HexCell dl) {
        this.dl = dl;
        dl.ur=this;
    }

    public void setL(HexCell l) {
        this.l = l;
        l.r=this;
    }

    public void setUl(HexCell ul) {
        this.ul = ul;
        ul.dr=this;
    }

    @Override
    public String toString(){
//        return ""+ col + row;
        return (index>9?"":" ")+index;
    }

    @Override
    public int compareTo(HexCell hc){
        if(Character.compare(this.col,hc.col)!=0) return Character.compare(this.col,hc.col);
        return Integer.compare(this.row,hc.row);
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        HexCell hco = (HexCell)o;
        return this.col==hco.col && this.row==hco.row;
    }

    public Boolean setPosition(String position){
        if(!position.matches("[a-ik-l]([1-9]|1[01])")) return false;
        this.col=position.charAt(0);
        this.row=Integer.valueOf(position.substring(1));
        return true;
    }

    public String getPosition(){
        return "" +col+row;
    }

}
