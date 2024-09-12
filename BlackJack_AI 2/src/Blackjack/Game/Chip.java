package Blackjack.Game;

public class Chip {
    private int chipsValue;
    private int minBet;
    private int maxBet;

    public Chip (int chipsValue, int minBet, int maxBet) {
        this.chipsValue = chipsValue;
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public int getChipValues() {return chipsValue;}

    public int getMinBet() {return minBet;}

    public int getMaxBet() {return maxBet;}
}

