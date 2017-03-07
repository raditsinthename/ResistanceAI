package s21730745;

import java.util.*;

public class Node21730745{
	public Action21730745 action;
	public ArrayList<Node21730745> unvisitedChildren;
	public ArrayList<Node21730745> children;
	public ArrayList<Action21730745> legalActions;
	public Node21730745 parent;
	public double score;
	public int scoreNum;
	public int visited;
	
	//root node
	public Node21730745(State21730745 s) {
		children = new ArrayList<Node21730745>();
		unvisitedChildren = new ArrayList<Node21730745>();
		score = 0;		
	}
	
	// other nodes
	public Node21730745(State21730745 s, Action21730745 a, Node21730745 nParent){
		children = new ArrayList<Node21730745>();
		unvisitedChildren = new ArrayList<Node21730745>();
		parent = nParent;
		action = a;
		State21730745 tempState = s.duplicate();
		tempState.nominatedTeam = s.nominatedTeam;
		tempState.randomSpies = s.randomSpies;
		tempState.doAction(a);
	}
	
	// back propagate
	public void backUp(int scoreVal) {
		if (parent != null){
			this.visited++;
			this.scoreNum += scoreVal;
			this.score = (double)this.scoreNum / (double)this.visited;
			parent.backUp(scoreVal);
		}
		else {
			this.visited++;
			this.scoreNum += scoreVal;
			this.score = (double)this.scoreNum / (double)this.visited;
		}
	}
	
	// expand node
	public void expandNode(State21730745 currentState){
		ArrayList<Action21730745> legalActions = currentState.getActions();
		unvisitedChildren = new ArrayList<Node21730745>();
		children = new ArrayList<Node21730745>();
		for (int i = 0; i < legalActions.size(); i++){
			Node21730745 addState = new Node21730745(currentState, legalActions.get(i), this);
			unvisitedChildren.add(addState);
			children.add(addState);
		}
	}
}