//JESSICA WONG 10181646
//CMPE365 - FINAL PROJECT
import java.util.ArrayList;

public class JessicaW_Mahjong {
	public static int priority = 0; //Once this is 1, the player would have won or run out of tiles
	public static int turn = 1;
	
	
	public static void main(String[] args) {
	
//INITIALIZE
		ShuffleTile t = new ShuffleTile();
		Mahjong_alg a = new Mahjong_alg();
		
//GET TILE SEQUENCE
	//FOR TESTING PURPOSE USE RANDOM TILE SET
		//ArrayList<String> play = t.Shuffle();
		//Input file path
		ArrayList<String> play = t.ReadTiles("dealsequence.csv");
		//ArrayList<String> play = t.ReadTiles("dealsequence2.csv");
		System.out.print (play + " ");
		
		
		
		
		
		
		
//SINGLE PLAYER GAME
		System.out.println("\n\nPlaying Solo Game...");
//PICK UP TILES
		//Pick up initial 13 tiles
		ArrayList<String> hand = new ArrayList<String>();
		for (int i=0; i<13; i++){
			hand.add(play.get(i));
		}
		
//SORT HAND
		hand.sort(String::compareToIgnoreCase);
		System.out.println ("Starting Hand: "+ hand + " ");
		//Initial Play (Set up your hand)
		
		//a.setupInit(hand);
		int current = 13;		//Current Tile, iterates through it 
		//ArrayList<String> pickup = new ArrayList<String>(); //Current picked up tile

//GAME PLAY
		while (priority == 0){ //|| priority == 1){ //While you have 0 pairs
			if (current == 136){ 								// Reach end of 136 tiles
				priority = 10;
			}else{
				
				/*System.out.println("");
				System.out.println ("Turn #: " + turn);
				System.out.println("Pickup: " + play.get(current)); //Display tile picked up
				System.out.println("Hand: " + hand);
				*/
				
				if(priority == 0 && hand.size()==13){
					hand.add(play.get(current));						// Add picked up tile to hand
					hand.sort(String::compareToIgnoreCase);
					hand = a.play2(hand);
				}else{
					priority = 12;
				}
				
				current++;
				turn++;
			}
		}
		
		//Final Results
		if (priority == 10){
			System.out.println("End Hand: " + hand);
			System.out.println("Oopsies! Something went wrong.");
		}else {
			System.out.println("Winning Hand: " + hand);
			System.out.println("Congratulations! Took " + (turn-1) + " turns.");
		}
		
		
		
		
		
//SET UP BY PLAYER
		int winner = 0;
		int tile = 52; 
		turn = 0;
		priority = 0;
		System.out.println("\nPlaying by player...");
		//INIT
		ArrayList<String> first = new ArrayList<String>();
		ArrayList<String> second = new ArrayList<String>();
		ArrayList<String> third = new ArrayList<String>();
		ArrayList<String> fourth = new ArrayList<String>();
		a.checkDG.clear();
		a.checkTG.clear();
		a.checkQG.clear();
		Mahjong_alg.masterGarbage.clear();
		
//PICKUP TILES
		//Pick up initial 12 tiles per player
		for(int p = 0; p<(12*4); p++){
			if (p%4 == 0){
				first.add(play.get(p));
			}else if (p%4 == 1){
				second.add(play.get(p));
			}else if (p%4 == 2){
				third.add(play.get(p));
			}else if (p%4 == 3){
				fourth.add(play.get(p));
			}
		}
		
		//Get final tile (13th tile)
		first.add(play.get(48));
		second.add(play.get(49));
		third.add(play.get(50));
		fourth.add(play.get(51));
		
		first.sort(String::compareToIgnoreCase);
		second.sort(String::compareToIgnoreCase);
		third.sort(String::compareToIgnoreCase);
		fourth.sort(String::compareToIgnoreCase);
		System.out.println("Group Play");
		System.out.println("Player 1:" + first);
		System.out.println("Player 2:" + second);
		System.out.println("Player 3:" + third);
		System.out.println("Player 4:" + fourth);
		
		
		while (priority == 0){
			if (tile == 136){ 								// Reach end of 136 tiles
				priority = 12;								//no one wins
			}else{
				//this play is by each tile, each player goes one at a time
				//also observing which tile the other players discard
				if (tile%4 == 0){							
					first.add(play.get(tile));					//add tile to hand (total count = 14tiles)
					first.sort(String::compareToIgnoreCase);	//sort tiles
					first = a.play2(first);						//use algorithm to find best move
					if (priority == 12){					//if player 1 is the winner, assign winner
						winner = 1;
					}
				}else if (tile%4 == 1){
					second.add(play.get(tile));					//add tile to hand (total count = 14tiles)
					second.sort(String::compareToIgnoreCase);	//sort tiles
					second = a.play2(second);					//use algorithm to find best move
					if (priority == 12){					//if player 2 is the winner, assign winner
						winner = 2;
					}
				}else if (tile%4 == 2){
					third.add(play.get(tile));					//add tile to hand (total count = 14tiles)
					third.sort(String::compareToIgnoreCase);	//sort tiles
					third = a.play2(third);						//use algorithm to find best move
					if (priority == 12){					//if player 3 is the winner, assign winner
						winner = 3;
					}
				}else if (tile%4 == 3){
					fourth.add(play.get(tile));					//add tile to hand (total count = 14tiles)
					fourth.sort(String::compareToIgnoreCase);	//sort tiles
					fourth = a.play2(fourth);					//use algorithm to find best move
					if (priority == 12){					//if player 4 is the winner, assign winner
						winner = 4;
					}
				}
				tile++;										//which tile is being picked up
				turn++;										//how many individual turns have gone by
			}
		}
		
		
		for(int p = 0 ; p<4; p++){ //Loop for each player
			int player = p+1;
			hand.clear();
			//initialize hand to print output 
			if (player == 1){
				hand = first;
			}else if (player == 2){
				hand = second;
			}else if (player == 3){
				hand = third;
			}else if (player == 4){
				hand = fourth;
			}
			
			//not the winner
			if (winner != player){
				System.out.println("Hand:" + hand);
				System.out.println("Sorry, Player " + player + "! You are not the winner.");
			}else{ //winner!
				System.out.println("Winning Hand: " + hand);
				System.out.println("Congratulations Player " + player + "! Took " + turn + " turns.");
			}
		}	
	}
	
}

