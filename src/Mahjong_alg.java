//JESSICA WONG 10181646
//CMPE365 - FINAL PROJECT

//NOTE: Search TESTING PURPOSES and uncomment the Print Lines for an explanation of what is happening during each turn

import java.util.ArrayList;

public class Mahjong_alg {
	
	private ArrayList<String> hand = null;
    private String discard = new String();
    
    private ArrayList<String> throwOut = new ArrayList<String>();		
    private ArrayList<String> pair = new ArrayList<String>(); 			//Locate all the pairs (MUST HAVE ONE PAIR)
	private ArrayList<String> trips = new ArrayList<String>();			//Locate all triples
	private ArrayList<String> straight = new ArrayList<String>();		//Locate all straights
	
	private ArrayList<String> keeper = new ArrayList<String>();			//Ideal for winning hand
	private ArrayList<String> temp = new ArrayList<String>();			//Hand to play around with
	private ArrayList<String> keepme = new ArrayList<String>();			//All tiles with potential of becoming a straight
	
	//Following ArrayLists are for the likelihood of using a tile that has already been discarded
	ArrayList<String> checkDG = new ArrayList<String>();				//checkDG holds all tiles where 2 tiles were discarded
	ArrayList<String> checkTG = new ArrayList<String>();				//checkTG holds all tiles where 3 of its type went by (Triple garbage)
	ArrayList<String> checkQG = new ArrayList<String>();				//if all 4 tiles have gone by
	static ArrayList<String> masterGarbage = new ArrayList<String>();	//List of all tiles that have gone by
	ArrayList<String> throwmeout = new ArrayList<String>();				//current tile that is being discarded

	String[] tileSearch = {"B","C","W"};
	
	private ArrayList<String> holdB = new ArrayList<String>();			//Holds all B tiles in hand
	private ArrayList<String> holdC = new ArrayList<String>();			//Holds all C tiles in hand
	private ArrayList<String> holdW = new ArrayList<String>();			//Holds all W tiles in hand
	private ArrayList<String> holdOthers = new ArrayList<String>();		//Holds all D or X tiles in hand
	
	
	//Organize by tile type
	public ArrayList<String> play2 (ArrayList<String> hand){
		//Before each turn, clear all lists
		holdB.clear();
		holdC.clear();
		holdW.clear();
		holdOthers.clear();
		temp.clear();
		keeper.clear();
		pair.clear();
		trips.clear();
		straight.clear();
		
		//Sort the hand by numerical order
		hand.sort(String::compareToIgnoreCase);
		String letter = "";			//Checks letter of the tile
		
		//Divide the tiles by type ( B,C,W,(X/D))
		for (int i = 0; i<14; i++){
			
			letter = Character.toString(hand.get(i).charAt(1));
			
			if (letter.equals("B")){ 			// B
				holdB.add(hand.get(i));
			}else if (letter.equals("C")){ 		// C
				holdC.add(hand.get(i));
			}else if (letter.equals("W")){		// W
				holdW.add(hand.get(i));
			}else{								// D or X
				holdOthers.add(hand.get(i));
			}
			
		}
		
		//TESTING PURPOSES
		/*
		System.out.println("HoldB:" + holdB);
		System.out.println("HoldC:" + holdC);
		System.out.println("HoldW" + holdW);
		System.out.println("HoldOther:" + holdOthers);
		*/
		
		//Checks for Pairs, Triples, Straights in each suit
		if (holdB.size()>=2){
			holdB = check(holdB);
		}
		if (holdC.size()>=2){
			holdC = check(holdC);
		}
		if (holdW.size()>=2){
			holdW = check(holdW);
		}
		if (holdOthers.size()>=2){
			holdOthers = check(holdOthers);
		}
		
		//In order to not interfere with hand, a temporary hand is created 
		temp.addAll(hand);
		
		//Keeper holds all found pairs, triples and straights (this will essentially be the winning hand)
		keeper.addAll(pair);
		keeper.addAll(trips);
		keeper.addAll(straight);
		
		
		//TESTING PURPOSES
		/*
		System.out.println("Pairs: " + pair);
		System.out.println("Triple: " + trips);
		System.out.println("Straight: " + straight);
		*/
		
		//After removing all of keeper from temp, the leftover tiles will be considered for discarding
		temp.removeAll(keeper);
		throwOut.clear();
		
		//All D/X tiles will be considered first for discard
		holdOthers.removeAll(keeper);
		discard = "";
		
		//TESTING PURPOSES
		//System.out.println("temp: " + temp);
		
		//Find which tile to discard first
		if (!holdOthers.isEmpty()){				//if there are D/X tiles leftover, check for garbage priority
			discard = garbage(holdOthers);			//Check garbage priority
		}else if (!temp.isEmpty()){				//if there are tiles still held in temp, check for garbage priority
			if (temp.size()>1){						//if there are more than 1 tiles found in temp
				potential(temp);					//check the potential for a straight, keep tile
				//TESTING PURPOSES
				/*System.out.println("throwmeout: " + throwmeout);
				System.out.println("keepme: " + keepme);*/
			}else{
				throwmeout.clear();
				keepme.clear();
			}
			
			if (throwmeout.size()>0){			//if throwmeout has some tiles, consider it to be discarded next
				discard = garbage(throwmeout);
			}else if (keepme.size()>0){			//if all tiles hold potential, discard the tile with the highest discard potential
				discard = garbage(keepme);
			}else{
				discard = garbage(temp);
			}
		}
		
			
		//winning hand!
		if (discard == ""){
			JessicaW_Mahjong.priority = 12;
		}else{
			//next turn, discard tile so hand holds 13
			throwOut.add(discard);
			hand.removeAll(throwOut);
			//failsafe incase there are multiple tiles being referenced (this should never be used technically)
			if (hand.size() < 13){
				for (int i = hand.size(); i<14; i++){
					hand.add(discard);
				}
			}
		}
		
		//TESTING PURPOSES
		/*
		System.out.println("Keeper: " + keeper);
		System.out.println("Discard: " + discard);
		System.out.println("Hand after: " + hand + hand.size());
		*/
		
		return hand;
	}

