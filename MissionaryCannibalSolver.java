// Joshua Knudsen
// Graph Data Structure
// Breadth-First Search Algorithm

//In order to run this project you just simply need to run the main method in the MissionaryCannibalSolver class


    //Pseudocode for the algorithm included below
/*
    function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
     node ← a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
     if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
     frontier ← a FIFO queue with node as the only element
     explored ← an empty set
     loop do
       if EMPTY?(frontier) then return failure
       node ← POP(frontier) // chooses the shallowest node in frontier
       add node.STATE to explored
               for each action in problem.ACTIONS(node.STATE) do
                  child ← CHILD-NODE(problem,node,action)
                  if child.STATE is not in explored or frontier then
                     if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
                     frontier ← INSERT(child,frontier)
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class MissionaryCannibalSolver {

    //driver method
    public static void main(String[] args){

        MissionaryCannibalSolver newSolver = new MissionaryCannibalSolver(); //creates an object of Missionary Cannibal Solver in order to run the program

        Node solutionFound = newSolver.BreadthFirstSearch(newSolver.getPROBLEMNODE()); //runs the search algorithm and stores the solution


        Node tempNode = solutionFound;

        Stack<Node> nodesSolution = new Stack<Node>(); //will hold the set of actions it took to get from the intitial state to the goal state

        while(tempNode.getParent() != null){

            nodesSolution.add(tempNode); //the current node get pushed to the stack
            tempNode = tempNode.getParent(); //temp node will become the parent of the current node

        }

        System.out.println("Solution to the Missionaries Cannibals Problem:\n");
        graphicsEngine(nodesSolution); //function call to the graphics engine



    }

    //this function will print out the solution in a presentable way
    public static void graphicsEngine(Stack<Node> solution){

        Node currentNode;
        int solutionStackSize = solution.size();

        for(int i = 0; i < solutionStackSize; i++){
            currentNode = solution.pop(); //pops off the solution stack

            if(currentNode.getAction().equals(ActionChoice.ML)){ //Missionary Left
                System.out.println(getPilotGraphic(currentNode) + "takes a Missionary from the Right Shore to the Left Shore");
            }
            else if(currentNode.getAction().equals(ActionChoice.MR)){ //Missionary right
                System.out.println(getPilotGraphic(currentNode) + "takes a Missionary from the Left Shore to the Right Shore");
            }
            else if(currentNode.getAction().equals(ActionChoice.CL)){// Cannibal left
                System.out.println(getPilotGraphic(currentNode) + "takes a Cannibal from the Right Shore to the Left Shore");
            }
            else if(currentNode.getAction().equals(ActionChoice.CR)){ //Cannibal Right
                System.out.println(getPilotGraphic(currentNode) + "takes a Cannibal from the Left Shore to the Right Shore");
            }
            else if(currentNode.getAction().equals(ActionChoice.CP)){ //Cannibal pilot
                System.out.println("A Cannibal becomes the Pilot");
            }
            else if(currentNode.getAction().equals(ActionChoice.MP)){ //Missionary Pilot
                System.out.println("A Missionary becomes the Pilot");
            }
            else if(currentNode.getAction().equals(ActionChoice.BL)){ //Boat left
                System.out.println(getPilotGraphic(currentNode) + "takes the Boat from the Right Shore to the Left Shore by them self");
            }
            else if(currentNode.getAction().equals(ActionChoice.BR)){ //boat right
                System.out.println(getPilotGraphic(currentNode) + "takes the Boat from the Left Shore to the Right Shore by them self");
            }
            else if(currentNode.getAction().equals(ActionChoice.EB)){ //Empty the boat
                System.out.println(getPilotGraphic(currentNode) + "gets out of the Boat and the Boat is now Empty");
            }

            printCurrentStateGraphic(currentNode);//displays the current state at each state
            System.out.println(); //will print an empty line to space out the text to make it more readable
        }

    }

    //returns the appropriate pilot
    public static String getPilotGraphic(Node currentNode){
        if(currentNode.getState().getBoatPilot().equals(Pilot.MISSIONARY)){
            return "A Missionary Pilot ";
        }
        else{
            return "A Cannibal Pilot ";
        }
    }

    //will print out the current state
    public static void printCurrentStateGraphic(Node currentNode){

        System.out.println("Current State: [ " + currentNode.getState().getMissionariesLeftSide() + "M, " +
                currentNode.getState().getCannibalsLeftSide() + "C " + "| " + currentNode.getState().getMissionariesRightSide() + "M, " +
                currentNode.getState().getCannibalsRightSide() + "C ] {Boat Pilot: " + currentNode.getState().getBoatPilot() + ". Boat Position: " +
                currentNode.getState().getBoatPosition() + "}");
    }

    private Node PROBLEMNODE; //private field to hold the new node with the initial state for the driver
    private State INITIALSTATE; //private field to hold the INITIALSTATE for the problem node

    //constructor (used for the driver)
    public MissionaryCannibalSolver(){
        this.INITIALSTATE = new State(); //creates the initial state (defined in the state default constructor)
        this.PROBLEMNODE = new Node(INITIALSTATE,null,null); //will create the problem node for the driver
    }

    //gets the initial state which is passed into the BreadthFirstSearch function
    public Node getPROBLEMNODE(){
        return PROBLEMNODE;
    }



    //enum to signify the different action choices
    public enum ActionChoice{
        ML,MR,CL,CR,CP,MP,BL,BR,EB
    }
    //ML = missionary left
    //MR = missionary right
    //CL = Cannibal left
    //CR = Cannibal right
    //CP = Cannibal starts piloting
    //MP = Missionary starts piloting
    //BL = Boat left
    //BR = Boat right
    //EB = Empty the boat

    //enumerator for the side of the river the boat is currently at
    public enum Position{
        LEFT, RIGHT
    }

    //enumerator for who is piloting the boat
    public enum Pilot{
        MISSIONARY, CANNIBAL, EMPTY
    }



    private Queue<Node> frontier;
    private Vector<State> explored; //a list of states that have already been reached


    public Node BreadthFirstSearch(Node problem){ //function returns Node if a path is successfully found or null if it is not

        Node currentNode = new Node();
        currentNode.setState(problem.getState()); //sets the node to a node with the problem's initial state

        if(GOALTEST(currentNode.getState())){
            return currentNode; //returns success if it matches the current state
        }

        frontier = new LinkedList<Node>();

        frontier.add(currentNode); //initializing the frontier with the initial state of the problem

        explored = new Vector<State>(); //explored is initialized as empty



        while(!isFrontierEmpty()){ //will loop while the frontier is not empty


            currentNode = frontier.remove(); //POPs from frontier

            explored.add(currentNode.state); //adds the currentNode's state to the explored set


            for(ActionChoice action : problem.ACTIONS(currentNode.state)){ //for every action in available actions at the currentNode state


                Node child = CHILDNODE(problem, currentNode, action);

                if(!(explored.contains(child.state)) || !(frontier.contains(child))){ // if the child is not in the explored or in the frontier
                    if(GOALTEST(child.getState())){
                        return child;
                    }
                    frontier.add(child); // inserts child into the frontier
                }

            }

        }

        return null; //if there is no possible outcome the function will return null

    }

    //function to spawn off a new child node given an action and the parentNode
    public Node CHILDNODE(Node problem, Node parentNode, ActionChoice act){

        State newState = new State(parentNode.getState()); //variable to represent the child state (copies the parent's state)


        //will update the state based on the action just called (only actions that are possible are called due to the ACTIONS function returning only possible actions given a current state)
        if(act.equals(ActionChoice.ML)){ //missionary left
            if(newState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if its a missionary pilotting the boat
                //missionary pilot will also be moved
                newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() + 1);
                newState.setMissionariesRightSide(newState.getMissionariesRightSide() - 1);
            }
            else{
                //cannibal pilot will also be moved
                newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() + 1);
                newState.setCannibalsRightSide(newState.getCannibalsRightSide() - 1);
            }
            newState.setBoatPosition(Position.LEFT); //will set the boat left
            //missionary gets moved from right to left
            newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() + 1);
            newState.setMissionariesRightSide(newState.getMissionariesRightSide() - 1);
        }
        else if(act.equals(ActionChoice.MR)){ //missionary right
            if(newState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if its a missionary pilotting the boat
                //missionary pilot will also be moved right
                newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() - 1);
                newState.setMissionariesRightSide(newState.getMissionariesRightSide() + 1);
            }
            else{
                //cannibal pilot will also be moved right
                newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() - 1);
                newState.setCannibalsRightSide(newState.getCannibalsRightSide() + 1);
            }
            newState.setBoatPosition(Position.RIGHT); //will set the boat right
            //missionary gets moved from right to left
            newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() - 1);
            newState.setMissionariesRightSide(newState.getMissionariesRightSide() + 1);
        }
        else if(act.equals(ActionChoice.CL)){ //cannibal left
            if(newState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if its a missionary piloting the boat
                //missionary pilot will also be moved
                newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() + 1);
                newState.setMissionariesRightSide(newState.getMissionariesRightSide() - 1);
            }
            else{
                //cannibal pilot will also be moved
                newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() + 1);
                newState.setCannibalsRightSide(newState.getCannibalsRightSide() - 1);
            }
            newState.setBoatPosition(Position.LEFT); //will set the boat left
            //cannibal gets moved from right to left
            newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() + 1);
            newState.setCannibalsRightSide(newState.getCannibalsRightSide() - 1);
        }
        else if(act.equals(ActionChoice.CR)){ //cannibal right
            if(newState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if its a missionary piloting the boat
                //missionary pilot will also be moved right
                newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() - 1);
                newState.setMissionariesRightSide(newState.getMissionariesRightSide() + 1);
            }
            else{
                //cannibal pilot will also be moved right
                newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() - 1);
                newState.setCannibalsRightSide(newState.getCannibalsRightSide() + 1);
            }
            newState.setBoatPosition(Position.RIGHT); //will set the boat right
            //cannibal gets moved from left to right
            newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() - 1);
            newState.setCannibalsRightSide(newState.getCannibalsRightSide() + 1);
        }
        else if(act.equals(ActionChoice.CP)){ //cannibal pilot
            newState.setBoatPilot(Pilot.CANNIBAL);
        }
        else if(act.equals(ActionChoice.MP)){ //missionary pilot
            newState.setBoatPilot(Pilot.MISSIONARY);
        }
        else if(act.equals(ActionChoice.BL)){ //boat left
            if(newState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if its a missionary pilotting the boat
                //missionary pilot will also be moved
                newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() + 1);
                newState.setMissionariesRightSide(newState.getMissionariesRightSide() - 1);
            }
            else{
                //cannibal pilot will also be moved
                newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() + 1);
                newState.setCannibalsRightSide(newState.getCannibalsRightSide() - 1);
            }
            newState.setBoatPosition(Position.LEFT); //will set the boat left
        }
        else if(act.equals(ActionChoice.BR)){ //boat right
            if(newState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if its a missionary pilotting the boat
                //missionary pilot will also be moved right
                newState.setMissionariesLeftSide(newState.getMissionariesLeftSide() - 1);
                newState.setMissionariesRightSide(newState.getMissionariesRightSide() + 1);
            }
            else{
                //cannibal pilot will also be moved right
                newState.setCannibalsLeftSide(newState.getCannibalsLeftSide() - 1);
                newState.setCannibalsRightSide(newState.getCannibalsRightSide() + 1);
            }
            newState.setBoatPosition(Position.RIGHT); //will set the boat right
        }
        else if(act.equals(ActionChoice.EB)){ //empty the boat
            newState.setBoatPilot(Pilot.EMPTY);
        }


        Node newChild = new Node(newState,act,parentNode); //initializes the child node with the parent's state (will be updated before returning),
        //the action that it took to get there, and a reference to the parent node

        return newChild; //returns the new child
    }

    public boolean isFrontierEmpty(){
        if(frontier.size() == 0){
            return true; //will return true if the frontier is empty
        }
        else{
            return false; //otherwise it will return false
        }
    }

    public boolean GOALTEST(State currentState){

        State goalState = new State(0,0,3,3, Position.RIGHT, Pilot.EMPTY ); //goal state


        //equals operator has been overridden for this to compare the correct fields
        if(currentState.equals(goalState)){ //if the current state is equal to the goal state then the test was passed
            return true;
        }
        else{
            return false; //else returns false and it fails the goaltest
        }



    }



    public class Node{


        private State state; //the current state of the node

        private Node parent; //a reference to the parent of the current Node

        private ActionChoice action; // the action that was applied to the parent to generate this node


        //default constructor
        public Node(){

            this.state = null;

            this.action = null;

            this.parent = null;

        }

        //constructor that takes in the state, action and parent when creating a new node
        public Node(State st, ActionChoice act, Node parent){

            this.state = st;
            this.action = act;
            this.parent = parent;
        }

        public State getState() {
            return state;
        }

        public void setState(State updatedState){
            this.state = updatedState;
        }

        public ActionChoice getAction(){
            return action;
        }

        public Node getParent() {
            return parent;
        }



        @Override
        public boolean equals(Object object){
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            Node currentNode = (Node) object;
            return state.equals(currentNode.state) && action.equals(currentNode.action);
        }


        public Vector<ActionChoice> ACTIONS(State currentState){ // returns a vector of all possible actions that can be taken given the current state

            Vector<ActionChoice> possibleActions = new Vector<ActionChoice>();


            if(currentState.getBoatPosition().equals(Position.LEFT)){ //if the boat is on the left side

                if(currentState.getBoatPilot().equals(Pilot.EMPTY)){ //if the boat has an empty pilot


                    if(currentState.getMissionariesLeftSide() > 0){ //if there at least one missionary on the left side
                        possibleActions.add(ActionChoice.MP); //a possible action can be that a Missionary becomes the pilot
                    }
                    if(currentState.getCannibalsLeftSide() > 0){
                        possibleActions.add(ActionChoice.CP); // a possible action can be that a Cannibal becomes the pilot
                    }


                }
                if(currentState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if the boat pilot is a missionary



                    //All the states with possible actions when the boat is on the left side while piloted by a missionary
                    if(currentState.getCannibalsLeftSide() == 3 && currentState.getMissionariesLeftSide() == 3){
                        possibleActions.add(ActionChoice.CP); //it is possible to make the cannibal the pilot
                        possibleActions.add(ActionChoice.CR); // and to take a cannibal to the right
                    }
                    if(currentState.getCannibalsLeftSide() == 2 && currentState.getMissionariesLeftSide() == 3){
                        possibleActions.add(ActionChoice.CP); //cannibal pilot
                        possibleActions.add(ActionChoice.BR); //boat to the right
                    }
                    if(currentState.getCannibalsLeftSide() == 1 && currentState.getMissionariesLeftSide() == 3){
                        possibleActions.add(ActionChoice.CP);//cannibal pilot
                        possibleActions.add(ActionChoice.MR); //missionary right
                    }
                    if(currentState.getCannibalsLeftSide() == 2 && currentState.getMissionariesLeftSide() == 2){
                        possibleActions.add(ActionChoice.CR); //cannibal right
                        possibleActions.add(ActionChoice.MR); //missionary right

                    }

                }
                if(currentState.getBoatPilot().equals(Pilot.CANNIBAL)){ //if the boat is on the left and the pilot is a cannibal

                    //all states with possible actions when the boat is on the left side and the pilot is a cannibal
                    if(currentState.getCannibalsLeftSide() == 3 && currentState.getMissionariesLeftSide() == 3){
                        possibleActions.add(ActionChoice.MP); //can set missionary as the pilot
                        possibleActions.add(ActionChoice.MR); //can take a missionary right
                    }
                    if(currentState.getCannibalsLeftSide() == 2 && currentState.getMissionariesLeftSide() == 3){
                        possibleActions.add(ActionChoice.MP); //missionary pilot
                        possibleActions.add(ActionChoice.CR); //cannibal right
                    }
                    if(currentState.getCannibalsLeftSide() == 1 && currentState.getMissionariesLeftSide() == 3){
                        possibleActions.add(ActionChoice.BR); //Boat right
                        possibleActions.add(ActionChoice.MP); //Missionary pilot
                    }
                    if(currentState.getCannibalsLeftSide() == 3 && currentState.getMissionariesLeftSide() == 0){
                        possibleActions.add(ActionChoice.BR); //boat right
                        possibleActions.add(ActionChoice.CR); //cannibal right
                    }
                    if(currentState.getCannibalsLeftSide() == 2 && currentState.getMissionariesLeftSide() == 0){

                        possibleActions.add(ActionChoice.BR);//boat right
                        possibleActions.add(ActionChoice.CR); //cannibal right
                    }



                }


            }
            if(currentState.getBoatPosition().equals(Position.RIGHT)){ //if the boat is on the right side


                //All possible actions if the boat is on the right

                if(currentState.getBoatPilot().equals(Pilot.EMPTY)){ //if the boat is empty on the right side (typically would mean that we have reached the goal state but we can always undo what we did)
                   possibleActions.add(ActionChoice.CP); //cannibal pilot
                }
                if(currentState.getBoatPilot().equals(Pilot.MISSIONARY)){ //if the boat has a missionary pilot on the right side

                    if(currentState.getCannibalsRightSide() == 1 && currentState.getMissionariesRightSide() == 1){ //if there is one missionary
                                                                                                                    // and one cannibal on the right
                        possibleActions.add(ActionChoice.CP);//cannibal pilot
                        possibleActions.add(ActionChoice.CL);//cannibal left
                        possibleActions.add(ActionChoice.BL);//boat left
                    }
                    if(currentState.getCannibalsRightSide() == 2 && currentState.getMissionariesRightSide() == 2){ //if there is 2 cannibals and 2 missionaries on the right side
                        possibleActions.add(ActionChoice.ML); //missionary left
                        possibleActions.add(ActionChoice.CL); //cannibals left
                    }
                    if(currentState.getCannibalsRightSide() == 1 && currentState.getMissionariesRightSide() == 3){ //if there is one cannibal and 3 missionaries on the right side
                        possibleActions.add(ActionChoice.ML); //missionary left
                        possibleActions.add(ActionChoice.CP); //cannibal pilot
                    }

                }
                if(currentState.getBoatPilot().equals(Pilot.CANNIBAL)){ //if the boat has a cannibal pilot on the right side
                    if(currentState.getCannibalsRightSide() == 1 && currentState.getMissionariesRightSide() == 1){ //1 cannibals right 1 missionaries right
                        possibleActions.add(ActionChoice.ML);//missionary left
                        possibleActions.add(ActionChoice.MP); //missionary pilot
                    }
                    if(currentState.getCannibalsRightSide() == 3 && currentState.getMissionariesRightSide() == 0){ //3 C and 0 M
                        possibleActions.add(ActionChoice.CL); //cannibal left
                        possibleActions.add(ActionChoice.BL); // boat left
                    }
                    if(currentState.getCannibalsRightSide() == 1 && currentState.getMissionariesRightSide() == 3){ //1 cannibals right 3 missionaries right
                        possibleActions.add(ActionChoice.MP);//missionary pilot
                        possibleActions.add(ActionChoice.BL); //boat left
                    }
                    if(currentState.getCannibalsRightSide() == 2 && currentState.getMissionariesRightSide() == 3){ //2 cannibals right 3 missionaries right
                        possibleActions.add(ActionChoice.CL);//Cannibal left
                        possibleActions.add(ActionChoice.BL); //Boat left
                    }
                    if(currentState.getCannibalsRightSide() == 3 && currentState.getMissionariesRightSide() == 3){ //3 cannibals right 3 missionaries right
                        possibleActions.add(ActionChoice.CL); //cannibal left
                        possibleActions.add(ActionChoice.EB); //Empty the boat
                    }

                }
            }


            return possibleActions; //will return the set of all possible actions
        }



    }

    public class State{


        private int missionariesLeftSide;
        private int cannibalsLeftSide;
        private int missionariesRightSide;
        private int cannibalsRightSide;
        private Position boatPosition; //position of the boat (LEFT OR RIGHT)
        private Pilot boatPilot; // who is currently piloting the boat



        public State(){

            //start state (initial state) <M,M,M,C,C,C,B|>
            //3 missionaries left
            //3 cannibals left
            //Boat left
            //Boat empty

            this.missionariesLeftSide = 3;
            this.cannibalsLeftSide = 3;
            this.missionariesRightSide = 0;
            this.cannibalsRightSide = 0;
            this.boatPosition = Position.LEFT;
            this.boatPilot = Pilot.EMPTY;

        }

        public State(int mLeft, int cLeft, int mRight, int cRight, Position bPosition, Pilot bPilot){
            this.missionariesLeftSide = mLeft;
            this.cannibalsLeftSide = cLeft;
            this.missionariesRightSide = mRight;
            this.cannibalsRightSide = cRight;
            this.boatPosition = bPosition;
            this.boatPilot = bPilot;

        }

        public State(State copyState) {
            this.missionariesLeftSide = copyState.getMissionariesLeftSide();
            this.cannibalsLeftSide = copyState.getCannibalsLeftSide();
            this.missionariesRightSide = copyState.getMissionariesRightSide();
            this.cannibalsRightSide = copyState.getCannibalsRightSide();
            this.boatPosition = copyState.getBoatPosition();
            this.boatPilot = copyState.getBoatPilot();
        }


        //overridden equals function to compare states
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            State currentState = (State) object;
            return missionariesLeftSide == currentState.getMissionariesLeftSide() &&
                    cannibalsLeftSide == currentState.getCannibalsLeftSide() &&
                    missionariesRightSide == currentState.getMissionariesRightSide() &&
                    cannibalsRightSide == currentState.getCannibalsRightSide() &&
                    boatPosition.equals(currentState.getBoatPosition()) &&
                    boatPilot.equals(currentState.getBoatPilot());

        }



        //Accessors and Mutators for the state class
        public int getMissionariesLeftSide() {
            return missionariesLeftSide;
        }

        public void setMissionariesLeftSide(int missionariesLeftSide) {
            this.missionariesLeftSide = missionariesLeftSide;
        }

        public int getCannibalsLeftSide() {
            return cannibalsLeftSide;
        }

        public void setCannibalsLeftSide(int cannibalsLeftSide) {
            this.cannibalsLeftSide = cannibalsLeftSide;
        }

        public int getMissionariesRightSide() {
            return missionariesRightSide;
        }

        public void setMissionariesRightSide(int missionariesRightSide) {
            this.missionariesRightSide = missionariesRightSide;
        }

        public int getCannibalsRightSide() {
            return cannibalsRightSide;
        }

        public void setCannibalsRightSide(int cannibalsRightSide) {
            this.cannibalsRightSide = cannibalsRightSide;
        }

        public Position getBoatPosition() {
            return boatPosition;
        }

        public void setBoatPosition(Position boatPosition) {
            this.boatPosition = boatPosition;
        }

        public Pilot getBoatPilot() {
            return boatPilot;
        }

        public void setBoatPilot(Pilot boatPilot) {
            this.boatPilot = boatPilot;
        }
    }

}

