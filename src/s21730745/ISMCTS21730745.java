package s21730745;

import java.util.*;

//Information Set Monte Carlo Tree Search
public class ISMCTS21730745{
	private static Random random;
	private static Node21730745 root;
	private static String randomSpies;
	private static double stopwatch;
	private static double totalTime;
	
	public ISMCTS21730745(){
		random = new Random();
	}
	
	//Main loop
	public static Action21730745 run(State21730745 startingState, boolean spy){
			//Ensure total time does not exceed 97 milliseconds
			totalTime = 0;
			root = new Node21730745(startingState);
			
			while ( totalTime < 97){
				stopwatchOn();
				//randomise unknowns if not spy
				if (!spy){
					randomSpies = startingState.randomiseUnkowns();
				}
				//begin search
				select(startingState, root);
				stopwatchOff();
			}
			//return action
			return finalChoice(root);

	}
	
	//iteration of ISMCTS
	private static void select(State21730745 currentState, Node21730745 currentNode){
		//Exploration
		Map.Entry<State21730745,Node21730745> pair = treePolicy(currentState,currentNode);
		//Simulation
		int score = simulate(pair.getValue(), pair.getKey());
		//Back Propagation
		Node21730745 n = pair.getValue();
		n.backUp(score);
		
	}
	
	//tree policy, randomly choosing nodes
	private static Map.Entry<State21730745,Node21730745> treePolicy(State21730745 s, Node21730745 n){
		while(!s.getGameOver()){
			//expand node if it hasn't been already
			if (n.unvisitedChildren.isEmpty() && n.children.isEmpty()){
				s.getRandomUnknowns(randomSpies);
				n.expandNode(s);
			}
			//if there is still unvisited children, visit them
			if (!n.unvisitedChildren.isEmpty()){
				Node21730745 temp = n.unvisitedChildren.remove(random.nextInt(n.unvisitedChildren.size()));
				s.doAction(temp.action);
				return new AbstractMap.SimpleEntry<>(s,temp);
			}
			
			//randomly choose a node from the children
			Node21730745 chosenNode = n.children.get((int)(Math.random()*n.children.size()));
			n = chosenNode;
			s.doAction(chosenNode.action);
		}
		return new AbstractMap.SimpleEntry<>(s,n);
	}
	
	//simulation phase: simulate until terminal states using random decisions. use scores to back propagate
	private static int simulate(Node21730745 n, State21730745 s){
		ArrayList<Action21730745> actions;
		Action21730745 a;
		State21730745 state = s.duplicate();
		state.nominatedTeam = s.nominatedTeam;
		while(!state.getGameOver()){
			actions = state.getActions();
			a = actions.get(random.nextInt(actions.size()));
			state.getRandomUnknowns(randomSpies);
			state = state.doAction(a);
		}
		return (state.getWin() ? 1 : 0);
	}
	
	//make the final decision for which action to undergo in actual game
	private static Action21730745 finalChoice(Node21730745 n){
		double bestValue = Double.NEGATIVE_INFINITY;
		double tempBest;
		ArrayList<Node21730745> bestNodes = new ArrayList<Node21730745>();

		for (Node21730745 s : n.children) {
			tempBest = s.score;
			if (tempBest > bestValue) {
				bestNodes.clear();
				bestNodes.add(s);
				bestValue = tempBest;
			}
			else if (tempBest == bestValue) {
				bestNodes.add(s);
			}
		}		
		Node21730745 finalNode = bestNodes.get(random.nextInt(bestNodes.size()));
		bestValue = Double.NEGATIVE_INFINITY;
		for (Node21730745 s : bestNodes){
			tempBest = (double)s.visited;
			if (tempBest > bestValue){
				finalNode = s;
			}
		}
		
		//threshold for not betraying as spy 
		if (finalNode.action.phase == Phase21730745.Betray){
			if (finalNode.action.action == "no"){
				if (finalNode.score < 0.25){
					return new Action21730745(Phase21730745.Betray,"yes");
				}
			}
		}
		return finalNode.action;
	}
	
	//stopwatch methods
	private static void stopwatchOn(){
		stopwatch = System.currentTimeMillis();
	}
	
	//stopwatch method
	private static void stopwatchOff(){
		totalTime += System.currentTimeMillis()-stopwatch;
	}
	
}