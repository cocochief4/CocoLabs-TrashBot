package frc.robot;

import java.awt.Polygon;
import java.text.DecimalFormat;
import java.util.*;

/*
Note for this program:
A lot of this processing could be done at the same time; however, for the sake of understanding the code and debugging, I have used seperate methods.
*/

public class STC {

    //Global Variable Declaration

    latLong initialPos;
    latLong[] vertices; //the vertices that bound our park
    ArrayList<latLong>[] obstacles; //the array of arraylists for obstacle points
    latLong zeroPoint; //the top left most point in the array
    latLong farPoint; //the max point used for defining the boolean array
    Polygon vertexMap; //the polygon for the vertices; used for determining the boolean array
    Polygon[] obstacleMap; //an array that holds the obstacles; used for determining the boolean array
    boolean[][] map; //the actual boolean array

    ArrayList<latLong> nodes; //list of usable nodes
    HashMap<latLong, Integer> nodeToNodeNum; //a map that's useful for figuring out my life
    ArrayList<node>[] adjacencyGraph; //an adjacency graph for which nodes are adjacent to each other
    boolean[] visited;

    //Class Declaration

    private class latLong {
        double Lat;
        double Long;

        private latLong (double latitude, double longitude) {
            Lat = latitude;
            Long = longitude;
        }
    }

    private class node {
        int nodeNum;
        int direction; //1 = north, 2 = east, 3 = south, 4 = west

        private node (int nodeNumber, int dir) {
            nodeNum = nodeNumber;
            direction = dir;
        }
    }

    //Method Declaration

    /*
    In the input method, you manually type in the vertices and the obstacles.
    It is also used for init'ing the variables.
    Note for Aayush: Talk to Chris about this testcase. Next meeting, we should use the gps and measure out our test case. 
     */

    private void input() { 
        initialPos = new latLong(37.3453186, -122.0160531);
        int nVertices = 4; //4 is just a placeholder for now
        vertices = new latLong[nVertices];
        //Here you would define each lat, long in vertices


        int nObstacles = 1; //1 is just a placeholder for now
        obstacles = new ArrayList[nObstacles];
        for (int i = 0; i<nObstacles; i++) {
            obstacles[i] = new ArrayList<latLong>();
        }
        //Here you would define each obstacle as an arraylist of lat, longs


    }
    private void determineZeroPoint() {
        double maxLat = -10000.0;
        double minLong = 10000.0;
        for (int i = 0; i<vertices.length; i++) {
            if (vertices[i].Lat > maxLat) {
                maxLat = vertices[i].Lat;
            }
            if (vertices[i].Long < minLong) {
                minLong = vertices[i].Long;
            }
        }
        zeroPoint = new latLong(maxLat, minLong);
    }

    private void fromLatLongToFeet() {
        //Use 1 ft = 0.0000027495495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 10 mm at that point
        double minLat = 100000.0;
        double maxLong = -100000.0;
        for (int i = 0; i<vertices.length; i++) {
            vertices[i].Lat = Math.abs(zeroPoint.Lat - vertices[i].Lat);
            vertices[i].Long = Math.abs(zeroPoint.Long - vertices[i].Long);
            //Convert to feet actually
            vertices[i].Lat /= 0.0000027495495;
            vertices[i].Long /= 0.0000027495495;
            vertices[i].Lat = Math.floor(vertices[i].Lat);
            vertices[i].Long = Math.floor(vertices[i].Long);
            if (vertices[i].Lat < minLat) {
                minLat = vertices[i].Lat;
            }
            if (vertices[i].Long > maxLong) {
                maxLong = vertices[i].Long;
            }
        }
        farPoint = new latLong(minLat, maxLong);
        for (int i = 0; i<obstacles.length; i++) {
            for (int j = 0; j<obstacles[i].size(); j++) {
                obstacles[i].get(j).Lat = Math.abs(zeroPoint.Lat - obstacles[i].get(j).Lat);
                obstacles[i].get(j).Long = Math.abs(zeroPoint.Long - obstacles[i].get(j).Long);
                //Convert to feet actually
                obstacles[i].get(j).Lat /= 0.0000027495495;
                obstacles[i].get(j).Long /= 0.0000027495495;
                obstacles[i].get(j).Lat = Math.floor(obstacles[i].get(j).Lat);
                obstacles[i].get(j).Long = Math.floor(obstacles[i].get(j).Long);
            }
        }
        initialPos.Lat /= 0.0000027495495;
        initialPos.Long /= 0.0000027495495;
        //Everything is in feet now and defined via the zero point with rounding
    }
    
    private latLong feetToLatLong (latLong i) {
        i.Lat *= 0.0000027495495;
        i.Long *= 0.0000027495495;
        i.Lat = zeroPoint.Lat - i.Lat;
        i.Long += zeroPoint.Long;
        return i;

        //this only works for our section of the earth
    }