	//check potential, keep if it could be a straight
	public void potential (ArrayList<String> toss){
		
		ArrayList<String> checkB = new ArrayList<String>();
		ArrayList<String> checkC = new ArrayList<String>();
		ArrayList<String> checkW = new ArrayList<String>();
		ArrayList<String> checkOthers = new ArrayList<String>();
		String letter = "";
		
		//iterate through tiles sent (toss)
		for (int i = 0; i<toss.size(); i++){
			//Sort by tile type
			letter = Character.toString(toss.get(i).charAt(1));
			if (letter.equals("B")){ 			// B
				checkB.add(toss.get(i));
			}else if (letter.equals("C")){ 		// C
				checkC.add(toss.get(i));
			}else if (letter.equals("W")){		// W
				checkW.add(toss.get(i));
			}else{								// D or X
				checkOthers.add(toss.get(i));
			}	
		}
		keepme.clear();
		throwmeout.clear();
		
		//check if it could be a straight potentially
		checkPotent(checkB);
		checkPotent(checkC);
		checkPotent(checkW);
		checkPotent(checkOthers);
		
	}
	
	//Check tile values to see if there is potential for a straight
	public void checkPotent (ArrayList<String> potential){
		int num, check; 
		int repeat = 0;
		//iterate through tiles of the same type 
		if (potential.size()>1){
			for (int i = 0; i<(potential.size()); i++){
				//check to make sure the search does not go out of bounds
				if ((i+1)<=potential.size()-1){
					//num = current tile
					//check = next tile
					num = Integer.parseInt(Character.toString(potential.get(i).charAt(0)));
					check = Integer.parseInt(Character.toString(potential.get(i+1).charAt(0)));
					//repeat = last saved tile in keepme
					if (keepme.size()>1){
						repeat = Integer.parseInt(Character.toString(keepme.get(keepme.size()-1).charAt(0)));
					}
					//check if current tile is one less than the next or if its two less than the next
					//eg. num = 1, check = 2 or check = 3
					if (num+1 == check || num+2 == check){
						//if the current tile overlaps with another potential tile that has already passed
						//only add the next tile, this avoids repeats 
						if (num == repeat){
							keepme.add(potential.get(i+1));
						}else{	// add both tiles 
							keepme.add(potential.get(i));
							keepme.add(potential.get(i+1));
						}
					}else{
						//check that the current tile is not repeated, if not: add to throwmeout
						if (num != repeat){
						throwmeout.add(potential.get(i));
						}
					}
				}else if (i == (potential.size()-1)){	//this is for the last tile in the pile
					num = Integer.parseInt(Character.toString(potential.get(i).charAt(0)));
					if (keepme.size() > 0){
						repeat = Integer.parseInt(Character.toString(keepme.get(keepme.size()-1).charAt(0)));
					}
					//check if this is a repeat from another potential tile, if not: add to throwmeout
					if (num != repeat){
						throwmeout.add(potential.get(i));
					}
				}
			}
		}else if (potential.size()==1){
			throwmeout.add(potential.get(0));
		}
	}
	
