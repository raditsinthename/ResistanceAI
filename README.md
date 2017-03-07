# ResistanceAI
An Artificial Intelligent agent created to play a simplified version of Resistance.

This was created for CITS3001 Agents, Algorithms and Artificial Intelligence at The University of Western Australia

Gameplay programmed by Timothy French (Lecturer). Agent (code in s21730745 folder) created by myself.

The agent takes advantage of Monte Carlo Tree Search, an algorithm that simulates many plays of a game from a given state and chooses the move with the highest probability of leading to a win.


After researching Bayes Rule, Markov Models and Monte Carlo Tree Search (MCTS) I chose to use MCTS. The reasons this was chosen were:
1.	MCTS and adaptations of the basic algorithm have been implemented for general game playing (GGP) competitions and have had great success since its conception. For example, AlphaGo, able to beat a professional player in a full game of Go, is based on MCTS. When Monte Carlo Tree Search and specifically the Upper Confidence Bounds Applied to Trees adaptation was introduced to GGP competitions, a large peak in game playing ability occurred (Genesereth and Bjornsson, 2012). Cadioplayer won the The International General Game Playing Competition three times in a row with a MCTS implementation and according to (Genesereth and Bjornsson, 2012), “almost every general game playing program today uses some version of Monte Carlo”.  Although this doesn’t apply to the project, I was interested in the general game playing side of MCTS and as little heuristic knowledge of the game is required, it helped me to start implementing an algorithm with only a slight understanding of the Resistance game play.
2.	As a time limit was imposed on decision making in the project, I was inclined to choose an algorithm that adapted to these limits to make the most of the time it was given. MCTS can run indefinitely, becoming (ideally) more accurate over time with more iterations rather than having a set amount of time to make a decision (be it longer or shorter than the limit given).
3.	During my research, I found only a few instances where MCTS was implemented in games with imperfect information and multiplayer and even fewer with both. I took it as a challenge to try and implement an agent that effectively used MCTS in this case.

a basic implementation of MCTS has been created for other games, and I used this as a basis for my agent: https://github.com/theKGS/MCTS


