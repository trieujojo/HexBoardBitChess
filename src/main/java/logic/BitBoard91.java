package logic;

public class BitBoard91 {
    private long low; //bit 0 to 63
    private long high; //bit 64 to 91 + extra

    public BitBoard91(int ... indices){
        low=0;high=0;
        for(int i: indices){
            toggle(i);
        }
        high &= 0x7FFFFFFFFFFL;
    }

    public BitBoard91(BitBoard91 other){
        this.low=other.low;
        this.high=other.high;
    }

    public BitBoard91(long low, long high){
        this.low=low;
        this.high=high;
    }

    public void or(BitBoard91 other) {
        this.low |= other.low;
        this.high |= other.high;
    }

    public BitBoard91 and(BitBoard91 other) {
        return new BitBoard91(this.low&other.low, this.high&other.high);
    }

    public BitBoard91 xor(BitBoard91 other) {
        return new BitBoard91(this.low ^= other.low,this.high ^= other.high);
    }

    // Toggles a specific square  (<< operation)
    public void toggle(int square) {
        if (square < 64) low ^= (1L << square);
        else if(square<91) high ^= (1L << (square - 64));
    }

    public BitBoard91 shiftLeft(int bits) {
        long carry = low >>> (64 - bits); // Get the bits that will fall off the top of 'low'
        low <<= bits;
        high = (high << bits) | carry;   // Move 'high' and bring in the carry
        high &= 0x7FFFFFFFFFFL;          // Optional: clear bits above 91
        return new BitBoard91(low<<bits,high<<bits);
    }

    public int getFirstIndex(){
        int result;
        if(low!=0){
            result= Long.numberOfLeadingZeros(low);
            low = low^(1L<<Long.numberOfLeadingZeros(low));
            return result;
        }else if(high!=0){
            result= Long.numberOfLeadingZeros(high);
            high = high^(1L<<Long.numberOfLeadingZeros(high));
            return result;
        }
        return -1;
    }

    public int getIndexAfter(int i){ // get the index of the first bit at index higher than i
        if(i>90) return -1;
        if(i<64){ // search both parts
            if(i!=63&&low>>>i+1!=0) return i+1+Long.numberOfTrailingZeros(low>>>i+1); //answer in first part
            else if(high!=0) return 64+Long.numberOfTrailingZeros(high);
        }else if(high>>>i+1!=0) return 1+i+ Long.numberOfTrailingZeros(high>>>i+1); //answer in high with i>64
        return -1;
    }


    public int bitCount(){
        return Long.bitCount(low) + Long.bitCount(high);
    }

    public long getAND(){
        return low & high;
    }

    public long getXOR(){
        return low ^ high;
    }

    public boolean getIndexBit(int i){
        if(i<64) return ((1L << i) & low) !=0;
        else return ((1L << i) & high) !=0;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        HexBoardLUT lut = HexBoardLUT.getSingleton();
        HexCell starting = lut.getPosition("f11");
        while (starting != null) {
            writeLine(sb, starting, lut);
            starting = starting.getSw();
        }
        starting = lut.getPosition("b6");
        while (starting.getS() != null) {
            writeLine(sb, starting, lut);
            starting = starting.getSw();
            writeLine(sb, starting, lut);
            starting = starting.getSe();
        }
        while (starting != null) {
            writeLine(sb, starting, lut);
            starting = starting.getSe();
        }
        return sb.toString();
    }

    private void writeLine(StringBuilder sb, HexCell c, HexBoardLUT lut) {
        int repeat = c.getPosition().charAt(0) - 97;
        if (c.getPosition().charAt(0) > 'j') repeat--;
        sb.append("    ".repeat(repeat));
        while (c != null) {
            if((c.getIndex()<64&&(low>>>(c.getIndex())&1)==1)||(c.getIndex()>63&&((high>>>(c.getIndex()-64)&1)==1)))
                sb.append(c).append("      ");
            else sb.append('-').append("       ");
            c = c.getR();
        }
        sb.append("\n");
    }

    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        if(other == null || getClass()!= other.getClass()) return false;
        BitBoard91 o = (BitBoard91) other;
        return o.low == this.low && o.high == this.high;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(low, high);
    }
}
