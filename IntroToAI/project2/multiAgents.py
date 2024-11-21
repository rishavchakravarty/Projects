# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from util import Queue
from game import Directions
import random, util

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        # Get the successor game state after applying the action
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        # Initialize score with the successor state's score
        score = successorGameState.getScore()

        # Get the distance to the nearest food
        foodDistances = [manhattanDistance(newPos, foodPos) for foodPos in newFood.asList()]
        if foodDistances:
            score += 1.0 / min(foodDistances)  # Closer food gives higher score

        # Penalize for proximity to active ghosts
        for ghostState, scaredTime in zip(newGhostStates, newScaredTimes):
            ghostPos = ghostState.getPosition()
            ghostDist = manhattanDistance(newPos, ghostPos)
            if scaredTime == 0 and ghostDist < 2:
                score -= 100  # Avoid ghosts when they are not scared

        return score


def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        # Start the minimax algorithm
        return self.minimax(gameState, 0, 0)

    def minimax(self, gameState, agentIndex, depth):
        """
        Recursive minimax function.
        """
        # Check if we have reached a terminal state (game over or depth limit)
        if gameState.isWin() or gameState.isLose() or depth == self.depth:
            return self.evaluationFunction(gameState)

        # If the agent is Pacman (maximizing player)
        if agentIndex == 0:
            return self.maxValue(gameState, depth)

        # If the agent is a ghost (minimizing player)
        else:
            return self.minValue(gameState, agentIndex, depth)

    def maxValue(self, gameState, depth):
        """
        Maximizing function for Pacman (agentIndex = 0)
        """
        # Get Pacman's legal actions
        legalActions = gameState.getLegalActions(0)
        if not legalActions:  # No legal actions available
            return self.evaluationFunction(gameState)

        # Initialize best score to a very low value
        bestScore = float('-inf')
        bestAction = None

        # Loop through all the legal actions for Pacman
        for action in legalActions:
            # Generate the successor state after taking this action
            successor = gameState.generateSuccessor(0, action)

            # Recursively call minimax for the next depth and first ghost (agentIndex = 1)
            score = self.minimax(successor, 1, depth)

            # Update best score and best action
            if score > bestScore:
                bestScore = score
                bestAction = action

        # If at the root (depth == 0), return the action
        if depth == 0:
            return bestAction

        # Otherwise, return the score
        return bestScore

    def minValue(self, gameState, agentIndex, depth):
        """
        Minimizing function for ghosts (agentIndex = 1 to numAgents-1)
        """
        # Get the legal actions for the current ghost
        legalActions = gameState.getLegalActions(agentIndex)
        if not legalActions:  # No legal actions available
            return self.evaluationFunction(gameState)

        # Initialize best score to a very high value
        bestScore = float('inf')

        # Loop through all the legal actions for the ghost
        for action in legalActions:
            # Generate the successor state after taking this action
            successor = gameState.generateSuccessor(agentIndex, action)

            # If it's the last ghost, go back to Pacman (depth+1)
            if agentIndex == gameState.getNumAgents() - 1:
                score = self.minimax(successor, 0, depth + 1)
            else:
                # Otherwise, go to the next ghost
                score = self.minimax(successor, agentIndex + 1, depth)

            # Update the best score (minimizing)
            if score < bestScore:
                bestScore = score

        return bestScore

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        # Initialize alpha and beta
        alpha = float('-inf')
        beta = float('inf')

        # Start the alpha-beta pruning process
        _, bestAction = self.alphaBeta(gameState, 0, 0, alpha, beta)
        return bestAction

    def alphaBeta(self, gameState, agentIndex, depth, alpha, beta):
        """
        Recursive alpha-beta pruning function
        Returns (bestScore, bestAction)
        """
        # Terminal state or maximum depth reached
        if gameState.isWin() or gameState.isLose() or depth == self.depth:
            return self.evaluationFunction(gameState), None

        # Determine the number of agents
        numAgents = gameState.getNumAgents()

        # Pacman's turn (Maximizing player)
        if agentIndex == 0:
            return self.maxValue(gameState, depth, alpha, beta)
        else:
            # Ghosts' turn (Minimizing players)
            return self.minValue(gameState, agentIndex, depth, alpha, beta)

    def maxValue(self, gameState, depth, alpha, beta):
        """
        Maximizing function for Pacman (agentIndex == 0)
        Returns (bestScore, bestAction)
        """
        legalActions = gameState.getLegalActions(0)
        if not legalActions:
            return self.evaluationFunction(gameState), None

        bestScore = float('-inf')
        bestAction = None

        for action in legalActions:
            successor = gameState.generateSuccessor(0, action)
            # Proceed to the first ghost (agentIndex = 1)
            score, _ = self.alphaBeta(successor, 1, depth, alpha, beta)

            if score > bestScore:
                bestScore = score
                bestAction = action

            # Update alpha
            alpha = max(alpha, bestScore)

            # Prune if alpha exceeds beta (using strict inequality)
            if alpha > beta:
                break

        return bestScore, bestAction

    def minValue(self, gameState, agentIndex, depth, alpha, beta):
        """
        Minimizing function for ghosts (agentIndex > 0)
        Returns (bestScore, None)
        """
        legalActions = gameState.getLegalActions(agentIndex)
        if not legalActions:
            return self.evaluationFunction(gameState), None

        bestScore = float('inf')

        for action in legalActions:
            successor = gameState.generateSuccessor(agentIndex, action)

            # Determine next agent
            if agentIndex == gameState.getNumAgents() - 1:
                # Last ghost; proceed to Pacman and increase depth
                score, _ = self.alphaBeta(successor, 0, depth + 1, alpha, beta)
            else:
                # Next ghost
                score, _ = self.alphaBeta(successor, agentIndex + 1, depth, alpha, beta)

            if score < bestScore:
                bestScore = score

            # Update beta
            beta = min(beta, bestScore)

            # Prune if beta is less than alpha (using strict inequality)
            if beta < alpha:
                break

        return bestScore, None
    #------------------------

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        #-------------------------
        # Start expectimax with Pacman (agentIndex = 0) and depth 0
        return self.expectimax(gameState, 0, 0)

    def expectimax(self, gameState, agentIndex, depth):
        """
        Recursive expectimax function
        """
        # Check for terminal conditions
        if gameState.isWin() or gameState.isLose() or depth == self.depth:
            return self.evaluationFunction(gameState)

        # Pacman's turn (maximizing player)
        if agentIndex == 0:
            return self.maxValue(gameState, depth)

        # Ghosts' turn (chance nodes)
        else:
            return self.expValue(gameState, agentIndex, depth)

    def maxValue(self, gameState, depth):
        """
        Maximizing function for Pacman (agentIndex = 0)
        """
        # Get Pacman's legal actions
        legalActions = gameState.getLegalActions(0)
        if not legalActions:
            return self.evaluationFunction(gameState)

        # Initialize best score to negative infinity
        bestScore = float('-inf')
        bestAction = None

        # Loop through legal actions
        for action in legalActions:
            # Generate successor state
            successor = gameState.generateSuccessor(0, action)

            # Recursive call to expectimax for the first ghost (agentIndex = 1)
            score = self.expectimax(successor, 1, depth)

            # Update the best score and action
            if score > bestScore:
                bestScore = score
                bestAction = action

        # If at root level, return the best action
        if depth == 0:
            return bestAction

        return bestScore

    def expValue(self, gameState, agentIndex, depth):
        """
        Expectimax function for ghosts (agentIndex > 0)
        """
        # Get the legal actions for the current ghost
        legalActions = gameState.getLegalActions(agentIndex)
        if not legalActions:
            return self.evaluationFunction(gameState)

        # Initialize expected value
        expectedValue = 0

        # Probability for each action (assuming equal probability)
        prob = 1.0 / len(legalActions)

        # Loop through the legal actions
        for action in legalActions:
            # Generate successor state
            successor = gameState.generateSuccessor(agentIndex, action)

            # If it's the last ghost, go back to Pacman (next depth)
            if agentIndex == gameState.getNumAgents() - 1:
                score = self.expectimax(successor, 0, depth + 1)
            else:
                # Otherwise, go to the next ghost
                score = self.expectimax(successor, agentIndex + 1, depth)

            # Accumulate expected value
            expectedValue += prob * score

        return expectedValue
    #-----------------------


