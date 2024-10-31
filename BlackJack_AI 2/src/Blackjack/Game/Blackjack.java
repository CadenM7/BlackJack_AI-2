package Blackjack.Game;

public class Blackjack {
    private Player player;

    private Player dealer;

    private BlackjackStack deck;

    private int size;


    public Blackjack(int size) {
        this.size = size;
        deck = new BlackjackStack();
        deck.shuffle();

        player = new Player();
        dealer = new Player();

        dealer.add(deck.pop());
        player.add(deck.pop());

        player.add(deck.pop());

    }

    public String toString() {
        String s = "";
        for (int p1 = 0; p1 < player.getLength(); p1++) {
            s += player.handValue();
            for (int p2 = 0; p2 < dealer.getLength(); p2++) {
                s += dealer.handValue();
            }
            s += "\n";
        }
        return s;
    }
    public void dealerHit() {
        dealer.add(deck.pop());
        dealer.handValue();
    }

    public void dealerStand() {dealer.tv();}

    public Card getCard(int x) {
        return player.get(x);
    }

    public Card getDealerCard(int x) {
        return dealer.get(x);
    }

    public int getPlayerHandValue() {
        return player.tv();
    }

    public int getDealerHandValue() {
        return dealer.tv();
    }

    public int getDealerFirstCard(){
        return dealer.fv();
    }

    public int handSize() {
        return player.getLength();
    }

    public int dealersHand() {
        return dealer.getLength();
    }

    public void playerHit() {
        player.add(deck.pop());
    }

    public void playerStand() {
        player.tv();
    }

}


