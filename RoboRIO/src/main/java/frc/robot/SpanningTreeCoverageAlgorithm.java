//No one clam should have all that chowder.

package frc.robot;

import java.util.*;

public class SpanningTreeCoverageAlgorithm {

    private ArrayList<node> nodeArr = new ArrayList<node>(); //an arraylist of valid nodes
    private boolean[][] map; //the actual matrice
    private int col; //dimension x
    private int row; //dimension y
    private ArrayList<node>[] adjancencyNodes;
    private HashMap<node, Integer> nodeInteger = new HashMap<node, Integer>();
    private HashMap<Integer, node> integerNode = new HashMap<Integer, node>();
    

    //the class node
    private class node {
        int xNode;
        int yNode;

        //each node is defined as the bottom right corner of the square
        public node(int x, int y) {
            xNode = x;
            yNode = y;
        }
    }

    //generate valid nodes
    private void generateNode() {
        for (int i = 1; i<row; i+=2) {
            for (int j = 1; j<col; j+=2) {
                if (map[i][j] == false && map[i-1][j] == false && map[i][j-1] && map[i-1][j-1] == false) {
                    node valid = new node(i, j);
                    nodeArr.add(valid);
                }
            }
        }    
    }

    //creates an adjacency graph for DFS/BFS
    private void adjacencyGraphGenerate() {
        //initialize adjacencyNodes
        adjancencyNodes = new ArrayList[nodeArr.size()];
        for (int i = 0; i<nodeArr.size(); i++) {
            adjancencyNodes[i] = new ArrayList<node>();
        }
        //creating the adjancency list by going through each node and adding its neighbors to the adjacency list
        for (node n : nodeArr) {
            
        }
    }

    public void stc(boolean[][] arr, int x, int y) {
        map = arr;

    }

}
