//JESSICA WONG 10181646
//CMPE365 - FINAL PROJECT

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Mahjong {
	
	private ArrayList<String> hand = null;
    private String discard = new String();
    private ArrayList<String> throwOut = new ArrayList<String>();
    private ArrayList<String> pair = new ArrayList<String>(); //Locate all the pairs (MUST HAVE ONE PAIR)
	private ArrayList<String> trips = new ArrayList<String>();
	private ArrayList<String> straight = new ArrayList<String>();
	
	private ArrayList<String> keeper = new ArrayList<String>();
	private ArrayList<String> temp = new ArrayList<String>();
	
	ArrayList<String> checkDG = new ArrayList<String>();	//checkDG holds all tiles where 2 tiles were discarded
	ArrayList<String> checkTG = new ArrayList<String>();	//checkTG holds all tiles where 3 of its type went by (Triple garbage)
	ArrayList<String> quad = new ArrayList<String>();		// if all 4 tiles have gone by
	static ArrayList<String> masterGarbage = new ArrayList<String>();
	
	private String[] tileSearch = {"B","C","W"};
    
	public void setupInit(ArrayList<String> hand){
		
		char[][] hold = new char[3][14];
		ArrayList<String> search = new ArrayList<String>();
		
		temp.addAll(hand);
		
		int sizeHand = temp.size();
		for (int i = 0; i<sizeHand-2; i++){ 
			if (temp.get(i).equals(temp.get(i+1))){ 			// LOCATE PAIR & TRIPLES
				if ((i+2)!=sizeHand){
					if (!temp.get(i+1).equals(temp.get(i+2))){	// LOCATE PAIRS THAT ARE NOT TRIPLES
					pair.add(temp.get(i));
					pair.add(temp.get(i+1));
					i++;
					}else{										//LOCATE TRIPLE
						trips.add(temp.get(i));
						trips.add(temp.get(i+1));
						trips.add(temp.get(i+2));

						pair.add(temp.get(i));					// SAVE AS PAIR AS WELL AS TRIPLE
						pair.add(temp.get(i+1));
						i++;
						i++;
					}
				}else{
					pair.add(temp.get(i));
					pair.add(temp.get(i+1));
					i++;
				}
			}
		}
		
		
		for(int j=0; j<3; j++){								//Search through tiles for B, C, W 
			search.clear();
			for(int i=0; i<temp.size(); i++){
				if(temp.get(i).contains(tileSearch[j])) {
			        search.add(temp.get(i));
				}
			}
			for(int i=0; i<hold[j].length; i++){ 
				if(search.size()>i){
						hold[j][i] = search.get(i).charAt(0); 
				}
			}
			search = new ArrayList<String>(new LinkedHashSet<String>(search));
			//search.removeAll(pair);
			if (hold[j].length>=3){
				for(int i=0; i<search.size()-1; i++){
					if (Character.getNumericValue(hold[j][i]) == ((Character.getNumericValue(hold[j][i+1]))-1) && ((Character.getNumericValue(hold[j][i+1]))-1) == (Character.getNumericValue(hold[j][i+2]))-2 && i<search.size()-2){
						for (int k = 0; k<3; k++){
							straight.add(search.get(i+k));
						}
						i++;
					}else if(Character.getNumericValue(hold[j][i]) == (Character.getNumericValue(hold[j][i+1])-1) || Character.getNumericValue(hold[j][i])==Character.getNumericValue(hold[j][i+1])-2 && i<search.size()-1){
						for (int k = 0; k<2; k++){
							keeper.add(search.get(i+k));
						}
					}
					i++;
				}
			}
		}
		
		if(pair.size()>2){					// Have at least one pair
			//IF TWO PAIRS, KEEP THE PAIR WITH MORE POTENTIAL (AKA NO THROW OUT MATCHES)
			pair.removeAll(trips);
			pair.removeAll(straight);
			trips.removeAll(straight);
		}else{
			trips.removeAll(pair);
			straight.removeAll(pair);
			straight.removeAll(trips);	
		}
		
		/*//When the straight removes the values from pairs/trips, remove one! add other to temp
		Set<String> doubles = new HashSet<String>();
		doubles = findDup(temp);
		
		temp.removeAll(straight);
		//temp.addAll(doubles);*/
		
		temp.removeAll(pair);
		temp.removeAll(trips);
		temp.removeAll(straight);
		temp.removeAll(keeper);
		
		/*System.out.println("Pairs: " + pair);
		System.out.println("Triples: " + trips);
		System.out.println("Straight: " + straight);
		System.out.println("Keeper: " + keeper);
		System.out.println("Temp after: " +temp);
		System.out.println("");*/
	}
	
	
public void bestPlay(ArrayList<String> hand, ArrayList<String> pickup){
		temp.sort(String::compareToIgnoreCase);
		
		char[] hold = new char[14];
		ArrayList<String> search = new ArrayList<String>();
		ArrayList<String> checkPair = new ArrayList<String>();
		discard = "";													 //Reset discard tile
		int track = 0;
		
		//track 1 = pair from temp
		//track 2 = triple from pair
		//track 3 = pair from keeper
		//track 4 = straight
		//track 6 = D/X and does not find (pair/trip) in keeper, temp
		
		//track 8 = if there is potential in a straight using temp
		//track 9 = If the pickup tile is found in a straight, keep it around 
		//track 10 = quadruple found
		
		String pickupString = new String();
		pickupString = pickup.get(0);
		
		checkPair.addAll(temp);
		checkPair.retainAll(pickup);

		/*//check pair
		for (int i = 0; i< checkme.size(); i++){
			if ((i+2)<checkme.size()-1){
				//Not a triple
				if (checkme.get(i).equals(checkme.get(i+1)) && (checkme.get(i+2).equals(checkme.get(i))) == false){
					pair.add(checkme.get(i));
					pair.add(checkme.get(i));
					i++;
				}
			}else if ((i+1) == checkme.size()-1){
				//Double at the end of the array
				if (checkme.get(i).equals(checkme.get(i+1))){
					pair.add(checkme.get(i));
					pair.add(checkme.get(i));
					i = checkme.size();
				}
			}
		}
		
		//check triple
		for (int i = 0; i< checkme.size(); i++){
			if ((i+2)<checkme.size()-1){
				//It is a triple, but not 4 
				if (checkme.get(i).equals(checkme.get(i+1)) && checkme.get(i+2).equals(checkme.get(i)) && (checkme.get(i+3).equals(checkme.get(i))) == false){
					trips.add(checkme.get(i));
					trips.add(checkme.get(i));
					trips.add(checkme.get(i));
					i++;
					i++;
				}else{
					i++;
					i++;
					
				}
			}else if ((i+2) == checkme.size()-1){
				//Triple at end of array
				if (checkme.get(i).equals(checkme.get(i+1)) && checkme.get(i+2).equals(checkme.get(i))){
					trips.add(checkme.get(i));
					trips.add(checkme.get(i));
					trips.add(checkme.get(i));
					i = checkme.size();
				}
			}
		}*/
		if (trips.contains(pickup) || quad.size()>0){											// IF THE FOURTH TILE GOES BY
			discard = pickupString;
			pickup.clear();
			track = 10;		
		}else if (checkPair.size()>1){											// LOCATE PAIR FROM TEMP
			pair.addAll(checkPair);
			pair.addAll(pickup);
			pickup.clear();
			track = 1;
		}else{
			checkPair.clear();
			checkPair.addAll(pair);
			checkPair.retainAll(pickup);
			if (checkPair.size()>1){										// LOCATE TRIP FROM PAIR
				for(int i = 0; i<3; i++){
					trips.add(checkPair.get(0));
				}
				pickup.clear();
				track = 2;
			}else{
				if (pickupString.contains("X") || pickupString.contains("D")){				// DISCARD PICKUP IF IT IS A DRAGON/WIND & DOES NOT MATCH PAIR/TRIP
					discard = pickupString;
					track = 6;
				}else{
					for(int j = 0; j<3; j++){												// SEARCH IF IT FILLS A STRAIGHT / MATCHES A PAIR
						if (pickupString.contains(tileSearch[j])){
							search.clear();
							int lookFor = Character.getNumericValue(pickupString.charAt(0));
							for(int i=0; i<keeper.size(); i++){								//Initialize search with tiles containing tileSearch(B,C,W)
								if(keeper.get(i).contains(tileSearch[j])) {
							        search.add(keeper.get(i));   
								}
							}
							for (int i = 0; i<search.size(); i++){
								hold[i] = (search.get(i)).charAt(0);
							}
							for (int i=0; i<hold.length-1; i++){
								//look for tile +1 and -1
								//look for tile -2 and -1
								//look for tile +1 and +2
								if (Character.getNumericValue(hold[i]) == lookFor){				// FOUND A PAIR IN KEEPER
									if (i%2==0){						// REMOVE THE MATCHING TILE OF THE NEWLY FOUND PAIR
						        		temp.add(search.get(i+1));
							        	keeper.remove(search.get(i+1));
							        }else{
							        	temp.add(search.get(i-1));
							        	keeper.remove(search.get(i-1));
							        }
									track = 3;
									pair.addAll(pickup);
									pair.add(search.get(i));
									keeper.remove(search.get(i));
									pickup.clear();
								}else if ((Character.getNumericValue(hold[i]) == lookFor-1 && Character.getNumericValue(hold[i+1]) == lookFor+1)   ||    (Character.getNumericValue(hold[i]) == lookFor-2 && Character.getNumericValue(hold[i+1]) == lookFor-1)    ||    (Character.getNumericValue(hold[i]) == lookFor+1 && Character.getNumericValue(hold[i+1]) == lookFor+2)){
									straight.add(search.get(i));
									straight.add(search.get(i+1));
									track = 4;
									straight.addAll(pickup);
									pickup.clear();
								}
							}
							search.clear();
							
							if (track == 0){
								for(int i=0; i<temp.size(); i++){								//Initialize search with tiles containing tileSearch(B,C,W)
									if(temp.get(i).contains(tileSearch[j])){
								        search.add(temp.get(i));
									}
								}
								for (int i = 0; i<search.size(); i++){
									hold[i] = search.get(i).charAt(0);
								}
								for (int i=0; i<search.size(); i++){
									if ((Character.getNumericValue(hold[i]) == lookFor+1)   ||   (Character.getNumericValue(hold[i]) == lookFor+2)){
										keeper.add(pickupString);
										keeper.add(search.get(i));
										temp.remove(search.get(i));
										pickup.clear();
										track = 8;
										break;	
									}else if((Character.getNumericValue(hold[i]) == lookFor-1)   ||  (Character.getNumericValue(hold[i]) == lookFor-2)){
										keeper.add(search.get(i));
										temp.remove(search.get(i));
										keeper.add(pickupString);
										pickup.clear();
										track = 8;
										break;	
									}
								}
							}
						}
					}
				}	
			}
		}
		//track 1 = pair from temp
		//track 2 = triple from pair
		//track 3 = pair from keeper
		//track 4 = straight from keeper
		//track 6 = Dragon/Wind found and does not match anything
		
		//track 8 = if there is potential in a straight using temp add to keeper
		//track 9 = If the pickup tile is found in a straight, keep it around 
		//track 10 = quadruple found
		
		if (track == 1){ 								// found a pair from temp
			temp.removeAll(pair);
		}else if (track == 2){ 							// found a trip from pair
			pair.removeAll(trips);
		}else if (track == 3){							// found a pair from keeper
			keeper.remove(pair);
		}else if (track ==4){							// found a straight from keeper
			keeper.removeAll(straight);
		}else if (track == 0){
			if(straight.contains(pickup) && temp.size()>0){
				track = 9;
				discard = temp.get(0);
				temp.remove(0);
				temp.add(pickupString);
				pickup.clear();
			}
		}
		
		ArrayList<String> check = new ArrayList<String>();		//check holds all tiles where at least 1 tile of its type was discarded
		
		//DISCARD TILE
		if (temp.isEmpty() && keeper.isEmpty() && pair.size()==2 && discard==""){				//CHECK IF WINNER
			JessicaW_Mahjong.priority = 2;
			track = -1;
		}else if (track == 6 || track == 10 || track==9){						// found a dragon/wind and no pair/trip found
			throwOut.add(discard);
			hand.remove(discard);
		}else{
			if(temp.isEmpty()){						//	this means I am waiting for a certain tile
				if (track == 0){
					discard = pickupString;
					pickup.clear();
				}else if (keeper.size()>0){
					discard = keeper.get(0);
					temp.add(keeper.get(1)); 		// eliminate its match
					keeper.remove(keeper.get(1));
					keeper.remove(discard);
				}else{								// this must mean there are multiple pairs
					check.addAll(pair);
					check.retainAll(checkDG);
					if (check.size()>0){			// One of the pairs will never be a triple
						discard = check.get(0);
						pair.removeAll(check);
					}else{							// Both pairs have a potential of being a triple, get rid of whichever one
						discard = pair.get(0);
						pair.remove(discard);
					}
					//temp.add(discard);
				}
			}else if(track==8){
				discard=temp.get(0);
				temp.remove(0);
			}else{// if there are tiles in temp
				check.addAll(temp);
				check.retainAll(masterGarbage);
				if (check.size()>0){
					for(int i=0; i<check.size()-1; i++){					// DISCARD WINDS AND DRAGONS THAT ARE NOT PAIRS
						//If a discard tile went by for Winds/Dragons, discard current tile in hand
						if(check.get(i).contains("D") || check.get(i).contains("X")){
							discard = check.get(i);
						}
					}
					if (discard==""){
						discard = check.get(0);
						temp.remove(discard);
					}
				}else{											//if no current tiles have gone by
					for(int i=0; i<temp.size(); i++){					// DISCARD WINDS AND DRAGONS THAT ARE NOT PAIRS
						//If a discard tile went by for Winds/Dragons, discard current tile in hand
						if((temp.get(i)).contains("D") || (temp.get(i)).contains("X")){
							discard = temp.get(i);
						}
					}
					if (discard == ""){
						discard = temp.get(0);
					}
					temp.addAll(pickup);
				}
				temp.remove(discard);
			}

			if (discard==""){
				discard = pickupString;
			}

			if (throwOut.contains(discard)){				// If two of the same tiles have gone by (keep track)
				throwOut.remove(discard);
				checkDG.add(discard);
			}else{
				throwOut.add(discard);
			}
			masterGarbage.add(discard);
			hand.remove(discard);				
		}
		if (track!= -1){
			if (checkDG.contains(discard)){						// If three of the same tiles have gone by (keep track)
				checkDG.remove(discard);
				checkTG.add(discard);
			}else if (checkTG.contains(discard)){
				checkTG.remove(discard);
				quad.add(discard); 					//don't need to worry but for testing sake
			}
		}
		
		
		/*System.out.println("Pairs: " + pair);
		System.out.println("Triples: " + trips);
		System.out.println("Straight: " + straight);
		System.out.println("Keeper: " + keeper);
		System.out.println("Temp after: " +temp);
		System.out.println ("Discarded Tile: " + discard);
		System.out.println("After Discard: " + hand + hand.size());
		System.out.println(""); */
		
		this.hand = hand;
		//this.discard = discard;
	}
	
	public ArrayList<String> getHand(){
		return hand;
	}
	public String getDiscard(){
		return discard;
	}
}
