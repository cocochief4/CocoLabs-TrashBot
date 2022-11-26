
package stcpack;

import java.awt.Polygon;
import java.awt.Graphics;
//import java.text.DecimalFormat;
import java.util.*;
import frc.robot.*;

import stcpack.input.*;

/*
Note for this program:
A lot of this processing could be done at the same time; however, for the sake of understanding the code and debugging, I have used seperate methods.
*/

/** Add your docs here. */
public class stc {
    
    //Global Variable Declaration

    static latLong zeroPoint; //the top left most point in the array
    static latLong farPoint; //the max point used for defining the boolean array
    static Polygon vertexMap; //the polygon for the vertices; used for determining the boolean array
    static Polygon[] obstacleMap; //an array that holds the obstacles; used for determining the boolean array
    public static boolean[][] map; //the actual boolean array

    static ArrayList<latLong> nodes; //list of usable nodes
    public static latLong startNode; //the starting node
    static HashMap<latLong, Integer> nodeToNodeNum; //a map that's useful for figuring out my life
    // needs to be worked on with the .equals() command
    static ArrayList<node>[] adjacencyGraph; //an adjacency graph for which nodes are adjacent to each other
    static boolean[] visited; //the visted array for DFS
    static ArrayList<node>[] connected; //the connected array for DFS
    public static ArrayList<latLong> finalNavigate; //the final output to chris
    static latLong startPos;
    public static ArrayList<latLong> finalPath;

    //Class Declaration

    /*
    public static class latLong {
        double Lat;
        double Long;

        latLong (double latitude, double longitude) {
            Lat = latitude;
            Long = longitude;
        }
    }
    */

    public static class node {
        int nodeNum;
        int direction; //1 = north, 2 = east, 3 = south, 4 = west

        private node (int nodeNumber, int dir) {
            nodeNum = nodeNumber;
            direction = dir;
        }
    }

    //Method Declaration

    private static void determineZeroPoint() {
        double maxLat = -10000.0;
        double minLong = 10000.0;
        for (int i = 0; i<input.vertices.length; i++) {
            if (input.vertices[i].Lat > maxLat) {
                maxLat = input.vertices[i].Lat;
            }
            if (input.vertices[i].Long < minLong) {
                minLong = input.vertices[i].Long;
            }
        }
        zeroPoint = new latLong(maxLat, minLong);
    }

    private static void convertObsAndVertToFeet() {
        //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 10 mm at that point
        double maxLat = -100000.0;
        double maxLong = -100000.0;
        for (int i = 0; i<input.vertices.length; i++) {
            input.vertices[i].Lat = Math.abs(zeroPoint.Lat - input.vertices[i].Lat);
            input.vertices[i].Long = Math.abs(zeroPoint.Long - input.vertices[i].Long);
            //Convert to feet actually
            input.vertices[i].Lat /= 0.0000027495;
            input.vertices[i].Long /= 0.0000027495;
            input.vertices[i].Lat = Math.round(input.vertices[i].Lat);
            input.vertices[i].Long = Math.round(input.vertices[i].Long);
            if (input.vertices[i].Lat > maxLat) {
                maxLat = input.vertices[i].Lat;
            }
            if (input.vertices[i].Long > maxLong) {
                maxLong = input.vertices[i].Long;
            }
        }
        farPoint = new latLong(maxLat, maxLong);
        for (int i = 0; i<input.obstacles.length; i++) {
            for (int j = 0; j<input.obstacles[i].size(); j++) {
                input.obstacles[i].get(j).Lat = Math.abs(zeroPoint.Lat - input.obstacles[i].get(j).Lat);
                input.obstacles[i].get(j).Long = Math.abs(zeroPoint.Long - input.obstacles[i].get(j).Long);
                //Convert to feet actually
                input.obstacles[i].get(j).Lat /= 0.0000027495;
                input.obstacles[i].get(j).Long /= 0.0000027495;
                input.obstacles[i].get(j).Lat = Math.round(input.obstacles[i].get(j).Lat);
                input.obstacles[i].get(j).Long = Math.round(input.obstacles[i].get(j).Long);
            }
        }
        // System.out.println("before initial pos calc");
        // printLatLong(input.initialPos);
        // System.out.println("zeropoint");
        // printLatLong(zeroPoint);
        input.initialPos.Lat = Math.abs(zeroPoint.Lat - input.initialPos.Lat);
        input.initialPos.Long = Math.abs(zeroPoint.Long - input.initialPos.Long);
        // System.out.println("Middle way through initial pos calc");
        // printLatLong(input.initialPos);
        input.initialPos.Lat /= 0.0000027495;
        input.initialPos.Long /= 0.0000027495;
        //Everything is in feet now and defined via the zero point with rounding
    }
    
