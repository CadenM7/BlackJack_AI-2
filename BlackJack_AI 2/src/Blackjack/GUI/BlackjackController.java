package Blackjack.GUI;

import Blackjack.Game.Blackjack;
import Blackjack.Game.BlackjackAI;
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
    private Button play;
    @FXML
    private TextField numOfGames;
    @FXML
    private Button train;
    @FXML
    private Label wins;
    @FXML
    private Label losses;
    @FXML
    private Label draws;


    private Blackjack blackjack;

    private BlackjackAI blackjackAi;

    @FXML
    public void initialize() {
        blackjack = new Blackjack(52);
        blackjackAi = new BlackjackAI(171, 2, 0, 10, 50, .5);
        updateView();
        System.out.println("Player " + blackjack.getPlayerHandValue());
        System.out.println("Dealer " + blackjack.getPlayer2HandValue());
    }

    @FXML
    private void aiPlay() {
        reset();
        boolean gameOver = false;
        while(!gameOver) {
            int state = 17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4); // Just the face card for the dealer
            System.out.println("state: " + state + " dealer: " + blackjack.getPlayer2FirstCard() + " player: " + blackjack.getPlayerHandValue());
            int action = blackjackAi.senseActLearn(state, 0);
            if(action == 0) {
                blackjack.playerHit();
                updateView();
                getPlayerHandValue();
                System.out.println("Player " + blackjack.getPlayerHandValue());
                if(blackjack.getPlayerHandValue() > 21) {
                    disableButton(true);
                    label.setText("LOSE, Play Again?");
                    gameOver = true;
                }
                if(blackjack.getPlayerHandValue() == 21) {
                    disableButton(true);
                    label.setText("WON, Play Again?");
                    gameOver = true;
                }
            } else {
                blackjack.playerStand();
                disableButton(true);
                updateView();
                gameOver = true;
            }
        }
        while (blackjack.getPlayer2HandValue() < 17) {
            if (blackjack.getPlayer2HandValue() == 17) {
                blackjack.dealerStand();
            } else {
                blackjack.dealerHit();
            }
            updateView();
        }
        System.out.println("Dealer " + blackjack.getPlayer2HandValue());
        if (blackjack.getPlayer2HandValue() > 21) {
            label.setText("WON, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), 10);
        }
        if (blackjack.getPlayer2HandValue() < blackjack.getPlayerHandValue() && blackjack.getPlayerHandValue() < 21) {
            label.setText("WON, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), 10);
        }
        if (blackjack.getPlayer2HandValue() == 21) {
            label.setText("LOSE, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), -20);
        }
        if (blackjack.getPlayer2HandValue() > blackjack.getPlayerHandValue() && blackjack.getPlayer2HandValue() < 21) {
            label.setText("LOSE, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), -20);
        }
        if (blackjack.getPlayer2HandValue() == blackjack.getPlayerHandValue()) {
            label.setText("DRAW, Play Again?");
            blackjackAi.senseActLearn(17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4), 0);
        }
        updateView();
    }
    @FXML
    public void aiTrain() {
        int win = 0;
        int loss = 0;
        int draw = 0;
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
            int state = 17 * (blackjack.getPlayer2FirstCard() - 2) + (blackjack.getPlayerHandValue() - 4);
            System.out.println("State: "+ state);
            System.out.println("Player: " + blackjack.getPlayerHandValue());
            System.out.println("DealerFaceCard: " + blackjack.getPlayer2FirstCard());

            // Look like what you did in play
            // after all training rounds are done, display # wins and losses

            boolean gameOver = false;
            while(!gameOver) {
                System.out.println("state: " + state + " dealer: " + blackjack.getPlayer2FirstCard() + " player: " + blackjack.getPlayerHandValue());
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
            while (blackjack.getPlayer2HandValue() < 17) {
                if (blackjack.getPlayer2HandValue() == 17) {
                    blackjack.dealerStand();
                } else {
                    blackjack.dealerHit();
                }
            }
            System.out.println("Dealer " + blackjack.getPlayer2HandValue());
            if (blackjack.getPlayer2HandValue() > 21) {
                blackjackAi.senseActLearn(state, 10);
                win += 1;
                wins.setText(String.valueOf(win));
            }
            if (blackjack.getPlayer2HandValue() < blackjack.getPlayerHandValue() && blackjack.getPlayerHandValue() < 21) {
                blackjackAi.senseActLearn(state, 10);
                win += 1;
                wins.setText(String.valueOf(win));
            }
            if (blackjack.getPlayer2HandValue() == 21) {
                blackjackAi.senseActLearn(state, -20);
                loss += 1;
                losses.setText(String.valueOf(loss));
            }
            if (blackjack.getPlayer2HandValue() > blackjack.getPlayerHandValue() && blackjack.getPlayer2HandValue() < 21) {
                blackjackAi.senseActLearn(state, -20);
                loss += 1;
                losses.setText(String.valueOf(loss));
            }
            if (blackjack.getPlayer2HandValue() == blackjack.getPlayerHandValue()) {
                blackjackAi.senseActLearn(state, 0);
                draw += 1;
                draws.setText(String.valueOf(draw));
            }
            updateView();
            reset();


            // * After game ends, call senseActLearn() one more time to present reward

            //   * Make sure to do this in AIPlay as well above.
        }
    }
    @FXML
    public void reset() {
        blackjack = new Blackjack(52);
        playerCards.getChildren().clear();
        dealersHand.getChildren().clear();
        disableButton(false);
        updateView();
        label.setText("BlackJack");
        System.out.println("Player " + blackjack.getPlayerHandValue());
        System.out.println("Dealer " + blackjack.getPlayer2HandValue());
        if(blackjack.getPlayer2HandValue() == 21) {
            disableButton(true);
        }
        if(blackjack.getPlayerHandValue() == 21) {
            disableButton(true);
        }
        if(blackjack.getPlayerHandValue() > 21) {
            disableButton(true);
        }
        if(blackjack.getPlayer2HandValue() > 21) {
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
        getDealerHandValue();
        getPlayerHandValue();

    }
    public void setCardImage(String top, ImageView cardImage) {
        File c = new File("src/Blackjack/Cards/" + top + ".png");
        //System.out.println(c.toURI());
        cardImage.setImage(new Image(c.toURI().toString()));
    }

    public void disableButton(boolean disable) {
        hit.setDisable(disable);
        stand.setDisable(disable);
    }

    @FXML
    public void hit() {
        blackjack.playerHit();
        updateView();
        getPlayerHandValue();
        System.out.println("Player " + blackjack.getPlayerHandValue());
        if(blackjack.getPlayerHandValue() > 21) {
            disableButton(true);
            label.setText("LOSE, Play Again?");
        }
        if(blackjack.getPlayerHandValue() == 21) {
            disableButton(true);
            label.setText("WON, Play Again?");
        }
    }

    @FXML
    public void stand() {
        blackjack.playerStand();
        disableButton(true);
        updateView();

        while (blackjack.getPlayer2HandValue() < 17) {
            if (blackjack.getPlayer2HandValue() == 17) {
                blackjack.dealerStand();
            } else {
                blackjack.dealerHit();
            }
            updateView();
        }
            System.out.println("Dealer " + blackjack.getPlayer2HandValue());
            if (blackjack.getPlayer2HandValue() > 21) {
                label.setText("WON, Play Again?");
            }
            if (blackjack.getPlayer2HandValue() < blackjack.getPlayerHandValue() && blackjack.getPlayerHandValue() < 21) {
                label.setText("WON, Play Again?");
            }
            if (blackjack.getPlayer2HandValue() == 21) {
                label.setText("LOSE, Play Again?");
            }
            if (blackjack.getPlayer2HandValue() > blackjack.getPlayerHandValue() && blackjack.getPlayer2HandValue() < 21) {
                label.setText("LOSE, Play Again?");
            }
            if (blackjack.getPlayer2HandValue() == blackjack.getPlayerHandValue()) {
                label.setText("DRAW, Play Again?");
            }
            updateView();
        }
    @FXML
    public void getPlayerHandValue() {
        playerScore.setText(String.valueOf(blackjack.getPlayerHandValue()));
    }
    @FXML
    public void getDealerHandValue() {
         dealerScore.setText(String.valueOf(blackjack.getPlayer2HandValue()));
    }

}

