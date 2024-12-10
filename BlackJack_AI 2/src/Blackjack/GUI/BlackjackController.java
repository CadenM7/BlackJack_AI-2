package Blackjack.GUI;

import Blackjack.AI.BlackjackAI;
import Blackjack.Game.Blackjack;
import Blackjack.Game.Chip;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.File;

public class BlackjackController {

    @FXML
    private Label label;
    @FXML
    private Label playerScore;
    @FXML
    private Label dealerScore;
    @FXML
    private HBox playerCards;
    @FXML
    private HBox dealersHand;
    @FXML
    private Button hit;
    @FXML
    private Button stand;
    @FXML
    private TextField numOfChips;
    @FXML
    private Button bet;
    @FXML
    private Label TotalChips;
    @FXML
    private Label minimum;
    @FXML
    private Label maximum;
    @FXML
    private TextField numOfGames;
    @FXML
    private Label wins;
    @FXML
    private Label losses;
    @FXML
    private Label draws;
    @FXML
    private Button train;
    @FXML
    private Button play;

    private Blackjack blackjack;

    private Chip chips;

    private BlackjackAI blackjackAi;

    private int c = 2000;

    @FXML
    public void initialize() {
        blackjack = new Blackjack(52);
        chips = new Chip(c, 5, 100);
        blackjackAi = new BlackjackAI(172, 2, 0, 10, 50, .5);
        updateView();
        System.out.println("Player " + blackjack.getPlayerHandValue());
        System.out.println("Dealer " + blackjack.getDealerHandValue());
    }

