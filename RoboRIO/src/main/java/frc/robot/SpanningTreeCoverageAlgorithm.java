package frc.robot;

import java.util.*;

/** Add your docs here. */
public class SpanningTreeCoverageAlgorithm {

    private ArrayList<node> nodeArr = new ArrayList<node>();
    private boolean[][] map;
    private int col;
    private int row;

    private class node {
        int xNode;
        int yNode;

        //each node is defined as the bottom right corner of the square
        public node(int x, int y) {
            xNode = x;
            yNode = y;
        }
    }

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

    public void stc(boolean[][] arr, int x, int y) {
        map = arr;

        


    }

}
