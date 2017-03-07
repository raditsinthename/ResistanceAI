package s21730745;

import java.util.*;

//State: Tracks the current state of the game
public class State21730745{
	public String sName;
	public String sPlayers;
	public ArrayList<Character> playersArrayList;
	public String sSpies;
	public int sMission;
	public int sFailures;
	public Phase21730745 sPhase;
	private boolean gameOver;
	private boolean win;
	private static final int[][] missionNum = {{2,3,2,3,3},{2,3,4,3,4},{2,3,3,4,4},{3,4,4,5,5},{3,4,4,5,5},{3,4,4,5,5}};
	private static final int[] spyNum = {2,2,3,3,3,4};
	private int gameSpies;
	public String randomSpies;
	public String nominatedTeam;
	private static ArrayList<String> perms;
	private static int teamSize;
	private boolean spy;
	
	public State21730745(String name, String players, String spies, int mission, int failures, Phase21730745 phase){
		// information required for nominate phase
		sName = name;
		sPlayers = players;
		sSpies = spies;
		sMission = mission;
		sFailures = failures;
		sPhase = phase;
		gameSpies = spyNum[sPlayers.length() - 5];
		playersArrayList = new ArrayList<Character>();
		perms = new ArrayList<String>();
		spy = spies.indexOf(name)!=-1;
		//required for permutations metho
		for (int i = 0; i < sPlayers.length(); i++){
			playersArrayList.add(sPlayers.charAt(i));
		}
		//compute if game is over and who won
		if (mission > 5){
			gameOver = true;
			if (sFailures > 2){
				if (spy){
					win = true;
				}
				else {
					win = false;
				}
			}
			else{
				if (spy){
					win = false;
				}
				else{
					win = true;
				}
			}
		}
		else {
			gameOver = false;
			win = false;
			teamSize = missionNum[sPlayers.length() - 5][sMission-1];
		}
	}
	
	//duplicate state
	public State21730745 duplicate(){
		State21730745 s = new State21730745(sName, sPlayers, sSpies, sMission, sFailures, sPhase);
		s.getRandomUnknowns(randomSpies);
		return s;
	}
	
	//get possible actions based on the current phase
	public ArrayList<Action21730745> getActions(){
		ArrayList<Action21730745> validActions = new ArrayList<Action21730745>();
		if (this.sPhase == Phase21730745.Nominate){
			int totalPlayers = sPlayers.length();
			int send = missionNum[totalPlayers-5][this.sMission-1];
			//find all permutations of total players to send on mission of size missionNum[totalPlayers-5][this.sMission-1]
			permutations("", playersArrayList);
			for ( String s : perms){
				validActions.add(new Action21730745(this.sPhase, s));
			}
		}
		else if (this.sPhase == Phase21730745.Vote){
			validActions.add(new Action21730745(this.sPhase, "yes"));
			validActions.add(new Action21730745(this.sPhase, "no"));
		}
		else if (this.sPhase == Phase21730745.Betray){
			validActions.add(new Action21730745(this.sPhase, "no"));
			if (sSpies.indexOf(sName)!= -1){
				//only allow betrayal if spy
				validActions.add(new Action21730745(this.sPhase, "yes"));
			}
		}
		return validActions;
	}
	
	//simulates an action being made and creates a new state with appropriate phase and variables
	public State21730745 doAction(Action21730745 a){
		State21730745 newState;
		String nom = this.nominatedTeam;
		if (a.phase == Phase21730745.Nominate){
			newState = new State21730745(this.sName,this.sPlayers,this.sSpies,this.sMission,this.sFailures,Phase21730745.Vote);
			newState.getRandomUnknowns(randomSpies);
			newState.nominatedTeam = a.action;
		}
		else if (a.phase == Phase21730745.Vote){
			if (a.action == "yes"){
				// assume agent's vote always goes through
				// do betrays here for random spies
				//add weighted version of this based on suspcision:
				int betray = 0;
				if(!spy){
					for ( int i = 0; i < this.nominatedTeam.length(); i++){
						for (int j = 0; j < this.randomSpies.length(); j++){
							if ( nominatedTeam.charAt(i) == randomSpies.charAt(j)){
								if (Math.random() < 0.5){
									betray = 1;
								}
							}
						}
					}
				}
				newState = new State21730745(this.sName,this.sPlayers,this.sSpies,this.sMission,this.sFailures + betray,Phase21730745.Betray);
				newState.getRandomUnknowns(randomSpies);
				newState.nominatedTeam = nom;
			}
			else {
				// set threshold for voting no, rather than actually searching down path.
				newState = new State21730745(this.sName, this.sPlayers,this.sSpies,this.sMission,this.sFailures,Phase21730745.Betray);
				newState.getRandomUnknowns(randomSpies);
				newState.nominatedTeam = nom;

			}
		}
		else{
			if (a.action == "yes"){
				newState = new State21730745(this.sName,this.sPlayers,this.sSpies,this.sMission + 1,this.sFailures + 1, Phase21730745.Nominate);
				newState.getRandomUnknowns(randomSpies);
				newState.nominatedTeam = nom;

			}
			else {
				newState = new State21730745(this.sName,this.sPlayers,this.sSpies,this.sMission + 1, this.sFailures,Phase21730745.Nominate);
				newState.getRandomUnknowns(randomSpies);	
				newState.nominatedTeam = nom;	
			}
		}

		return newState;
	}
	
	//check if game over
	public boolean getGameOver(){
		return gameOver;
	}
	
	//check if win or loss
	public boolean getWin(){
		return win;
	}
	
	//randomise unknowns i.e. spies when placed on team resistance
	public String randomiseUnkowns(){
		//add a weighted version
		Random rand = new Random();
		String randomSpies = "";
		int randomSpiesNum = 0;
		while (randomSpiesNum < gameSpies){
			int num = rand.nextInt(sPlayers.length());
			if (sSpies.indexOf(sPlayers.charAt(num)) != -1){
				continue;
			}
			else if (sPlayers.charAt(num) == sName.charAt(0)){
				continue;
			}
			else {
				randomSpies += sPlayers.charAt(num);
				randomSpiesNum++;	
			}
		}
		return randomSpies;
	}
	
	//give this state random unknowns
	public void getRandomUnknowns(String randoms){
		randomSpies = randoms;
		return;
	}
	
	//give this state the nominated team
	public void getNomination(String nomination){
		nominatedTeam = nomination;
		return;
	}
	
	//find all permutations of players of a given size for nomination actions 
	public static void permutations(String curString, ArrayList<Character> tempPlayers){
			if (curString.length() == teamSize){
				perms.add(curString);
				return;
			}
			else {
				ArrayList<Character> tempPlayersCopy = new ArrayList<Character>(tempPlayers);
				char[] charArray = curString.toCharArray();
				for (int i = 0; i < charArray.length; i++){
					for (int j = 0; j < tempPlayersCopy.size(); j++){
						if (charArray[i] == tempPlayersCopy.get(j)){
							tempPlayersCopy.remove(j);
							j--;
						}
					}
				}
				for (int i = 0; i < tempPlayersCopy.size(); i = 0){
					permutations(curString+tempPlayersCopy.get(i), tempPlayersCopy);
					tempPlayersCopy.remove(i);
				}
				
			}
			
	}
}
