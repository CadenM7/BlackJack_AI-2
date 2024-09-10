package Blackjack.Game;

public class Chips {
    private ChipColor color;
    private ChipValue values;

    public Chips (ChipValue value, ChipColor color) {
        this.values = value;
        this.color = color;
    }

    public String getcolor() {
        return color.name();
    }

    public void setcolor(ChipColor color) {
        this.color = color;
    }

    public int getChipValues() {
        return values.getValue();
    }

    public int getChipTV() {
        return values.getTV();
    }

    public void setChipValue(ChipValue chipvalue)
    {
        this.values = chipvalue;
    }

    public boolean equals (Object other) {
        if(other instanceof Chips) {
            Chips c = (Chips)other;
        }
        return false;
    }
    public String ab() {
        return "" + values.getValue() + color.name().charAt(0);
    }

}