    @FXML
    private void aiPlay() {
        reset();
        boolean gameOver = false;
        while(!gameOver) {
            int state = 17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4); // Just the face card for the dealer
            System.out.println("state: " + state + " dealer: " + blackjack.getDealerHandValue() + " player: " + blackjack.getPlayerHandValue());
            int action = blackjackAi.senseActLearn(state, 0);
            if(action == 0) {
                blackjack.playerHit();
                updateView();
                if (blackjack.getPlayerHandValue() > 21) {
                    disableButton(true);
                    label.setText("LOSE, Play Again?");
                    gameOver = true;
                }
                if (blackjack.getPlayerHandValue() == 21) {
                    disableButton(true);
                    label.setText("WON, Play Again?");
                    gameOver = true;
                }
                } else {
                    blackjack.playerStand();
                    disableButton(true);
                    updateView();
                    gameOver = true;
                getPlayerHandValue();
                System.out.println("Player " + blackjack.getPlayerHandValue());
            }
        }
        while (blackjack.getDealerHandValue() < 17) {
            if (blackjack.getDealerHandValue() == 17) {
                blackjack.dealerStand();
            } else {
                blackjack.dealerHit();
            }
            updateView();
        }
        System.out.println("Dealer " + blackjack.getDealerHandValue());
        if (blackjack.getDealerHandValue() > 21) {
            label.setText("WON, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), 20);
        }
        if (blackjack.getDealerHandValue() < blackjack.getPlayerHandValue() && blackjack.getPlayerHandValue() < 21) {
            label.setText("WON, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), 20);
        }
        if (blackjack.getDealerHandValue() == 21) {
            label.setText("LOSE, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), -50);
        }
        if (blackjack.getDealerHandValue() > blackjack.getPlayerHandValue() && blackjack.getDealerHandValue() < 21) {
            label.setText("LOSE, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), -50);
        }
        if (blackjack.getDealerHandValue() == blackjack.getPlayerHandValue()) {
            label.setText("DRAW, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), 0);
        }
        updateView();
    }
    @FXML
    public void aiTrain() {
        int win = 0;
        int loss = 0;
        int draw = 0;
        disableAI(true);
        int games = Integer.parseInt(numOfGames.getText());
        // For each game
        for(int i = 0; i < games; i++){
            // * generate hands for player and dealer
            for(int x = 0; x < blackjack.handSize(); x++) {
                blackjack.getCard(x).ab();
            }
            for(int x = 0; x < blackjack.dealersHand(); x++) {
                blackjack.getDealerCard(x).ab();
            }
            getDealerHandValue();
            getPlayerHandValue();
            // * Play game, calling senseActLearn() for each move
            int state = 17 * (blackjack.getDealerFirstCard() - 2) + (blackjack.getPlayerHandValue() - 4);
            System.out.println("State: "+ state);
            System.out.println("Player: " + blackjack.getPlayerHandValue());
            System.out.println("DealerFaceCard: " + blackjack.getDealerFirstCard());

            // Look like what you did in play
            // after all training rounds are done, display # wins and losses

            boolean gameOver = false;
            while(!gameOver) {
                System.out.println("state: " + state + " dealer: " + blackjack.getDealerHandValue() + " player: " + blackjack.getPlayerHandValue());
                int action = blackjackAi.senseActLearn(state, 0);
                if(action == 0) {
                    blackjack.playerHit();
                    updateView();
                    getPlayerHandValue();
                    System.out.println("Player " + blackjack.getPlayerHandValue());
                    if(blackjack.getPlayerHandValue() > 21) {
                        gameOver = true;
                    }
                    if(blackjack.getPlayerHandValue() == 21) {
                        gameOver = true;
                    }
                } else {
                    blackjack.playerStand();
                    disableButton(true);
                    gameOver = true;
                }
            }
            while (blackjack.getDealerHandValue() < 17) {
                if (blackjack.getDealerHandValue() == 17) {
                    blackjack.dealerStand();
                } else {
                    blackjack.dealerHit();
                }
            }
            System.out.println("Dealer " + blackjack.getDealerHandValue());
            if (blackjack.getDealerHandValue() > 21) {
                blackjackAi.senseActLearn(state, 20);
                win += 1;
                wins.setText(String.valueOf(win));
            }
            else if (blackjack.getPlayerHandValue() == 21) {
                blackjackAi.senseActLearn(state,20);
                win +=1;
                wins.setText(String.valueOf(win));
            }
            else if (blackjack.getPlayerHandValue() > 21) {
                blackjackAi.senseActLearn(state, -50);
                loss += 1;
                losses.setText(String.valueOf(loss));
            }
            else if (blackjack.getDealerHandValue() < blackjack.getPlayerHandValue() && blackjack.getPlayerHandValue() < 21) {
                blackjackAi.senseActLearn(state, 20);
                win += 1;
                wins.setText(String.valueOf(win));
            }
            else if (blackjack.getDealerHandValue() == 21) {
                blackjackAi.senseActLearn(state, -50);
                loss += 1;
                losses.setText(String.valueOf(loss));
            }
            else if (blackjack.getDealerHandValue() > blackjack.getPlayerHandValue() && blackjack.getDealerHandValue() < 21) {
                blackjackAi.senseActLearn(state, -50);
                loss += 1;
                losses.setText(String.valueOf(loss));
            }
            else if (blackjack.getDealerHandValue() > 21 && blackjack.getPlayerHandValue() > 21) {
                blackjackAi.senseActLearn(state, 0);
                draw += 1;
                draws.setText(String.valueOf(draw));
            }
            else {
                blackjackAi.senseActLearn(state, 0);
                draw += 1;
                draws.setText(String.valueOf(draw));
            }
            updateView();
            reset();
        }
    }
    @FXML
    public void bet(int out) {
        int bettingChips = Integer.parseInt(numOfChips.getText());
        int totalChips = c - bettingChips;
        if(bettingChips > chips.getMinBet() - 1 && bettingChips < chips.getMaxBet() + 1) {
            if (out == 0) {
                totalChips += bettingChips * 2;
                System.out.println(totalChips);
                TotalChips.setText(String.valueOf(totalChips));
            }
            if (out == 1) {
                totalChips = c - bettingChips;
                System.out.println(totalChips);
                TotalChips.setText(String.valueOf(totalChips));
            }
            if (out == 2) {
                System.out.println(totalChips);
                TotalChips.setText(String.valueOf(totalChips));

            }
            c = totalChips;
            updateView();
        }
    }


    @FXML
    public void reset() {
        blackjack = new Blackjack(52);
        dealersHand.getChildren().clear();
        System.out.println("Getting dealers Card" + blackjack.getDealerFirstCard());
        playerCards.getChildren().clear();
        disableButton(false);
        disableBetting(false);
        disableAI(false);
        updateView();
        label.setText("Place Bet");
        System.out.println("Player " + blackjack.getPlayerHandValue());
        System.out.println("Dealer " + blackjack.getDealerHandValue());
        if(blackjack.getDealerHandValue() == 21) {
            disableButton(true);
        }
        if(blackjack.getPlayerHandValue() == 21) {
            disableButton(true);
        }
        if(blackjack.getPlayerHandValue() > 21) {
            disableButton(true);
        }
        if(blackjack.getDealerHandValue() > 21) {
            disableButton(true);
        }
    }

    public void updateView() {
        playerCards.getChildren().clear();
        dealersHand.getChildren().clear();
        for(int x = 0; x < blackjack.handSize(); x++) {
            ImageView iv = new ImageView();
            playerCards.getChildren().add(iv);
            setCardImage(blackjack.getCard(x).ab(), iv);
        }
        for(int x = 0; x < blackjack.dealersHand(); x++) {
            ImageView iv2 = new ImageView();
            dealersHand.getChildren().add(iv2);
            setCardImage(blackjack.getDealerCard(x).ab(), iv2);

        }
        getPlayerHandValue();
        getDealerFirstCard();
        getTotalChips();
        getMinBet();
        getMaxBet();

    }
    public void setCardImage(String top, ImageView cardImage) {
        File c = new File("src/Blackjack/Cards/" + top + ".png");
        //System.out.println(c.toURI());
        cardImage.setImage(new Image(c.toURI().toString()));
    }

    public void disableButton(boolean disable) {
        hit.setDisable(disable);
        stand.setDisable(disable);
        play.setDisable(disable);
    }
    public void disableBetting(boolean disable) {
        bet.setDisable(disable);
        numOfChips.setDisable(disable);
    }
    public void disableAI(boolean disable) {
        numOfGames.setDisable(disable);
        train.setDisable(disable);

    }

    @FXML
    public void hit() {
        blackjack.playerHit();
        disableBetting(true);
        disableAI(true);
        updateView();
        getPlayerHandValue();
        System.out.println("Player " + blackjack.getPlayerHandValue());
        if(blackjack.getPlayerHandValue() > 21) {
            disableButton(true);
            label.setText("LOSE, Play Again?");
            bet(1);
        }
        if(blackjack.getPlayerHandValue() == 21) {
            disableButton(true);
            label.setText("WON, Play Again?");
            bet(0);
        }
    }

    @FXML
    public void stand() {
        blackjack.playerStand();
        disableButton(true);
        disableBetting(true);
        disableAI(true);
        updateView();

        while (blackjack.getDealerHandValue() < 17) {
            if (blackjack.getDealerHandValue() == 17) {
                blackjack.dealerStand();
            } else {
                blackjack.dealerHit();
            }
            updateView();
        }
            System.out.println("Dealer " + blackjack.getDealerHandValue());
            if (blackjack.getDealerHandValue() > 21) {
                label.setText("WON, Play Again?");
                bet(0);
                /* Add Double Chips to total chips for winning */
            }
            if (blackjack.getDealerHandValue() < blackjack.getPlayerHandValue() && blackjack.getPlayerHandValue() < 21) {
                label.setText("WON, Play Again?");
                bet(0);
                /* Add Double Chips to total chips for winning */
            }
            if (blackjack.getDealerHandValue() == 21) {
                label.setText("LOSE, Play Again?");
                bet(1);
                /* Take away Chips bet from total chips for losing */
            }
            if (blackjack.getDealerHandValue() > blackjack.getPlayerHandValue() && blackjack.getDealerHandValue() < 21) {
                label.setText("LOSE, Play Again?");
                bet(1);
                /* Take away Chips bet from total chips for losing */
            }
            if (blackjack.getDealerHandValue() == blackjack.getPlayerHandValue()) {
                label.setText("DRAW, Play Again?");
                bet(2);
                /* Give Chips back to player */
            }
            updateView();
        }

    @FXML
    public void getPlayerHandValue() {
        playerScore.setText(String.valueOf(blackjack.getPlayerHandValue()));
    }
    @FXML
    public void getDealerHandValue() {
         dealerScore.setText(String.valueOf(blackjack.getDealerHandValue()));
    }
    @FXML
    public void getDealerFirstCard() {
        dealerScore.setText(String.valueOf(blackjack.getDealerHandValue()));
    }
    @FXML
    public void getMinBet() {
        minimum.setText(String.valueOf(chips.getMinBet()));
    }
    @FXML
    public void getMaxBet() {
        maximum.setText(String.valueOf(chips.getMaxBet()));
    }
    @FXML
    public void getTotalChips() {
        TotalChips.setText(String.valueOf(c));
    }


}

