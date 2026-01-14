package logic;

public class BitBoard91 {
    private long low; //bit 0 to 63
    private long high; //bit 64 to 91 + extra

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

}
