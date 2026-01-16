package logic;

public class BitBoard91 {
    private long low; //bit 0 to 63
    private long high; //bit 64 to 91 + extra

    public BitBoard91(int ... indices){
        for(int i: indices){
            toggle(i);
        }
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

    public void and(BitBoard91 other) {
        this.low &= other.low;
        this.high &= other.high;
    }

    public void xor(BitBoard91 other) {
        this.low ^= other.low;
        this.high ^= other.high;
    }

    // Toggles a specific square  (<< operation)
    public void toggle(int square) {
        if (square < 64) low ^= (1L << square);
        else high ^= (1L << (square - 64));
    }

    public void shiftLeft(int bits) {
        long carry = low >>> (64 - bits); // Get the bits that will fall off the top of 'low'
        low <<= bits;
        high = (high << bits) | carry;   // Move 'high' and bring in the carry
        high &= 0x7FFFFFFFFFFL;          // Optional: clear bits above 91
    }

    public long convertForMagic(){
        long result=0;
        // Mapping Block 1: Bits 0-4 (5 bits) from source bits 7-11
        result |= (this.low >>> 7 & 0b11111L);

        // Mapping Block 2: Bits 5-10 (6 bits) from source bits 14-19
        result |= (this.low >>> 14 & 0b111111L) << 5;

        // Mapping Block 3: Bits 11-17 (7 bits) from source bits 22-28
        result |= (this.low >>> 22 & 0b1111111L) << 11;

        // Mapping Block 4: Bits 18-25 (8 bits) from source bits 31-38
        result |= (this.low >>> 31 & 0b11111111L) << 18;

        // Mapping Block 5: Bits 26-34 (9 bits) from source bits 41-49
        result |= (this.low >>> 41 & 0b111111111L) << 26;

        // Mapping Block 6: Bits 35-42 (8 bits) from source bits 52-59
        result |= (this.low >>> 52 & 0b11111111L) << 35;

        // Part 7a: 2 bits from Source A (bits 62-63) to Dest bits 43-44
        result |= (this.low >>> 62 & 0b11L) << 43;

        // Part 7b: 5 bits from Source B (bits 64-68 -> index 0-4) to Dest bits 45-49
        // (64 - 64 = 0)
        result |= (this.high & 0b11111L) << 45;

        // Mapping Block 8: Bits 50-55 (6 bits) from source bits 71-76
        // Note: Java longs are 64 bits; bits 71+ must come from sourceB
        result |= (this.high >>> (71 - 64) & 0b111111L) << 50;

        // Mapping Block 9: Bits 56-60 (5 bits) from source bits 79-83
        result |= (this.high >>> (79 - 64) & 0b11111L) << 56;

        return result;
    }

    public static BitBoard91 magicToBitBoard91(long source){
        long low=0,high=0;
        // Block 1: Dest bits 0-4 (5 bits) back to Source A bit 7
        low |= (source & 0b11111L) << 7;

        // Block 2: Dest bits 5-10 (6 bits) back to Source A bit 14
        low |= (source >>> 5 & 0b111111L) << 14;

        // Block 3: Dest bits 11-17 (7 bits) back to Source A bit 22
        low |= (source >>> 11 & 0b1111111L) << 22;

        // Block 4: Dest bits 18-25 (8 bits) back to Source A bit 31
        low |= (source >>> 18 & 0b11111111L) << 31;

        // Block 5: Dest bits 26-34 (9 bits) back to Source A bit 41
        low |= (source >>> 26 & 0b111111111L) << 41;

        // Block 6: Dest bits 35-42 (8 bits) back to Source A bit 52
        low |= (source >>> 35 & 0b11111111L) << 52;

        // --- Block 7: Split across Source A and Source B ---
        // Part 7a: Combined bits 43-44 (2 bits) back to Source A bits 62-63
        low |= (source >>> 43 & 0b11L) << 62;

        // Part 7b: Combined bits 45-49 (5 bits) back to Source B bits 0-4 (original 64-68)
        high |= (source >>> 45 & 0b11111L);

        // Block 8: Dest bits 50-55 (6 bits) back to Source B bit 7 (original 71)
        high |= (source >>> 50 & 0b111111L) << 7;

        // Block 9: Dest bits 56-60 (5 bits) back to Source B bit 15 (original 79)
        high |= (source >>> 56 & 0b11111L) << 15;
        return new BitBoard91(low,high);
    }

}
