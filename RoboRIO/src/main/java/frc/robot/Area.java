// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//The point of this program is to define a map. 

package frc.robot;


public class Area {

    int xDimension;
    int yDimension;
    boolean[][] map;

    public Area() {
        // Inputs will be taken eventually; for now, we are using the example that we have defined
        xDimension = 8;
        yDimension = 12;
        boolean[][] arr = new boolean[12][8];
        //Each true square stands for a obstacle there
        //There is probably a more efficient use of storage; however, for now this should suffice
        for (int i = 4; i<6; i++) {
			for (int j = 2; j<4; j++) {
				arr[i][j] = true;
			}
		}
		for (int i = 5; i<8; i++) {
			for (int j = 3; j<5; j++) {
				arr[i][j] = true;
			}
		}
		for (int i = 6; i<9; i++) {
			for (int j = 4; j<6; j++) {
				arr[i][j] = true;
			}
		}
        map = arr;
    }
}
