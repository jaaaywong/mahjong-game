//JESSICA WONG 10181646
//CMPE365 - FINAL PROJECT
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


//This program returns the tile set from a file or random set
public class ShuffleTile{
	private ArrayList<String> hand;
	
	//Read tiles from file, return tile set
	public ArrayList<String> ReadTiles(String file){
		ArrayList<String> play = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String currentLine;
            while ((currentLine = br.readLine()) != null) {
                play.add(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
		return play;
	}
	
	//Shuffle tiles to return randomized tile set 
	public ArrayList<String> Shuffle(){
		String[] suits = {"B","C","W"}; //B=Bamboo, C=Circle, W=Wan x4
		String[] ranks = {"1","2","3","4","5","6","7","8","9"}; 
		String[] dragons = {"1D","2D","3D"}; //DRAGONS  x4
		String[] winds = {"1X","2X","3X","4X"}; //WINDS x4
		
		ArrayList<String> play = new ArrayList<String>();
		
		for (int i=0; i<3; i++){
			for (int j=0; j<9; j++){
				for (int k = 0; k<4; k++){
					play.add(ranks[j] + suits[i]);
				}
			}
			for (int j=0; j<4; j++){
				play.add(dragons[i]);
			}
		}
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				play.add(winds[i]);
			}
		}
		Collections.shuffle(play);
		return play;
	}
	
	public ArrayList<String> getHand(){
		return hand;
	}
}