    private static latLong latLongToFeet(latLong i) {
        latLong j = new latLong(0, 0);
        j.Lat = Math.abs(zeroPoint.Lat - i.Lat);
        j.Long = Math.abs(zeroPoint.Long - i.Long);
        j.Lat /= 0.0000027495;
        j.Long /= 0.0000027495;
        return j;
    }

    //this works guaranteed
    private static latLong feetToLatLong (latLong i) {
        latLong j = new latLong(0, 0);
        j.Lat = i.Lat * 0.0000027495;
        j.Long = i.Long * 0.0000027495;
        j.Lat = zeroPoint.Lat - j.Lat;
        j.Long = zeroPoint.Long + j.Long;
        return j;
    }

    private static void makePolygon() {
        //The vertex map
        int nPoints = input.vertices.length;
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];
        //Lat is y/row; Long is x/col
        for (int i = 0; i<nPoints; i++) {
            xPoints[i] = (int) input.vertices[i].Long;
            yPoints[i] = (int) input.vertices[i].Lat;
        }
        vertexMap = new Polygon(yPoints, xPoints, nPoints);
        //The obstacles map
        obstacleMap = new Polygon[input.obstacles.length];
        for (int i = 0; i<input.obstacles.length; i++) {
            nPoints = input.obstacles[i].size();
            xPoints = new int[nPoints];
            yPoints = new int[nPoints];
            for (int j = 0; j<nPoints; j++) {
                xPoints[j] = (int) input.obstacles[i].get(j).Long;
                yPoints[j] = (int) input.obstacles[i].get(j).Lat;
            }
            obstacleMap[i] = new Polygon(yPoints, xPoints, nPoints);
        }
        
    }

    private static void booleanArrayGeneration() {
        int row = (int) farPoint.Lat;
        int col = (int) farPoint.Long;
        map = new boolean[row][col];
        //it is row, col, or y, x
        for (int i = 0; i<row; i++) { 
            for (int j = 0; j<col; j++) { 
                //checking for the vertexMap
                if (vertexMap.contains(i, j) == false) {
                    map[i][j] = true;
                }
                //checking for the obstaclesMap
                for (int k = 0; k<obstacleMap.length; k++) {
                    if (obstacleMap[k].contains(i, j) == true) {
                        map[i][j] = true;
                    }
                }
            }
        }
    }

