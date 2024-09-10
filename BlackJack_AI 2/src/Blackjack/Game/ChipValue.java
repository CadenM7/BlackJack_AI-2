package Blackjack.Game;

public enum ChipValue {
    // Values
    ONE(1, 1),
    FIVE(2, 5),
    TWENTY_FIVE(3, 25),
    ONE_HUNDRED(4, 100);

    private int value;

    private int tv;

    public int getValue() {return value;}

    public int getTV() {return tv;}

    private ChipValue(int value, int tv) {
        this.value = value;
        this.tv = tv;
    }
}
