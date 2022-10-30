// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package stcpack;
import java.util.*;
import frc.robot.*;
import stcpack.stc.*;

/** Add your docs here. */
public class input {

    public static latLong initialPos;
    public static latLong[] vertices; //the vertices that bound our park
    public static ArrayList<latLong>[] obstacles; //the array of arraylists for obstacle points

    /*
    In the input method, you manually type in the vertices and the obstacles.
    It is also used for init'ing the variables.
     */

    public static void Input() { 
        //initialPos = new latLong(37.3453149 - 0.00000137477975, -122.0160824 + 0.00000137477975); //update this
        int nVertices = 4; //4 is just a placeholder for now
        vertices = new latLong[nVertices];
        //Here you would define each lat, long in vertices
        //Top left Corner: Lat: 37.3453149 Long: -122.0160824
        vertices[0] = new latLong(373452954E-7,-1220160290E-7);
        vertices[1] = new latLong(373452945E-7,-1220160639E-7);
        vertices[2] = new latLong(373453269E-7,-1220160645E-7);
        vertices[3] = new latLong(373453265E-7,-1220160292E-7);


        int nObstacles = 0; //1 is just a placeholder for now
        obstacles = new ArrayList[nObstacles];
        for (int i = 0; i<nObstacles; i++) {
            obstacles[i] = new ArrayList<latLong>();
        }
        //Here you would define each obstacle as an arraylist of lat, longs
        // obstacles[0].add(new latLong(373453106E-7,-1220160351E-7));
        // obstacles[0].add(new latLong(373453112E-7,-1220160446E-7));
        // obstacles[0].add(new latLong(373453067E-7,-1220160432E-7));
    }

//other test cases

{
    vertices[0] = new latLong(37.3453149, -122.0160824);
        vertices[1] = new latLong(37.3453149, -122.0160824 + 20 * 0.0000027495);
        vertices[2] = new latLong(37.3453149 - 10 * 0.0000027495, -122.0160824 + 20 * 0.0000027495);
        vertices[3] = new latLong(37.3453149 - 10 * 0.0000027495, -122.0160824);

        obstacles[0].add(new latLong(37.3453149 - 2 * 0.0000027495, -122.0160824 + 2 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 2 * 0.0000027495, -122.0160824 + 3 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 5 * 0.0000027495, -122.0160824 + 3 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 5 * 0.0000027495, -122.0160824 + 4 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 4 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 0 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 4 * 0.0000027495, -122.0160824 + 0 * 0.0000027495));
        obstacles[0].add(new latLong(37.3453149 - 4 * 0.0000027495, -122.0160824 + 2 * 0.0000027495));

        obstacles[1].add(new latLong(37.3453149 - 1 * 0.0000027495, -122.0160824 + 9 * 0.0000027495));
        obstacles[1].add(new latLong(37.3453149 - 1 * 0.0000027495, -122.0160824 + 16 * 0.0000027495));
        obstacles[1].add(new latLong(37.3453149 - 2 * 0.0000027495, -122.0160824 + 16 * 0.0000027495));
        obstacles[1].add(new latLong(37.3453149 - 2 * 0.0000027495, -122.0160824 + 9 * 0.0000027495));

        obstacles[2].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 7 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 8 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 8 * 0.0000027495, -122.0160824 + 8 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 8 * 0.0000027495, -122.0160824 + 10 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 10 * 0.0000027495, -122.0160824 + 10 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 10 * 0.0000027495, -122.0160824 + 5 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 8 * 0.0000027495, -122.0160824 + 5 * 0.0000027495));
        obstacles[2].add(new latLong(37.3453149 - 8 * 0.0000027495, -122.0160824 + 7 * 0.0000027495));

        obstacles[3].add(new latLong(37.3453149 - 4 * 0.0000027495, -122.0160824 + 12 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 4 * 0.0000027495, -122.0160824 + 15 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 15 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 17 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 8 * 0.0000027495, -122.0160824 + 17 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 8 * 0.0000027495, -122.0160824 + 13 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 13 * 0.0000027495));
        obstacles[3].add(new latLong(37.3453149 - 6 * 0.0000027495, -122.0160824 + 12 * 0.0000027495));
}


{
    vertices[0] = new latLong(37.3453186, -122.0160531);
    vertices[1] = new latLong(37.3453186, -122.0160531 + 8 * 0.0000027495);
    vertices[2] = new latLong(37.3453186 - 12 * 0.0000027495, -122.0160531 + 8 * 0.0000027495);
    vertices[3] = new latLong(37.3453186 - 12 * 0.0000027495, -122.0160531);
    obstacles[0].add(new latLong(37.3453186 - 4 * 0.0000027495, -122.0160531 + 2 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 4 * 0.0000027495, -122.0160531 + 4 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 5 * 0.0000027495, -122.0160531 + 4 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 5 * 0.0000027495, -122.0160531 + 5 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 6 * 0.0000027495, -122.0160531 + 5 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 6 * 0.0000027495, -122.0160531 + 6 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 9 * 0.0000027495, -122.0160531 + 6 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 9 * 0.0000027495, -122.0160531 + 4 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 8 * 0.0000027495, -122.0160531 + 4 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 8 * 0.0000027495, -122.0160531 + 3 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 6 * 0.0000027495, -122.0160531 + 3 * 0.0000027495));
    obstacles[0].add(new latLong(37.3453186 - 6 * 0.0000027495, -122.0160531 + 2 * 0.0000027495));
}
}
