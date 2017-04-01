/**
 * Main is the class that builds the graphics for the Blackjack card game and implements its rules.
 * @author	Josh Boyer
 */
package game;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application 
{
	private Deck deck = new Deck();
	private Player dealer, player;
	private Text message = new Text();
	private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);				
	private Button btnPlay;
	private Button btnBet;
	private TextField bet;
	/**
	 * Defines the game rules and creates the graphics
	 * @return The Blackjack game's gui
	 */
	private Parent createGame()
	{	
		HBox dealerCards = new HBox(-70);
		HBox playerCards = new HBox(-70);
		//Create left margin for dealer and player card horizontal boxes
		dealerCards.setPadding(new Insets(0, 0, 0, 10)); 
		playerCards.setPadding(new Insets(0, 0, 0, 10));
		
		//Create dealer and player objects
		dealer = new Player(dealerCards.getChildren());
		player = new Player(playerCards.getChildren());
		
		Pane root = new Pane(); //Root pane for graphical layout
		root.setPrefSize(800,600);
		HBox rootLayout = new HBox(); //Horizontal box for gameArea and controlArea layout
		
		//Create and style rectangles (used for background color)
		Rectangle gameAreaBG = new Rectangle(560, 600);
		gameAreaBG.setFill(Color.GREEN);
		Rectangle controlAreaBG = new Rectangle(240, 600);
		controlAreaBG.setFill(Color.GREEN);
		
		//Create game area
		Text dealerScore = new Text(); //show dealer's score
		Text playerScore = new Text(); //show player's score
		
		StackPane gameAreaStackPane = new StackPane();		
		VBox gameAreaVBox = new VBox(50);
		gameAreaVBox.setAlignment(Pos.TOP_CENTER);
		
		gameAreaVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore);
		gameAreaStackPane.getChildren().addAll(gameAreaBG, gameAreaVBox);
		
		//Create control area
		Text numWins = new Text(); //show player's number of wins
		Text numLosses = new Text(); //show player's number of losses
		Text numMoney = new Text(); //show player's number of dollars
		
		StackPane controlAreaStackPane = new StackPane();
		
		VBox controlAreaVBox = new VBox(20);
		controlAreaVBox.setAlignment(Pos.CENTER);
		
		btnBet = new Button("Bet"); //Bet button
		btnPlay = new Button("Play"); //Play button
		btnPlay.disableProperty().set(true);
		Button btnHit = new Button("Hit"); //Hit button
		Button btnStand = new Button("Stand"); //"Stand button
		
		//Place buttons and stats in controlArea
		bet = new TextField("");
		bet.setPrefWidth(75);
		HBox betHBox = new HBox(15);
		betHBox.setAlignment(Pos.CENTER);
		betHBox.getChildren().addAll(bet, btnBet);
		HBox buttonsHBox = new HBox(15);
		buttonsHBox.setAlignment(Pos.CENTER); 
		buttonsHBox.getChildren().addAll(btnHit, btnStand);
		controlAreaVBox.getChildren().addAll(numWins, numLosses, numMoney, betHBox, btnPlay, buttonsHBox);
		controlAreaStackPane.getChildren().addAll(controlAreaBG, controlAreaVBox);
		
		//Place gameArea and controlArea on root pane
		rootLayout.getChildren().addAll(gameAreaStackPane, controlAreaStackPane);
		root.getChildren().add(rootLayout);
		
		//Bind properties
		//btnPlay.disableProperty().bind(playable);
		btnHit.disableProperty().bind(playable.not());
		btnStand.disableProperty().bind(playable.not());			
		playerScore.textProperty().bind(new SimpleStringProperty("Player:  ").concat(player.getPoints().asString()));
		dealerScore.textProperty().bind(new SimpleStringProperty("Dealer:  ").concat(dealer.getPoints().asString()));
		numMoney.textProperty().bind(new SimpleStringProperty("Money:  $").concat(player.getMoney().asString()));
		numWins.textProperty().bind(new SimpleStringProperty("Wins:  ").concat(player.getWins()));
		numLosses.textProperty().bind(new SimpleStringProperty("Losses:  ").concat(player.getLosses()));
		
		//Set initial number of wins, losses, and dollars(money) in stats area
		player.populateStats();
		
		//Restricts user input to numeric values for the Bet TextField
		bet.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> obsValue, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                bet.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
		
		//Ends the game if the player exceeds 21 points.
		player.getPoints().addListener((obsValue, oldValue, newValue) -> 
		{
			if (newValue.intValue() >= 21)
			{
				if (playable.get() != false)
				{
					endGame();
				}
			}
		});
		
		//Ends the game if the dealer exceeds 21 points
		dealer.getPoints().addListener((obsValue, oldValue, newValue) -> 
		{
			if (newValue.intValue() >= 21)
			{
				if (playable.get() != false)
				{
					endGame();
				}
			}
		});
		
		//Restrict bets to $10-$100
		btnBet.setOnAction(event -> 
		{
			if (bet.getText().isEmpty() || Integer.parseInt(bet.getText()) < 10)
			{
				bet.setText("10");
				message.setText("The minimum bet is $10!");
			}
			
			else if (bet.getText().length() > 3 || Integer.parseInt(bet.getText()) > 100)
			{
				if (Integer.parseInt(bet.getText()) > player.getMoney().get())
				{
					message.setText("The maximum bet is $100, and you don't have enough  money!");
					bet.setText(player.getMoney().toString());
				}
				else
				{
				bet.setText("100");
				message.setText("The maximum bet is $100!");
				}
			}
			
			else if (Integer.parseInt(bet.getText()) > player.getMoney().get())
			{
				message.setText("You don't have enough money!");
				bet.setText(player.getMoney().toString());
			}
			
			else 
			{
				btnBet.disableProperty().set(true);
				bet.disableProperty().set(true);
				btnPlay.disableProperty().set(false);
			}				
		});
		
		btnPlay.setOnAction(event -> 
		{
			startGame();
		});
		
		btnHit.setOnAction(event -> 
		{
			player.drawCard(deck.removeCard());
		});
		
		btnStand.setOnAction(event ->
		{
			while (dealer.getPoints().get() < player.getPoints().get())
			{
				dealer.drawCard(deck.removeCard());
			}
			
			if (playable.get() != false)
			{
				endGame();
			}
		});
		
		return root;
	}

	/**
	 * Removes cards from the player and dealer's hands, shuffles the deck, and starts the game.
	 */
	private void startGame()
	{
		playable.set(true);
		btnPlay.setDisable(true);
		message.setText("");
		deck.resetAndShuffle();
		dealer.reset();
		player.reset();
		dealer.drawCard(deck.removeCard());
		dealer.drawCard(deck.removeCard());
		player.drawCard(deck.removeCard());
		player.drawCard(deck.removeCard());	
	}
	
	/**
	 * Determines the winner of the game, issues or deducts funds, and saves the result to "Blackjack/save/save.txt".
	 */
	private void endGame()
	{
		playable.set(false);
		btnPlay.setDisable(true);
		int dealerPoints = dealer.getPoints().get();
		int playerPoints = player.getPoints().get();
		String endMessage = "";		
		
		if (dealerPoints == 21 || playerPoints > 21 || dealerPoints == playerPoints || (dealerPoints < 21 && dealerPoints > playerPoints))
		{
			endMessage = "The dealer won!";
			player.saveLosses();
			player.setMoney(player.getMoney().get() - Integer.parseInt(bet.getText()));
			
			if (player.getMoney().get() < 10)
			{
				player.setMoney(100);
				endMessage = "You ran out of money, but here's some more!";
			}
		}
		
		else if (playerPoints == 21 || dealerPoints > 21 || playerPoints > dealerPoints)
		{
			endMessage = "You won!";
			player.saveWins();
			if(playerPoints == 21)
			{
				player.setMoney(player.getMoney().get() + Integer.parseInt(bet.getText()) * 2);
			}
			else
			{
				player.setMoney(player.getMoney().get() + Integer.parseInt(bet.getText()));
			}
		}
		
		player.saveMoney();	
		btnBet.disableProperty().set(false);
		bet.disableProperty().set(false);
		message.setText(endMessage);
	}

	@Override
	public void start(Stage primaryStage) 
	{
		primaryStage.setScene(new Scene(createGame()));
		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.setResizable(false);
		primaryStage.setTitle("BlackJack");
		primaryStage.show();
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
	
}