    private void makePolygon() {
        //The vertex map
        int nPoints = vertices.length;
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];
        //Lat is y/row; Long is x/col
        for (int i = 0; i<nPoints; i++) {
            xPoints[i] = (int) vertices[i].Long;
            yPoints[i] = (int) vertices[i].Lat;
        }
        vertexMap = new Polygon(xPoints, yPoints, nPoints);
        //The obstacles map
        obstacleMap = new Polygon[obstacles.length];
        for (int i = 0; i<obstacles.length; i++) {
            nPoints = obstacles[i].size();
            xPoints = new int[nPoints];
            yPoints = new int[nPoints];
            for (int j = 0; j<nPoints; j++) {
                xPoints[j] = (int) obstacles[i].get(j).Long;
                yPoints[j] = (int) obstacles[i].get(j).Lat;
            }
            obstacleMap[i] = new Polygon(xPoints, yPoints, nPoints);
        }
    }

    private void booleanArrayGeneration() {
        int row = (int) farPoint.Lat;
        int col = (int) farPoint.Long;
        map = new boolean[row][col];
        for (int i = 0; i<row; i++) {
            for (int j = 0; j<col; j++) {
                //checking for the vertexMap
                if (!(vertexMap.contains(i, j))) {
                    map[i][j] = true;
                }
                //checking for the obstaclesMap
                for (int k = 0; k<obstacleMap.length; k++) {
                    if (!(obstacleMap[k].contains(i, j))) {
                        map[i][j] = true;
                    }
                }
            }
        }
    }

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

    private static double pytho(latLong a, latLong b) {
        double diffLat = Math.abs(a.Lat - b.Lat);
        double diffLong = Math.abs(a.Long - b.Long);
        double hypo = Math.sqrt((diffLat * diffLat) + (diffLong * diffLong));
        return hypo;
    }

    private void generateNodes() {
        nodes = new ArrayList<latLong>();
        nodeToNodeNum = new HashMap<>();
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

    private latLong toClosestNode() {
        double minDist = 10000000;
        latLong placeholder;
        int savedI = 0;
        for (int i = 0; i<nodes.size(); i++) {
            placeholder = feetToLatLong(nodes.get(i));
            if (Math.pow(Math.abs(placeholder.Lat - initialPos.Lat), 2) + Math.pow(Math.abs(placeholder.Long - initialPos.Long), 2) < minDist) {
                minDist = Math.pow(Math.abs(placeholder.Lat - initialPos.Lat), 2) + Math.pow(Math.abs(placeholder.Long - initialPos.Long), 2);
                savedI = i;
            }
        }
        placeholder = nodes.get(savedI);
        return placeholder;
    }
 
    private void navigate(latLong after) {
        
        //this method is a placeholder for chris to use for his navigate function
    
    }

    private void generateAdjacencyGraph() {
        adjacencyGraph = new ArrayList[nodes.size()];
        latLong placeholder;
        node placeholder2;
        for (int i = 0; i<nodes.size(); i++) {
            adjacencyGraph[i] = new ArrayList<node>();
        }
        for (int i = 0; i<nodes.size(); i++) {
            //north case
            placeholder = new latLong(nodes.get(i).Lat - 2.0, nodes.get(i).Long);
            if (nodeToNodeNum.containsKey(placeholder)) {
                placeholder2 = new node(nodeToNodeNum.get(placeholder), 1);
                adjacencyGraph[i].add(placeholder2);
            }
            //east
            placeholder = new latLong (nodes.get(i).Lat, nodes.get(i).Long + 2.0);
            if (nodeToNodeNum.containsKey(placeholder)) {
                placeholder2 = new node(nodeToNodeNum.get(placeholder), 2);
                adjacencyGraph[i].add(placeholder2);
            }
            //south
            placeholder = new latLong (nodes.get(i).Lat + 2.0, nodes.get(i).Long);
            if (nodeToNodeNum.containsKey(placeholder)) {
                placeholder2 = new node(nodeToNodeNum.get(placeholder), 3);
                adjacencyGraph[i].add(placeholder2);
            }
            //west
            placeholder = new latLong (nodes.get(i).Lat, nodes.get(i).Long - 2.0);
            if (nodeToNodeNum.containsKey(placeholder)) {
                placeholder2 = new node(nodeToNodeNum.get(placeholder), 4);
                adjacencyGraph[i].add(placeholder2);
            }
        }
    }

    private void preDfs() {
        visited = new boolean[nodes.size()];
    }

    private void dfs() {
        
    }

    //Main Method

    public static void main(String[] args) {

        /* The below code was used for vertex calculation
        latLong v1 = new latLong(37.3453186, -122.0160531);
        latLong v2 = new latLong(37.3453181, -122.0160558);
        latLong v3 = new latLong(37.3453179, -122.0160519);
        latLong v4 = new latLong(37.3453194, -122.0160546);
        latLong v5 = new latLong(37.3453066, -122.0160597);
        latLong v6 = new latLong(37.3453129, -122.0160625);
        latLong v7 = new latLong(37.3453149, -122.0160824);
        latLong v8 = new latLong(37.3453162, -122.0160803);
        double test = pytho(v1, v2);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(15);
        System.out.println(df.format(test));
        test = pytho(v3, v4);
        System.out.println(df.format(test));
        test = pytho(v5, v6);
        System.out.println(df.format(test));
        test = pytho(v7, v8);
        System.out.println(df.format(test)); */


    }


}
