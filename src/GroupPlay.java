import java.util.ArrayList;

public class GroupPlay {
	
	ShuffleTile t = new ShuffleTile();
	Mahjong a = new Mahjong();
	ArrayList<String> play = t.ReadTiles("dealsequence.csv");
	System.out.print (play + " ");
	
	//SET UP BY PLAYER
			System.out.println("\nPlaying by player...");
			//INIT
			ArrayList<String> first = new ArrayList<String>();
			ArrayList<String> second = new ArrayList<String>();
			ArrayList<String> third = new ArrayList<String>();
			ArrayList<String> fourth = new ArrayList<String>();
			ArrayList<String> discard = new ArrayList<String>(); // Represents the tiles that are discarded
			
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
			
			for (int tile = 52; tile<136; tile++){
				if (tile%4 == 0){
					first.add(play.get(tile));
				}else if (tile%4 == 1){
					second.add(play.get(tile));
				}else if (tile%4 == 2){
					third.add(play.get(tile));
				}else if (tile%4 == 3){
					fourth.add(play.get(tile));
				}
				
			}
			
			
			
			for(int p = 0 ; p<4; p++){ //Loop for each player
				hand.clear();
				priority=0;
				turn=1;
				for (int i=p; i<13*4; i++){
					hand.add(play.get(i));
					i=i+3;
				}
				hand.sort(String::compareToIgnoreCase);
				System.out.println ("\nStarting Hand: "+hand + " ");
				a.setupInit(hand);
				current = 13*4;
				int offset = p;
				current = current+offset;
				while (priority == 0){ 										//While you have 0 pairs
					if (current >= 132){ 								// Reach end of 136 tiles
						priority = 10;
					}else{
						
						hand.add(play.get(current));						// Add picked up tile to hand
						hand.sort(String::compareToIgnoreCase);
						pickup.clear();
						pickup.add(play.get(current));
						if(priority == 0 && hand.size()==14){
							a.bestPlay(hand, pickup);
						}
						
						hand = a.getHand();
						//discard = a.getDiscard();
						
						current = current+4;
						turn++;
					}
				}
				int player = p+1;
				if (priority == 10){
					System.out.println("Hand:" + hand);
					System.out.println("Sorry, Player " + player + "! You are not the winner.");
				}else {
					System.out.println("Winning Hand: " + hand);
					System.out.println("Congratulations Player " + player + "! Took " + turn + " turns.");
				}
				
			}	
}
