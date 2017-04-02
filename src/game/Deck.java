/**
 * Deck is the class that represents a deck of 52 playing cards used in the Blackjack card game.
 * @author Josh Boyer
 */

package game;

import java.util.ArrayList;
import java.util.Collections;

import game.Card.Rank;
import game.Card.Suit; 

public class Deck 
{
	private ArrayList<Card> cards = new ArrayList<Card>(52);
	
	/**
	 * A constructor for the deck class.
	 */
	public Deck() 
	{
		resetAndShuffle();
	}
	
	/**
	 * Places one of each card in the deck and shuffles them.
	 */
	public void resetAndShuffle()
	{
		for(Rank rank: Rank.values())
		{
			for(Suit suit: Suit.values())
			{
				cards.add(new Card(rank, suit));
			}
		}
		Collections.shuffle(cards);
	}
	
	/**
	 * Removes a card from the top of the deck.
	 * @return The card removed from the top of the deck.
	 */
	public Card removeCard()
	{
		return cards.remove(0);
	}
	
}