	//check garbage priority
	public String garbage (ArrayList<String> toss){
		String discardTile = "";
		
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		
		list1.addAll(toss);			//holds tiles after checkGarbage is removed
		list2.addAll(toss);			//holds all the tiles removed from list1 initially
		
		//if 3 have past, get rid of tile
		list1.removeAll(checkTG);
		list2.removeAll(list1);
		//continue if no triple passes are found
		if (list2.isEmpty()){
			list1.clear();
			list2.clear();
			list1.addAll(toss);
			list2.addAll(toss);
			//if 2 have passed, get rid of tile
			list1.removeAll(checkDG);
			list2.removeAll(list1);
			//if no double passes are found
			if (list2.isEmpty()){
				list1.clear();
				list2.clear();
				list1.addAll(toss);
				list2.addAll(toss);
				//if 1 has passed, get rid of tile
				list1.removeAll(masterGarbage);
				list2.removeAll(list1);
				//if no single passes are found
				if (list2.isEmpty()){
					discardTile = toss.get(0);	//discard the first in the toss pile
				}else{
					discardTile = list2.get(0);	//discard the first in the single pass
					checkDG.add(discardTile);	//update double pass with the newly discarded
				}
			}else{
				discardTile = list2.get(0);		//discard the first in the double pass
				checkTG.add(discardTile);		//update triple pass with the newly discarded
				checkDG.removeAll(checkTG);		//remove tile from double pass
			}
		}else{
			discardTile = list2.get(0);			//discard the first in triple pass
			checkQG.add(discardTile);			//update quad pass with the newly discarded
			checkTG.removeAll(checkQG);			//remove tile from triple pass
		}
		
		//TESTING PURPOSES
		//System.out.println("list2: " + list2);
		
		masterGarbage.add(discardTile);			//update master list with the newly discarded tile
		return discardTile;
	}
	
    
	
	
	//Check for Pairs, Triples and Straight
	public ArrayList<String> check (ArrayList<String> checkme){
		
		for (int i = 0; i < checkme.size(); i++){
			if ((i+2)<checkme.size()-1){
				//Not a triple
				if (checkme.get(i).equals(checkme.get(i+1))){
					if (checkme.get(i+2).equals(checkme.get(i))){
						//if there's a quad == 2 pairs
						if (checkme.get(i+3).equals(checkme.get(i))){
							pair.add(checkme.get(i));
							pair.add(checkme.get(i));
							pair.add(checkme.get(i));
							pair.add(checkme.get(i));
							i++;
							i++;
							i++;
						}else{
							trips.add(checkme.get(i));
							trips.add(checkme.get(i));
							trips.add(checkme.get(i));
							i++;
							i++;
						}
					}else{
						pair.add(checkme.get(i));
						pair.add(checkme.get(i));
						i++;
					}
				}
			}else if ((i+2) == checkme.size()-1){
				//Double or triple at the end of the array
				if (checkme.get(i).equals(checkme.get(i+1))){
					//check if its a triple at the last 3 tiles
					if (checkme.get(i+2).equals(checkme.get(i))){
						trips.add(checkme.get(i));
						trips.add(checkme.get(i));
						trips.add(checkme.get(i));
						i++;
						i++;
					}else{	// check if its a double at the 2nd last and 3rd last tile
						pair.add(checkme.get(i));
						pair.add(checkme.get(i));
						i++;
					}
				}
			}else if ((i+1)<=checkme.size()-1){
				//check if its a pair at the last two tiles
				if (checkme.get(i).equals(checkme.get(i+1))){
					pair.add(checkme.get(i));
					pair.add(checkme.get(i));
					i++;
				}
			}
		}
		
		checkme.removeAll(pair);
		checkme.removeAll(trips);
		//check for straight with leftover tiles
		if (checkme.size()>=3){
			if (!Character.toString(checkme.get(0).charAt(1)).equals("D") && !Character.toString(checkme.get(0).charAt(1)).equals("X")){
				for (int i = 0; i< checkme.size(); i++){
					if ((i+2) <=checkme.size()-1){
						//check if current tile = next tile-1 = next next tile - 2
						if (checkme.get(i).charAt(0)+1 == checkme.get(i+1).charAt(0) && checkme.get(i+1).charAt(0)+1 == checkme.get(i+2).charAt(0)){
							straight.add(checkme.get(i));
							straight.add(checkme.get(i+1));
							straight.add(checkme.get(i+2));
							i++;
							i++;
							i++;
						}
					}
				}
			}
		}
		checkme.addAll(pair);
		checkme.addAll(trips);
		return checkme;
	}
	
	
	public ArrayList<String> getHand(){
		return hand;
	}
	
	public String getDiscard(){
		return discard;
	}
}
