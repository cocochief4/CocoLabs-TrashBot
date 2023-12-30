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
        //initialPos = new latLong(37.3453020, -122.0160470); //update this
        int nVertices = 4; //4 is just a placeholder for now
        vertices = new latLong[nVertices];
        //Here you would define each lat, long in vertices
        //Top left Corner: Lat: 37.3453149 Long: -122.0160824

        // akhil coords
        vertices[0] = new latLong(373501115E-7,-1220187724E-7);
        vertices[1] = new latLong(373501118E-7,-1220187355E-7);
        vertices[2] = new latLong(373500866E-7,-1220187373E-7);
        vertices[3] = new latLong(373500847E-7,-1220187709E-7);

        //House Coords
        // vertices[0] = new latLong(373473899E-7,-1220184057E-7);
        // vertices[1] = new latLong(373474542E-7,-1220184084E-7);
        // vertices[2] = new latLong(373474558E-7,-1220184835E-7);
        // vertices[3] = new latLong(373473930E-7,-1220184798E-7);
        
        //Panama Park Coords decaperated
        // vertices[0] = new latLong(373471167E-7,-1220185661E-7);
        // vertices[1] = new latLong(373471892E-7,-1220185660E-7);
        // vertices[2] = new latLong(373471878E-7,-1220186348E-7);
        // vertices[3] = new latLong(373471216E-7,-1220186298E-7);


        int nObstacles = 1; //1 is just a placeholder for now
        obstacles = new ArrayList[nObstacles];
        for (int i = 0; i<nObstacles; i++) {
            obstacles[i] = new ArrayList<latLong>();
        }

        obstacles[0].add(new latLong(373474227E-7,-1220184590E-7));
        obstacles[0].add(new latLong(373474216E-7,-1220184400E-7));
        obstacles[0].add(new latLong(373474369E-7,-1220184379E-7));
        obstacles[0].add(new latLong(373474388E-7,-1220184539E-7));
        
// home obstacle coords
        // obstacles[0].add(new latLong((373453099-30)*1E-7, (-1220160341 - 30)*1E-7));
        // obstacles[0].add(new latLong((373453099+30)*1E-7, (-1220160341 - 30)*1E-7));
        // obstacles[0].add(new latLong((373453099-30)*1E-7, (-1220160341 + 30)*1E-7));
        // obstacles[0].add(new latLong((373453099+30)*1E-7, (-1220160341 + 30)*1E-7));
        //Here you would define each obstacle as an arraylist of lat, longs
        // Panama Park obstacle decaprated
        // obstacles[0].add(new latLong(373471462E-7,-1220186094E-7));
        // obstacles[0].add(new latLong(373471455E-7,-1220185970E-7));
        // obstacles[0].add(new latLong(373471343E-7,-1220185972E-7));
        // obstacles[0].add(new latLong(373471353E-7,-1220186072E-7));
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
