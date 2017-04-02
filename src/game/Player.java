/**
 * Player is the class that represents a player of the Blackjack card game.
 * @author	Josh Boyer
 */

package game;

import game.Card.Rank;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Player 
{	
	private ObservableList<Node> cards; 
	private SimpleIntegerProperty points = new SimpleIntegerProperty(0);
	private int numAces = 0;
	private SimpleIntegerProperty wins = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty losses = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty money = new SimpleIntegerProperty(0);	
	private SimpleIntegerProperty betAmount = new SimpleIntegerProperty(0);	
	
	/**
	 * A constructor for the Player class
	 * @param cards An observable list of cards belonging to the player
	 */
	public Player(ObservableList<Node> cards)
	{
		this.cards = cards;
	}
	
	/**
	 * Gives the player the card from the top of the deck
	 * @param card The card from the top of the deck
	 */
	public void drawCard(Card card)
	{
		cards.add(card); 
		
		if (card.rank == Rank.ACE)
		{
			numAces++;
		}
		
		if (card.value + points.get() > 21 && numAces > 0)
		{
			points.set(card.value - 10 + points.get());
			numAces--;
		}
		
		else
		{
			points.set(card.value + points.get());
		}
	}
	
	/**
	 * Removes cards from the player
	 */
	public void reset()
	{
		cards.clear();
		points.set(0);
		numAces = 0;
	}
	
	/**
	 * Accessor method for the total points of all cards the player currently has
	 * @return The total points of all the cards the player currently has
	 */
	public SimpleIntegerProperty getPoints()
	{
		return points;
	}
	
	/**
	 * Accessor method for the total number of times the player has won a Blackjack game
	 * @return The total number of times the player has won a Blackjack game
	 */
	public SimpleIntegerProperty getWins()
	{
		return wins;
	}
	
	/**
	 * Mutator method for the total number of times the player has won a Blackjack game
	 * @param n The number of times the player has won a Blackjack game
	 */
	public void setWins(int n)
	{
		wins.set(n);
	}

	/**
	 * Accessor method for the number of times the player has lost a Blackjack game
	 * @return The number of times the player has lost a Blackjack game
	 */
	public SimpleIntegerProperty getLosses()
	{
		return losses;
	}
	
	/**
	 * Mutator method for the number of times the player has lost a Blackjack game
	 * @param n The number of times the player has lost a Blackjack game
	 */
	public void setLosses(int n)
	{
		losses.set(n);
	}
	
	/**
	 * Accessor method for the player's total amount of money
	 * @return The player's total amount of money
	 */
	public SimpleIntegerProperty getMoney()
	{
		return money;
	}
	
	/**
	 * Mutator method for the player's total amount of money
	 * @param n The amount of money to set 
	 */
	public void setMoney(int n)
	{
		money.set(n);
	}
	
	/**
	 * Mutator method for the player's bet amount
	 * @param n The player's bet amount
	 */
	public void setBetAmount(int n)
	{
		betAmount.set(n);
	}
	
	/**
	 * Accessor method for the player's bet amount
	 */
	public SimpleIntegerProperty getBetAmount()
	{
		return betAmount;
	}

			
}

