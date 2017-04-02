package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Game 
{
	private static Path filePath = Paths.get(new File("src/save/save.txt").getAbsolutePath().toString());
	private static final int MINIMUM_BET = 10;
	private static final int MAXIMUM_BET = 100;
	private static final int STARTING_MONEY = 200;
	
	//A statistic that can be saved
	enum StatToSave
	{
		money, wins, losses;
	};
	
	/**
	 * Saves the total number of Blackjack losses to "Blackjack/save/save.txt"
	 * @param player The player
	 * @param stat The statistic to be saved (money, wins, or losses)
	 */	
	public static void save(Player player, StatToSave stat)
	{
		try
		{
			List<String> fileData = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));
			int statValue = 0;
			for (int i = 0; i < fileData.size(); i++) 
			{
			    if (fileData.get(i).contains(stat.toString()))
			    {
			    	statValue = (Integer.parseInt(fileData.get(i).toString().substring(stat.toString().length() + 1, fileData.get(i).length())));

			    	if(stat.equals(StatToSave.money)) //save money
			    	{
			    		statValue = player.getMoney().get();
			    	}
			    	
			    	else if (stat.equals(StatToSave.wins)) //save wins
			    	{
			    		player.setWins(statValue + 1);
			    		statValue = player.getWins().get();
			    	}
			    	
			    	else //save losses
			    	{
			    		player.setLosses(statValue + 1);
			    		statValue = player.getLosses().get();
			    	}
			    		
			        fileData.set(i, stat + " " + statValue);				        				        
			    }
			}

			Files.write(filePath, fileData, StandardCharsets.UTF_8);
		}
		
		catch (FileNotFoundException e)
		{
			save(player, stat);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to populate the statistics area of the Blackjack game when it loads.
	 * @param player The player
	 * @param stat The stat to populate (money, wins, or losses)
	 */
	public static void populateStats(Player player, StatToSave stat)
	{
		try
		{
			List<String> fileData = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));
			int statValue = 0;
			for (int i = 0; i < fileData.size(); i++) 
			{
			    if (fileData.get(i).contains(stat.toString()))
			    {
			    	statValue = (Integer.parseInt(fileData.get(i).toString().substring(stat.toString().length() + 1, fileData.get(i).length())));
			    	
			    	if(stat.equals(StatToSave.wins)) //save money
				    {
			    		player.setWins(statValue);
				    }
				    
			    	else if (stat.equals(StatToSave.losses)) //save wins
				    {
				    	player.setLosses(statValue); 		        				        
				    }
				    
			    	else //save losses
			    	{
			    		player.setMoney(statValue);
			    	}
			    }  
			}
		}
		
		catch (Exception e)
		{
			populateStats(player, stat);		
		}
	}		
	/**
	 * Determines whether the player wins or loses a Blackjack game and saves the result and amount of money in "save.txt"
	 * @param player The player
	 * @param dealer The dealer
	 * @return A string regarding the outcome of the Blackjack game
	 */
	public static String determineOutcome(Player player, Player dealer)
	{
		String endMessage = "";		
		int dealerPoints = dealer.getPoints().get();
		int playerPoints = player.getPoints().get();
		
		if (dealerPoints == 21 && playerPoints == 21)
		{
			endMessage = "It's a tie!";
		}
		
		else if (dealerPoints == 21 || playerPoints > 21 || dealerPoints == playerPoints || (dealerPoints < 21 && dealerPoints > playerPoints))
		{
			endMessage = "The dealer won!";
			save(player, StatToSave.losses);
			player.setMoney(player.getMoney().get() - (player.getBetAmount().get()));
			
			if (player.getMoney().get() < 10)
			{
				player.setMoney(getStartingMoney());
				endMessage = "You ran out of money, but here's some more!";
			}
		}
		
		else if (playerPoints == 21 || dealerPoints > 21 || playerPoints > dealerPoints)
		{
			endMessage = "You won!";
			save(player, StatToSave.wins);
			if(playerPoints == 21)
			{
				player.setMoney((int)(Math.floor(player.getMoney().get() + (player.getBetAmount().get() * 1.5))));
			}
			else
			{
				player.setMoney(player.getMoney().get() + player.getBetAmount().get());
			}
		}
		
		save(player, StatToSave.money);
		return endMessage;
	}
	
	/**
	 * Accessor method for the minimum possible bet
	 * @return The minimum possible bet
	 */
	public static int getMinimumBet()
	{
		return MINIMUM_BET;
	}
	
	/**
	 * Accessor method for the maximum possible bet
	 * @return The maximum possible bet
	 */
	public static int getMaximumBet()
	{
		return MAXIMUM_BET;
	}
	
	/**
	 * Accessor method for the amount of money the player starts with
	 * @return The amount of money the player starts with
	 */
	public static int getStartingMoney()
	{
		return STARTING_MONEY;
	}
}

