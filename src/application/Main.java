package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class Main extends Application {
    
    //global variables
	static boolean activeDub = true;   //if player is able to double
	boolean firstTurn = true;          //if it is player first move
	boolean activeRound = false;       //if a round is active
    @Override
	public void start(Stage stage) {
        //Game title
        stage.setTitle("Black Jack");
        
        //Starting scene
	    BorderPane startBorderPane = new BorderPane();
        Scene startScene = new Scene(startBorderPane, 640, 480);
        Pane startPane = new Pane();
        startBorderPane.setCenter(startPane);
        BorderPane.setAlignment(startPane, Pos.CENTER);
        
        //Starting scene features
        Label welcomeLabel = new Label("Welcome to Black Jack!");
        welcomeLabel.setStyle("-fx-font-size: 20pt");
        startBorderPane.setTop(welcomeLabel);
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);
        Button start = new Button("Start");
        startPane.getChildren().add(start);
        start.setLayoutX(310);
        start.setLayoutY(300);
        
        //Balance scene
        BorderPane balanceBorderPane = new BorderPane();
        Scene balanceScene = new Scene(balanceBorderPane, 640, 480);
        Pane balancePane = new Pane();
        balanceBorderPane.setCenter(balancePane);
        BorderPane.setAlignment(balancePane, Pos.CENTER);
        
        //Balance scene features
        TextField balText = new TextField("Enter your starting balance:");
        balancePane.getChildren().add(balText);
        balText.setLayoutX(270);
        balText.setLayoutY(240);
        Button enter = new Button("Enter");
        balancePane.getChildren().add(enter);
        enter.setLayoutX(300);
        enter.setLayoutY(280);
        
        //Bet scene
        BorderPane betBorderPane = new BorderPane();
        Scene betScene = new Scene(betBorderPane, 640, 480);
        Pane betPane = new Pane();
        betBorderPane.setCenter(betPane);
        BorderPane.setAlignment(betPane, Pos.CENTER);
        
        //Betting scene features
        TextField betText = new TextField("Enter your bet");
        betPane.getChildren().add(betText);
        betText.setLayoutX(280);
        betText.setLayoutY(240);
        Button bet = new Button("Bet");
        betPane.getChildren().add(bet);
        bet.setLayoutX(320);
        bet.setLayoutY(280);
        Label balanceLabel = new Label("Balance: 0");
        balanceLabel.setStyle("-fx-font-size: 30pt");
        BorderPane.setAlignment(balanceLabel, Pos.TOP_RIGHT);
        betBorderPane.setTop(balanceLabel);
        
        //Game scene
        BorderPane gameBorderPane = new BorderPane();
        Scene gameScene = new Scene(gameBorderPane, 640, 480);
        Pane gamePane = new Pane();
        gameBorderPane.setCenter(gamePane);
        BorderPane.setAlignment(gamePane, Pos.CENTER);
        
        //Game scene features
        Label balanceLabel1 = new Label("Balance: 0");
        balanceLabel1.setStyle("-fx-font-size: 30pt");
        BorderPane.setAlignment(balanceLabel1, Pos.TOP_RIGHT);
        gameBorderPane.setTop(balanceLabel1);
        Label betLabel = new Label("Bet: 0");
        betLabel.setStyle("-fx-font-size: 30pt");
        BorderPane.setAlignment(betLabel, Pos.BOTTOM_RIGHT);
        gameBorderPane.setBottom(betLabel);
        Label dealerCards = new Label("Dealer Hand: ");
        dealerCards.setStyle("-fx-font-size: 20pt");
        gamePane.getChildren().add(dealerCards);
        dealerCards.setLayoutX(180);
        dealerCards.setLayoutY(100);
        Label playerCards = new Label("Player Hand: ");
        playerCards.setStyle("-fx-font-size: 20pt");
        gamePane.getChildren().add(playerCards);
        playerCards.setLayoutX(180);
        playerCards.setLayoutY(250);
        Label gameMessage = new Label("");
        gamePane.getChildren().add(gameMessage);
        gameMessage.setStyle("-fx-font-size: 16pt");
        gameMessage.setLayoutX(270);
        gameMessage.setLayoutY(180);
        
        //Game buttons
        Button hit = new Button("Hit");
        Button stand = new Button("Stand");
        Button dub = new Button("Double");
        Button playAgain = new Button("Play again");
        Button exit = new Button("Exit");
        gamePane.getChildren().add(hit);
        gamePane.getChildren().add(stand);
        gamePane.getChildren().add(dub);
        gamePane.getChildren().add(playAgain);
        gamePane.getChildren().add(exit);
        dub.setLayoutX(360);
        dub.setLayoutY(320);
        hit.setLayoutX(260);
        hit.setLayoutY(320);
        stand.setLayoutX(300);
        stand.setLayoutY(320);
        exit.setLayoutX(1000);
        exit.setLayoutY(500);
        playAgain.setLayoutX(280);
        playAgain.setLayoutY(360);
        
        //Button groups for organization
        Button[] dubControl = {dub};
        Button[] basicControl = {hit,stand};
        Button[] allControl = {dub, hit, stand};
        Button[] againControl = {playAgain};
        
    
        //Button actions
        start.setOnAction(event->{
            stage.setScene(balanceScene);
        });
  
        enter.setOnAction(event->{
            //Read balance
            BlackJack.balance = Integer.parseInt(balText.getText());
            //Check valid balance
            if(BlackJack.balance > 0 && BlackJack.balance < Integer.MAX_VALUE) {
                balanceLabel.setText("Balance: "+BlackJack.balance);
                balanceLabel1.setText("Balance: "+BlackJack.balance);
                stage.setScene(betScene);
            }

        });
        
        bet.setOnAction(event->{
            //Getting the bet
            BlackJack.bet = Integer.parseInt(betText.getText());
            //check valid bet
            if(BlackJack.bet > 0 && BlackJack.bet <= BlackJack.balance) {
                stage.setScene(gameScene);
                
                BlackJack.balance = BlackJack.balance - BlackJack.bet; //set balance
                
                betLabel.setText("Bet: " + BlackJack.bet);
                balanceLabel1.setText("Balance: "+BlackJack.balance);
                
                //Check if double is available
                if(BlackJack.balance < BlackJack.bet) {
                    hideButtons(dubControl);
                    activeDub = false;
                }
                activeRound = true;
                BlackJack.roundStart();
                playerCards.setText(BlackJack.displayPlayerHand(BlackJack.playerHand));
                dealerCards.setText(BlackJack.displayDealerHand(BlackJack.dealerHand, false));
                hideButtons(againControl);
                
                //check for bj
                if(BlackJack.checkBJ(BlackJack.playerHand)) {
                    if(!BlackJack.checkBJ(BlackJack.dealerHand)) {
                        BlackJack.balance = (int)(BlackJack.balance + BlackJack.bet*2.5);
                        BlackJack.playerTurn = false;
                        gameMessage.setText("Black Jack!");
                        gameMessage.setTextFill(Color.GREEN);
                    }else{
                        BlackJack.playerTurn = false;
                        gameMessage.setText("Push");  
                        gameMessage.setTextFill(Color.BLUE);
                        BlackJack.balance = BlackJack.balance+BlackJack.bet;
                    }
                    hideButtons(allControl);
                    showAgain(playAgain);
                    balanceLabel.setText("Balance: "+BlackJack.balance);
                    balanceLabel1.setText("Balance: "+BlackJack.balance);
                }else if(BlackJack.checkBJ(BlackJack.dealerHand)) {
                    BlackJack.playerTurn = false;
                    dealerCards.setText(BlackJack.displayDealerHand(BlackJack.dealerHand, true));
                    gameMessage.setText("Dealer Black Jack");
                    gameMessage.setTextFill(Color.RED);
                    hideButtons(allControl);
                    activeRound = false;
                    if(BlackJack.balance > 0 && !activeRound) {
                        showAgain(playAgain);
                    }else {
                        showExit(exit);
                    }
                    balanceLabel.setText("Balance: "+BlackJack.balance);
                    balanceLabel1.setText("Balance: "+BlackJack.balance);
                }
            }
        });
        hit.setOnAction(event->{
            BlackJack.hit();
            playerCards.setText(BlackJack.displayPlayerHand(BlackJack.playerHand));
            if(BlackJack.getCardTotal(BlackJack.playerHand) > 21) {
                 gameMessage.setText("Bust! You lose.");
                 gameMessage.setTextFill(Color.RED);
                 hideButtons(allControl);
                 activeRound = false;
                 if(BlackJack.balance > 0 && !activeRound) {
                     showAgain(playAgain);
                 }else {
                     showExit(exit);
                 }
            }
            if(activeDub && firstTurn) {
                hideButtons(dubControl);
            }
        });
        stand.setOnAction(event->{
            BlackJack.dealerTurn();
            dealerCards.setText(BlackJack.displayDealerHand(BlackJack.dealerHand, true));
            gameMessage.setText(checkWin(1));
            if(checkWin(0)=="Player win!") {
                gameMessage.setTextFill(Color.GREEN);
            }else if(checkWin(0)=="Push!") {
                gameMessage.setTextFill(Color.BLUE);
            }else {
                gameMessage.setTextFill(Color.RED);
            }
            balanceLabel1.setText("Balance: "+BlackJack.balance);
            
            hideButtons(allControl);
            activeRound = false;
            if(BlackJack.balance > 0 && !activeRound) {
                showAgain(playAgain);
            }else {
                showExit(exit);
            }
            
        });
        dub.setOnAction(event->{
            BlackJack.balance = BlackJack.balance - BlackJack.bet;
            BlackJack.bet +=BlackJack.bet;
            betLabel.setText("Bet: " + BlackJack.bet);
            balanceLabel1.setText("Balance: "+BlackJack.balance);
            BlackJack.dub();
            playerCards.setText(BlackJack.displayPlayerHand(BlackJack.playerHand));
            BlackJack.dealerTurn();
            dealerCards.setText(BlackJack.displayDealerHand(BlackJack.dealerHand, true));
            gameMessage.setText(checkWin(1));
            if(checkWin(0)=="Player win!") {
                gameMessage.setTextFill(Color.GREEN);
            }else if(checkWin(0)=="Push!") {
                gameMessage.setTextFill(Color.BLUE);
            }else {
                gameMessage.setTextFill(Color.RED);
            }
            balanceLabel1.setText("Balance: "+BlackJack.balance);
            hideButtons(allControl);
            activeRound = false;
            if(BlackJack.balance > 0 && !activeRound) {
                showAgain(playAgain);
            }else {
                showExit(exit);
            }
        });
        playAgain.setOnAction(event->{
            BlackJack.reset();
            setButtons(allControl);
            stage.setScene(betScene);
            gameMessage.setText("");
            balanceLabel.setText("Balance: "+BlackJack.balance);
            balanceLabel1.setText("Balance: "+BlackJack.balance);
        });

        exit.setOnAction(event -> {
            Platform.exit();
        });
        
             
        stage.setScene(startScene);       
        stage.show();
	}
    
    //Hides buttons
    public static void hideButtons(Button[] buttons) {
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setLayoutX(buttons[i].getLayoutX()+700);
        }
    }
    //Hides button
    public static void hideButton(Button buttons) {
        
        buttons.setLayoutX(buttons.getLayoutX()+700);
        
    }
    //Shows buttons
    public static void showButtons(Button[] buttons) {
        for(int i =0; i < buttons.length; i++) {
            buttons[i].setLayoutX(buttons[i].getLayoutX()-700);
        }
    }
    //shows buttons
    public static void setButtons(Button[] buttons) {
        if(BlackJack.balance < BlackJack.bet || !activeDub) {
            hideButton(buttons[0]);
            activeDub = false;
            buttons[1].setLayoutX(260);
            buttons[1].setLayoutY(320);
            buttons[2].setLayoutX(300);
            buttons[2].setLayoutY(320);
        }else {
            buttons[0].setLayoutX(360);
            buttons[0].setLayoutY(320);
            buttons[1].setLayoutX(260);
            buttons[1].setLayoutY(320);
            buttons[2].setLayoutX(300);
            buttons[2].setLayoutY(320);
        }
    }
    //shows again button
    public static void showAgain(Button button) {
        button.setLayoutX(280);
        button.setLayoutY(360);
    }
    //Shows exit button
    public static void showExit(Button button) {
        button.setLayoutX(280);
        button.setLayoutY(360);
    }
    //Checks BJ hand results
    public static String checkWin(int val) {
        int dealer = BlackJack.getCardTotal(BlackJack.dealerHand);
        int player = BlackJack.getCardTotal(BlackJack.playerHand);
        if(dealer>21) {
            BlackJack.balance = BlackJack.balance + BlackJack.bet*2*val;
            return "Player win!";
        }else if(player>dealer) {
            BlackJack.balance = BlackJack.balance + BlackJack.bet*2*val;
            return "Player win!";
        }else if(dealer>player){
            return "Dealer win!";
        }else {
            BlackJack.balance = BlackJack.balance + BlackJack.bet*val;
            return "Push!";
        }
    }
	public static void main(String[] args) {
		Application.launch();
	}
}
