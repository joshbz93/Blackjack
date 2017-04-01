/**
 * Card is the class that represents a playing card used in the Blackjack card game.
 * @author Josh Boyer
 */

package game;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card extends Parent
{
	public final Rank rank;	
	public final Suit suit;
	public final int value;
	
	//The rank(and value) of the card.
	enum Rank
	{
		TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), JACK(10), QUEEN(10), KING(10), ACE(11);
		
		private final int value;	
		private Rank(int value)
		{
			this.value = value;
		}
	};
	
	//The suit of the card.
	enum Suit
	{
		CLUBS, DIAMONDS, HEARTS, SPADES;
	};

	/** 
	 * A constructor for the Card class.
	 * @param name The name of the card (e.g. eight, nine, jack, queen, etc.)
	 * @param suit The suit of the card (e.g. clubs, diamonds, hearts, spades)
	 */
	public Card(Rank rank, Suit suit)
	{
		this.rank = rank;
		this.suit = suit;
		this.value = rank.value;
		Image cardImage = new Image("file:../../images/" + rank + "_of_" + suit + ".png");
		ImageView cardImageView = new ImageView (cardImage);
		cardImageView.setFitWidth(100);
		cardImageView.setPreserveRatio(true);
		getChildren().add(new StackPane(cardImageView));
	}
	
}
