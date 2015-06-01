/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.findrobotpath.findPath;

import java.io.File;

/**
 *
 * @author frane
 */
public class BlushfireNew {
    /* obstacles (such as walls or columns) have the value of 0,
    free spaces have the value of 255.
    */
    int nothing=255;
    int wall=0;
    
    int goal=254;
    int robot=1;
    
    /* You dont need this values below for this task, 
    they are here in case you needit in the future.
    */
    //declare starting robot/goal locations
    int robot_x=5;
    int robot_y=3;
    int goal_x=0;
    int goal_y=3;

    //map locations
    int x=0;
    int y=0;

    //temp variables
    int temp_A=0;
    int temp_B=0;
    int counter=0;
    int steps=0;//to determine how processor intensive the algorithm was

    //when searching for a node with a lower value
    int minimum_node=250;
    int max_node_location=5;
    int new_state=1;
    int old_state=1;
    int trans=50;
    int reset_min=5;//anything below this number is a special item, ie a wall or robot

    //int[][] map;
    //X is vertical, Y is horizontal
    int[][] map=    {{0,0,0,0,0,0},
                     {0,0,0,0,0,0},
                     {0,0,0,0,0,0},
                     {255,255,255,255,0,0},
                     {0,0,0,0,0,0},
                     {0,0,0,0,0,0}};
				 

    public BlushfireNew()
    {//int[][] map
        //this.map=map;
        File out = new File("results.txt");
        //fprintf(out,"Starting Wavefront\n");
        System.out.println("Starting Wavefront\n\n");

        //////////////wavefront code//////////////
        while(map[robot_x][robot_y]!=goal)
        {
            //find new location to go to
            new_state=propagate_wavefront(robot_x,robot_y);
            //update new location of robot
            if (new_state==1)
            {
                robot_x--;
                //System.out.println("x=%d y=%d\n\n",robot_x,robot_y);
            }
            if (new_state==2)
            {
                robot_y++;
                //System.out.println("x=%d y=%d\n\n",robot_x,robot_y);
            }
            if (new_state==3)
            {
                robot_x++;
                //System.out.println("x=%d y=%d\n\n",robot_x,robot_y);
            }
            if (new_state==4)
            {
                robot_y--;
                //System.out.println("x=%d y=%d\n\n",robot_x,robot_y);
            }

/*
            //if not pointed in the right direction, rotate
            if (abs(old_state - new_state) == 2)//rotate 180 degrees
                    rotate_CCW(200,200);
            if ((old_state - new_state) == 1 || (signed int)(old_state - new_state) == -3)//rotate 90 degrees CW
                    rotate_CW(100,200);
            if ((signed int)(old_state - new_state) == -1 || (old_state - new_state) == 3)//rotate 90 degrees CCW
                    rotate_CCW(100,200);

            //go to new location
            straight(30,100);*/

            //make new state the old state
            old_state=new_state;
            trans--;
        }
        //////////////////////////////////////////
        System.out.println("steps: %d\n"+steps);
        //fclose(out);
        //system("PAUSE");
    }


    int propagate_wavefront(int robot_x, int robot_y)
    {
	//clear old wavefront
	//unpropagate(robot_x, robot_y);
	
	//start location to begin scan at goal location
	//map[goal_x][goal_y]=goal;//goal at 3,3 for error
	
	System.out.println("Adding Goal:\n");
	print_map();

        counter=0;
        while(counter<50)//allows for recycling until robot is found
        {
            x=0;
            y=0;
            while(x<6 && y<6)//while the map hasnt been fully scanned
            {
                //if this location is a wall or the goal, just ignore it
                if (map[x][y] != wall && map[x][y] != goal)
                {	
                    //a full trail to the robot has been located, finished!
                    if (min_surrounding_node_value(x, y) < reset_min && map[x][y]==robot)
                    {
                        System.out.println("Finished Wavefront:\n");
                        print_map();
                        //finshed! tell robot to start moving down path
                        return max_node_location;
                    }
                    //record a value in to this node
                    else if (minimum_node!=reset_min)//if this isnt here, 'nothing' will go in the location
                        map[x][y]=minimum_node+1;
                }
                //go to next node and/or row
                y++;
                if (y==6 && x!=6)
                {
                    x++;
                    y=0;
                }
            }
            System.out.println("Sweep #: %d\n"+counter+1);
            print_map();
            counter++;
        }
        return 0;
    }

    void unpropagate(int robot_x, int robot_y)//clears old path to determine new path
    {	
	System.out.println("Old Map:\n");
	print_map();
	//System.out.println("Starting Unpropagate\n");
	//stay within boundary
	for (x=0; x<6; x++)
	    for (y=0; y<6; y++)
		    if (map[x][y] != wall && map[x][y] != goal) //if this location is a wall or goal, just ignore it
		       map[x][y] = nothing;//clear that space

	//old robot location was deleted, store new robot location in map
	map[robot_x][robot_y]=robot;
	
	System.out.println("Unpropagation Complete:\n");
	//fprintf(out, "Unpropagation Complete:\n");
	print_map();
    }

    //this function looks at a node and returns the lowest value around that node
    int min_surrounding_node_value(int x, int y)
    {
	minimum_node=reset_min;//reset minimum

	//down
	if(x < 5)//not out of boundary
	if  (map[x+1][y] < minimum_node && map[x+1][y] != nothing)//find the lowest number node, and exclude empty nodes (0's)
        {
            minimum_node = map[x+1][y];
            max_node_location=3;
        }
	//up
	if(x > 0)
        {
            if  (map[x-1][y] < minimum_node && map[x-1][y] != nothing)
            {
		minimum_node = map[x-1][y];
		max_node_location=1;
            }
        }
	
	//right
	if(y < 5)
        {
            if  (map[x][y+1] < minimum_node && map[x][y+1] != nothing)
            {
                minimum_node = map[x][y+1];
                max_node_location=2;
            }
        }
            
	//left
	if(y > 0)
        {
            if  (map[x][y-1] < minimum_node && map[x][y-1] != nothing)
            {
                minimum_node = map[x][y-1];
                max_node_location=4;
            }
        }
        return minimum_node;
    }
    
    void print_map()
    {
        for (temp_B=0;temp_B<6;temp_B++)
        {
            for (temp_A=0;temp_A<6;temp_A++)
            {
                if (map[temp_B][temp_A]==wall)
                    System.out.println("W ");
                else if (map[temp_B][temp_A]==robot)
                    System.out.println("R ");
                else if (map[temp_B][temp_A]==goal)
                    System.out.println("G ");
                else
                    System.out.println("%d "+map[temp_B][temp_A]);
                //fprintf(out, "%d",map[temp_A][temp_B]);
            }
            System.out.println("\n");//then go to next line Y
            //fprintf(out, "\n");
            }
        System.out.println("\n");
        steps++;
    }
}