def betterEvaluationFunction(currentGameState: GameState):
    """
    DESCRIPTION:
    This evaluation function considers several factors to encourage Pacman to:
    - Collect food pellets efficiently.
    - Eat capsules promptly.
    - Chase scared ghosts when advantageous.
    - Avoid active ghosts.
    """
    # Get useful information from currentGameState
    pacmanPos = currentGameState.getPacmanPosition()
    food = currentGameState.getFood()
    walls = currentGameState.getWalls()
    ghostStates = currentGameState.getGhostStates()
    capsules = currentGameState.getCapsules()

    # Base score from the current game state
    score = currentGameState.getScore()

    # Initialize variables
    foodScore = 0
    ghostScore = 0
    capsuleScore = 0
    scaredGhostScore = 0

    # Factor 1: Distance to the closest food (using manhattanDistance)
    foodList = food.asList()
    if foodList:
        foodDistances = [manhattanDistance(pacmanPos, foodPos) for foodPos in foodList]
        minFoodDist = min(foodDistances)
        foodScore = 1.0 / minFoodDist  # The closer the better
    else:
        foodScore = 10  # No food left, high reward

    # Factor 2: Ghost distances
    for ghostState in ghostStates:
        ghostPos = ghostState.getPosition()
        ghostDist = manhattanDistance(pacmanPos, ghostPos)
        if ghostDist == 0:
            ghostDist = 1  # Avoid division by zero

        if ghostState.scaredTimer > 0:
            # Scared ghost: encourage Pacman to chase
            scaredGhostScore += 200 / ghostDist
        else:
            # Active ghost: penalize proximity
            if ghostDist <= 1:
                ghostScore -= 500  # Immediate danger
            else:
                ghostScore -= 10 / ghostDist  # Closer ghost, higher penalty

    # Factor 3: Capsules
    if capsules:
        capsuleDistances = [manhattanDistance(pacmanPos, capPos) for capPos in capsules]
        minCapsuleDist = min(capsuleDistances)
        capsuleScore = 10.0 / minCapsuleDist  # Encourage approaching capsules
    else:
        capsuleScore = 0  # No capsules left

    # Factor 4: Remaining food count
    remainingFoodScore = -len(foodList) * 4  # Penalize remaining food

    # Factor 5: Remaining capsules count
    remainingCapsuleScore = -len(capsules) * 100  # Strongly penalize remaining capsules

    # Combine all the factors with adjusted weights
    evaluation = (
        score +
        foodScore * 1.5 +
        capsuleScore * 5 +
        ghostScore +
        scaredGhostScore * 2 +
        remainingFoodScore +
        remainingCapsuleScore
    )

    return evaluation

#---------------------------------
# Abbreviation
better = betterEvaluationFunction