{
    //Vertices for testing the length of a foot.
    //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 10 mm at that point

    //Vertex #1 Lat: 37.3453186 Long: -122.0160531
    //Vertex #2 Lat: 373453181 Long: -1220160558

    //Vertex #3 Lat: 373453179 Long: -1220160519
    //Vertex #4 Lat: 373453194 Long: -1220160546

    //Vertex: 5 Lat: 373453066 Long: -1220160597
    //Vertex: 6 Lat: 373453129 Long: -1220160625

    //Vertex 7 Lat: 373453149 Long: -1220160824
    //Vertex 8 Lat: 373453162 Long: -1220160803
}

    private static void printBoolArr() {
        for (int i = 0; i< (int) farPoint.Lat; i++) {
            for (int j = 0; j< (int) farPoint.Long; j++) {
                if (map[i][j]) {
                    System.out.print("1 ");
                }
                else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    public static void printLatLong(latLong i) {
        System.out.println(i.Lat + " " + i.Long);
    }

    private static void generateNodes() {
        nodes = new ArrayList<latLong>();
        nodeToNodeNum = new HashMap<latLong, Integer>();
        boolean check;
        latLong usable;
        for (int i = 1; i< (int) farPoint.Lat; i+=2) {
            for (int j = 1; j < (int) farPoint.Long; j+=2) {
                check = true;
                //check each test case
                if (map[i][j] == true) {
                    check = false;
                }
                if (map[i][j-1] == true) {
                    check = false;
                }
                if (map[i-1][j] == true) {
                    check = false;
                }
                if (map[i-1][j-1] == true) {
                    check = false;
                }
                if (check) {
                    usable = new latLong((double) i, (double) j);
                    nodes.add(usable);
                    nodeToNodeNum.put(usable, nodes.size()-1);
                }
                check = true;
            }
        }
    }

    private static void toClosestNode() {
        double minDist = Integer.MAX_VALUE;
        latLong placeholder;
        int savedI = 0;
        // System.out.println("( " + zeroPoint.Lat + " " + zeroPoint.Long + ")");
        // System.out.println("( " + input.initialPos.Long + ", " + input.initialPos.Lat + ")");
        for (int i = 0; i<nodes.size(); i++) {
            placeholder = new latLong(nodes.get(i).Lat, nodes.get(i).Long);
            // System.out.println("(" + placeholder.Long + ", " + placeholder.Lat + ")");
            // System.out.println(Math.pow(Math.abs(placeholder.Lat - input.initialPos.Lat), 2) + Math.pow(Math.abs(placeholder.Long - input.initialPos.Long), 2));
            if (Math.pow(Math.abs(placeholder.Lat - input.initialPos.Lat), 2) + Math.pow(Math.abs(placeholder.Long - input.initialPos.Long), 2) < minDist) {
                minDist = Math.pow(Math.abs(placeholder.Lat - input.initialPos.Lat), 2) + Math.pow(Math.abs(placeholder.Long - input.initialPos.Long), 2);
                savedI = i;
                // System.out.println(minDist);
            }
        }
        startNode = nodes.get(savedI);
    }
 
    private static int latLongToNodeNum(latLong Node) {
        for (int i = 0; i<nodes.size(); i++) {
            if (Node.Lat == nodes.get(i).Lat && Node.Long == nodes.get(i).Long) {
                return i;
            }
        }
        return -1;
    }

    private static void generateAdjacencyGraph() {
        adjacencyGraph = new ArrayList[nodes.size()];
        latLong placeholder;
        node placeholder2;
        for (int i = 0; i<nodes.size(); i++) {
            adjacencyGraph[i] = new ArrayList<node>();
        }
        for (int i = 0; i<nodes.size(); i++) {
            //north case
            placeholder = new latLong(nodes.get(i).Lat - 2.0, nodes.get(i).Long);
            if (latLongToNodeNum(placeholder) != -1) {
                placeholder2 = new node(latLongToNodeNum(placeholder), 1);
                adjacencyGraph[i].add(placeholder2);
            }
            //east
            placeholder = new latLong(nodes.get(i).Lat, nodes.get(i).Long + 2.0);
            if (latLongToNodeNum(placeholder) != -1) {
                placeholder2 = new node(latLongToNodeNum(placeholder), 2);
                adjacencyGraph[i].add(placeholder2);
            }
            //south
            placeholder = new latLong(nodes.get(i).Lat + 2.0, nodes.get(i).Long);
            if (latLongToNodeNum(placeholder) != -1) {
                placeholder2 = new node(latLongToNodeNum(placeholder), 3);
                adjacencyGraph[i].add(placeholder2);
            }
            //west
            placeholder = new latLong(nodes.get(i).Lat, nodes.get(i).Long - 2.0);
            if (latLongToNodeNum(placeholder) != -1) {
                placeholder2 = new node(latLongToNodeNum(placeholder), 4);
                adjacencyGraph[i].add(placeholder2);
            }
        }
    }

    private static void preDfs() {
        visited = new boolean[nodes.size()];
        connected = new ArrayList[nodes.size()];
        for (int i = 0; i<nodes.size(); i++) {
            connected[i] = new ArrayList<node>();
        }
        dfs(latLongToNodeNum(startNode));
    }

    private static void dfs(int currNodeNum) {
        visited[currNodeNum] = true;
        for (int i = 0; i<adjacencyGraph[currNodeNum].size(); i++) {
            if (visited[adjacencyGraph[currNodeNum].get(i).nodeNum] == false) {
                dfs(adjacencyGraph[currNodeNum].get(i).nodeNum);
                connected[currNodeNum].add(adjacencyGraph[currNodeNum].get(i));
                connected[adjacencyGraph[currNodeNum].get(i).nodeNum].add(new node(currNodeNum, reverseDir(adjacencyGraph[currNodeNum].get(i).direction)));
            }
        }
    }
    private static int reverseDir(int dir) {
        //1 = north, 2 = east, 3 = south, 4 = west
        if (dir == 1) {
            return 3;
        }
        else if (dir == 2) {
            return 4;
        }
        else if (dir == 3) {
            return 1;
        }
        else if (dir == 4) {
            return 2;
        }
        else {
            return 0;
        }
    }

    private static int degreeCalc(int currDirection, int nextDirection) {
        //1 = north, 2 = east, 3 = south, 4 = west
        if (currDirection == 1) {
            if (nextDirection == 1) {
                return 180;
            }
            else if (nextDirection == 2) {
                return 90;
            }
            else if (nextDirection == 3) {
                return 360;
            }
            else if (nextDirection == 4) {
                return 270;
            }
        }
        else if (currDirection == 2) {
            if (nextDirection == 1) {
                return 270;
            }
            else if (nextDirection == 2) {
                return 180;
            }
            else if (nextDirection == 3) {
                return 90;
            }
            else if (nextDirection == 4) {
                return 360;
            }
        }
        else if (currDirection == 3) {
            if (nextDirection == 1) {
                return 360;
            }
            else if (nextDirection == 2) {
                return 270;
            }
            else if (nextDirection == 3) {
                return 180;
            }
            else if (nextDirection == 4) {
                return 90;
            }
        }
        else if (currDirection == 4) {
            if (nextDirection == 1) {
                return 90;
            }
            else if (nextDirection == 2) {
                return 360;
            }
            else if (nextDirection == 3) {
                return 270;
            }
            else if (nextDirection == 4) {
                return 180;
            }
        }
        return 0;
    }

    private int calcOffOfDegree (int direction, int angle) {
        if (direction == 1) {
            if (angle == 180) {
                return 1;
            }
            else if (angle == 90) {
                return 2;
            }
            else if (angle == 360) {
                return 3;
            }
            else if (angle == 270) {
                return 4;
            }
        }
        else if (direction == 2) {
            if (angle == 270) {
                return 1;
            }
            else if (angle == 180) {
                return 2;
            }
            else if (angle == 90) {
                return 3;
            }
            else if (angle == 360) {
                return 4;
            }
        }
        else if (direction == 3) {
            if (angle == 360) {
                return 1;
            }
            else if (angle == 270) {
                return 2;
            }
            else if (angle == 180) {
                return 3;
            }
            else if (angle == 90) {
                return 4;
            }
        }
        else if (direction == 4) {
            if (angle == 90) {
                return 1;
            }
            else if (angle == 360) {
                return 2;
            }
            else if (angle == 270) {
                return 3;
            }
            else if (angle == 180) {
                return 4;
            }
        }

        return 0;
    }

    private static int[] priority(int direction) {
        int[] priorityOrder = new int[4];
        if (direction == 1) {
            priorityOrder[0] = 2;
            priorityOrder[1] = 1;
            priorityOrder[2] = 4;
            priorityOrder[3] = 3;
        }
        else if (direction == 2) {
            priorityOrder[0] = 3;
            priorityOrder[1] = 2;
            priorityOrder[2] = 1;
            priorityOrder[3] = 4;
        }
        else if (direction == 3) {
            priorityOrder[0] = 4;
            priorityOrder[1] = 3;
            priorityOrder[2] = 2;
            priorityOrder[3] = 1;
        }
        else if (direction == 4) {
            priorityOrder[0] = 1;
            priorityOrder[1] = 4;
            priorityOrder[2] = 3;
            priorityOrder[3] = 2;
        }
        return priorityOrder;
    }
    
    private static latLong go(latLong currPos, int dir, int fn) {
        if (((currPos.Lat == startPos.Lat && currPos.Long == startPos.Long) || (currPos.Lat == -500.0 && currPos.Long == -500.0)) && fn>3) {
            currPos.Lat = -500.0;
            currPos.Long = -500.0;
            return currPos;
        }
        if (dir == 1) {
            currPos.Lat += 0.0000027495;
            return currPos;
        }
        else if (dir == 2) {
            currPos.Long += 0.0000027495;
            return currPos;
        }
        else if (dir == 3) {
            currPos.Lat -= 0.0000027495;
            return currPos;
        }
        else if (dir == 4) {
            currPos.Long -= 0.0000027495;
            return currPos;
        }
        else {
            return currPos;
        }
    }

    private static int turn (int currDir) {
        if (currDir == 1) {
            return 4;
        }
        else if (currDir == 2) {
            return 1;
        }
        else if (currDir == 3) {
            return 2;
        }
        else if (currDir == 4) {
            return 3;
        }
        else {
            return 0;
        }
    }
    
    private static void output() {
        // 90 - stop, turn
        // 180 - forward
        // 270 - forward, turn, forward
        // 360 - forward, turn, forward, turn, forward

        finalNavigate = new ArrayList<latLong>();
        finalPath = new ArrayList<latLong>();

        latLong currPos = feetToLatLong(new latLong(startNode.Lat, startNode.Long));
        //System.out.println(currPos.Lat + " " + currPos.Long);
        //moving it down 0.5 ft, 0.5 ft
        currPos.Lat -= 0.00000137477975;
        currPos.Long += 0.00000137477975;
        startPos = new latLong(currPos.Lat, currPos.Long);
        finalNavigate.add(new latLong(currPos.Lat, currPos.Long));

        int currDir = 1;
        int currNode = latLongToNodeNum(startNode);
        int nextDir;
        int angle;
        int[] currPriorityOrder = new int[4];
        int ensure = 0;
        //System.out.println(startPos.Lat + " " + startPos.Long);
        int check = 0;
        while (true) {
            currPriorityOrder = priority(currDir);
            for (int i = 0; i<4; i++) { //looping through priorityorder
                //System.out.println(currPriorityOrder[0] + " " + currPriorityOrder[1] + " " + currPriorityOrder[2] + " " + currPriorityOrder[3]);
                for (int j = 0; j<connected[currNode].size(); j++) { //looping through connected to see to if if it has the priority order
                    nextDir = connected[currNode].get(j).direction;
                    if (nextDir == currPriorityOrder[i]) {
                        angle = degreeCalc(currDir, nextDir);
                        currNode = connected[currNode].get(j).nodeNum;
                        ensure += 1;
                        /* 
                        System.out.println();
                        System.out.println(angle);
                        System.out.println(currDir + " " + nextDir);
                        System.out.println(currPriorityOrder[0] + " " + currPriorityOrder[1] + " " + currPriorityOrder[2] + " " + currPriorityOrder[3]);
                        printLatLong(latLongToFeet(currPos));
                        printLatLong(currPos);
                        */
                    
                        if (angle == 90) {
                            //turn
                            //go
                            //add point
                            
                            //the turn
                            currDir = nextDir;
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                        }
                        if (angle == 180) {
                            //go
                            //add point
                            //go
                            //add point

                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            
                        }
                        if (angle == 270) {
                            //go
                            //add point
                            //turn
                            //go
                            //add point
                            //go
                            //add point

                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            currDir = turn(currDir);
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                        }
                        if (angle == 360) {
                            //go
                            //add point
                            //turn
                            //go 
                            //add point
                            //turn
                            //go
                            //add point
                            //go
                            //add point

                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            currDir = turn(currDir);
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            currDir = turn(currDir);
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                            currPos = go(currPos, currDir, ensure);
                            finalNavigate.add(new latLong(currPos.Lat, currPos.Long));
                        }
                        check = 1;
                        break;
                    }
                }
                if (check == 1) {
                    check = 0;
                    break;
                }
            }
            if ((currPos.Lat == startPos.Lat && currPos.Long == startPos.Long) || (currPos.Lat == -500.0 && currPos.Long == -500.0)) {
                break;
            }
        }
    }

    private static void inALine() {
        latLong start = new latLong(finalNavigate.get(0).Lat, finalNavigate.get(0).Long);
        latLong end = new latLong(finalNavigate.get(0).Lat, finalNavigate.get(0).Long);
        finalPath.add(new latLong(start.Lat, start.Long));
        boolean latDif = false;
        boolean longDif = false;
        for (int i = 1; i<finalNavigate.size(); i++) {


            //checks to see what has changed

            //the lat is changing
            if (finalNavigate.get(i).Lat != start.Lat) {
                latDif = true;
            }

            //the long is changing
            if (finalNavigate.get(i).Long != start.Long) {
                longDif = true;
            }

            // System.out.println("Thing we are currently processing");
            // printLatLong(finalNavigate.get(i));
            // System.out.println("Thing we are comparing it to");
            // printLatLong(start);

            // if (latDif) {
            //     System.out.println("Lat is changing right now.");
            // }
            // if (longDif) {
            //     System.out.println("Long is changing right now.");
            // }

            if (latDif ^ longDif) {
                end = new latLong(finalNavigate.get(i).Lat, finalNavigate.get(i).Long);
                // System.out.println("We have updated the end.");
            }

            //both have changed, reset
            if (latDif & longDif) {
                // System.out.println("Both have changed and now we are going to add the end to finalpath");
                finalPath.add(new latLong(end.Lat, end.Long));
                start = new latLong(end.Lat, end.Long);
                end = new latLong(finalNavigate.get(i).Lat, finalNavigate.get(i).Long);
                latDif = false;
                longDif = false;
            }

        }
    }


    //Main Method



    public static void spanningTreeCoverageAlgorithm(latLong initialPosition) {
        input.initialPos = new latLong(initialPosition.Lat, initialPosition.Long);
        //the entire input function
        input.Input();
        //determine zeroPoint (needed for later functions)
        determineZeroPoint();
        //convert everything that is in lat, long into feet, feet from the zero point
        convertObsAndVertToFeet();
        //make the polygon objects
        makePolygon();
        //create the boolean array of obstacles and non-obstacle space
        booleanArrayGeneration();
        //generate the nodes
        generateNodes();
        //find the closest node and define that as the startnode
        toClosestNode();
        //generate the adjacency graph of useable nodes
        generateAdjacencyGraph();
        //do the dfs (dfs the function is called in preDfs)
        preDfs();
        //the final output is the finalNavigate array once the function is done
        output();
        //printing out the final navigate array
        for (int i = finalNavigate.size()-1; i>0; i--) {
            if (finalNavigate.get(i).Lat == -500.0 && finalNavigate.get(i).Long == -500.0) {
                finalNavigate.remove(i);
            }
        }
        //optimizes finalNavigate
        inALine();
    }

    public static void main(String[] args) {
        //the entire input function
        input.Input();
        //determine zeroPoint (needed for later functions)
        determineZeroPoint();
        //convert everything that is in lat, long into feet, feet from the zero point
        convertObsAndVertToFeet();
        //make the polygon objects
        makePolygon();
        //create the boolean array of obstacles and non-obstacle space
        booleanArrayGeneration();
        //generate the nodes
        generateNodes();
        //find the closest node and define that as the startnode
        toClosestNode();
        //generate the adjacency graph of useable nodes
        generateAdjacencyGraph();
        //do the dfs (dfs the function is called in preDfs)
        preDfs();
        //the final output is the finalNavigate array once the function is done
        output();
        //printing out the final navigate array
        finalNavigate.remove(finalNavigate.size()-1);
        // for (int i = 0; i<finalNavigate.size(); i++) {
        //     System.out.println("(" + finalNavigate.get(i).Long + ", " + finalNavigate.get(i).Lat + ")");
        // }
        inALine();
        // System.out.println("( " + startNode.Long + ", " + startNode.Lat + ")");
    }
}
