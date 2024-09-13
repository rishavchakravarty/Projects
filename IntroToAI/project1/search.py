# search.py
# ---------
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


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem: SearchProblem):
    """Search the deepest nodes in the search tree first."""
    # Initialize the fringe as a stack, storing (state, path_to_state)
    fringe = util.Stack()
    fringe.push((problem.getStartState(), []))
    
    # To track visited states efficiently
    visited = set()

    while not fringe.isEmpty():
        # Pop the current state and the actions leading to it
        current_state, actions = fringe.pop()

        # Check if current state is the goal
        if problem.isGoalState(current_state):
            return actions

        # Only expand if the current state hasn't been visited yet
        if current_state not in visited:
            visited.add(current_state)

            # Get all the successors (unvisited neighbors)
            for successor, action, stepCost in problem.getSuccessors(current_state):
                if successor not in visited:
                    # Push successor to fringe with updated path
                    fringe.push((successor, actions + [action]))

    # If no solution is found, return an empty list
    return []
    util.raiseNotDefined()

def breadthFirstSearch(problem: SearchProblem):
    """Search the shallowest nodes in the search tree first."""
    # Initialize the fringe as a queue
    fringe = util.Queue()
    # Enqueue the start state and an empty list of actions (path)
    fringe.push((problem.getStartState(), []))
    
    # To track visited states efficiently
    visited = set()

    while not fringe.isEmpty():
        # Dequeue the current state and the actions leading to it
        current_state, actions = fringe.pop()

        # Check if current state is the goal
        if problem.isGoalState(current_state):
            return actions

        # If the current state has not been visited
        if current_state not in visited:
            visited.add(current_state)

            # Get all the successors of the current state
            for successor, action, stepCost in problem.getSuccessors(current_state):
                if successor not in visited:
                    # Enqueue successor with the updated path
                    fringe.push((successor, actions + [action]))

    # If no solution is found, return an empty list
    return []
    util.raiseNotDefined()

def uniformCostSearch(problem: SearchProblem):
    """Search the node of least total cost first."""
    # Initialize the fringe as a priority queue, storing (cost, state, actions)
    fringe = util.PriorityQueue()
    # Push the start state with cost 0 and an empty list of actions (path)
    fringe.push((problem.getStartState(), [], 0), 0)
    
    # To track visited states and the cost to reach them
    visited = {}

    while not fringe.isEmpty():
        # Dequeue the state with the least cost
        current_state, actions, current_cost = fringe.pop()

        # If this state is the goal, return the actions (path)
        if problem.isGoalState(current_state):
            return actions

        # Only expand if this state hasn't been visited or we've found a cheaper path to it
        if current_state not in visited or current_cost < visited[current_state]:
            visited[current_state] = current_cost

            # Get all the successors of the current state
            for successor, action, stepCost in problem.getSuccessors(current_state):
                new_cost = current_cost + stepCost
                # Push successor with the updated cost and actions
                if successor not in visited or new_cost < visited[successor]:
                    fringe.push((successor, actions + [action], new_cost), new_cost)

    # If no solution is found, return an empty list
    return []
    util.raiseNotDefined()

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem: SearchProblem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    # Priority queue for the fringe, storing (state, path, cost_so_far)
    fringe = util.PriorityQueue()
    
    # Push the start state with cost 0 and heuristic h(start), f(n) = g(n) + h(n)
    start_state = problem.getStartState()
    start_heuristic = heuristic(start_state, problem)
    fringe.push((start_state, [], 0), start_heuristic)

    # To track the minimum cost to reach each state
    visited = {}

    while not fringe.isEmpty():
        # Pop the state with the lowest f(n) value
        current_state, actions, current_cost = fringe.pop()

        # If we've already processed this state with a lower cost, skip it
        if current_state in visited and visited[current_state] <= current_cost:
            continue

        # Update the cost for this state
        visited[current_state] = current_cost

        # If the current state is the goal, return the actions (path)
        if problem.isGoalState(current_state):
            return actions

        # Get all the successors of the current state
        for successor, action, stepCost in problem.getSuccessors(current_state):
            new_cost = current_cost + stepCost
            # f(n) = g(n) + h(n) where g(n) is the current cost and h(n) is the heuristic estimate
            heuristic_cost = heuristic(successor, problem)
            total_cost = new_cost + heuristic_cost
            
            # Only push the successor to the fringe if we haven't visited it or found a cheaper path
            if successor not in visited or new_cost < visited[successor]:
                fringe.push((successor, actions + [action], new_cost), total_cost)

    # If no solution is found, return an empty list
    return []
    util.raiseNotDefined()


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
