/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.findrobotpath.findPath;


import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author frane
 */
public class Blushfire {

    private int[][] map;
    private int x_size;
    private int y_size;

    public Blushfire(int[][] map) {
        this.map = map;
        calcMapSize();
    }
    private void calcMapSize(){
      y_size=map.length-1;
      x_size=0;
      
    // to find real y_size we have to find max y_size          
    for (int[] row : map) {
        if((row.length-1)>x_size)
            x_size=row.length;
        }
    }
////    public Blushfire(int[][] map) {
////        this.map = IntStream.range(0, x_size);    
////    }
//////    private float[][] convertIntToFloat(int[][] input){
//////        float[][] output=new float[input.length][];
//////        IntStream.range(0, floatArray.length).mapToDouble(i -> floatArray[i]).toArray()
//////    }
    private class Cell{
        int x,y;
        Cell(int x, int y){
            this.x=x;this.y=y;
        }
    }
    public void doBlushfire(){
        List<Cell> closed_list;
        List<Cell> open_list;
        
        
    }
    
    
    //FUNCTION wavefront algorithm to find most efficient path to goal
    public void wavefrontSearch()
    {
      int goal_x, goal_y;
      y_size=map.length-1;
      x_size=0;
      
    // to find real y_size we have to find max y_size          
    for (int[] row : map) {
        if((row.length-1)>x_size)
            x_size=row.length;
        }
              
      boolean foundWave = true;
      int currentWave = 2; //Looking for goal first

      while(foundWave == true)
      {
        foundWave = false;
        for(int y=0; y < y_size; y++)
        {
          for(int x=0; x < x_size; x++)
          {
            if(map[x][y] == currentWave)
            {
              foundWave = true;
              goal_x = x;
              goal_y = y;

              if(goal_x > 0) //This code checks the array bounds heading WEST
                if(map[goal_x-1][goal_y] == 0)  //This code checks the WEST direction
                  map[goal_x-1][goal_y] = currentWave + 1;

              if(goal_x < (x_size - 1)) //This code checks the array bounds heading EAST
                if(map[goal_x+1][goal_y] == 0)//This code checks the EAST direction
                  map[goal_x+1][goal_y] = currentWave + 1;

              if(goal_y > 0)//This code checks the array bounds heading SOUTH
                if(map[goal_x][goal_y-1] == 0) //This code checks the SOUTH direction
                  map[goal_x][goal_y-1] = currentWave + 1;

              if(goal_y < (y_size - 1))//This code checks the array bounds heading NORTH
                if(map[goal_x][goal_y+1] == 0) //This code checks the NORTH direction
                  map[goal_x][goal_y+1] = currentWave + 1;
            }
          }
        }
        currentWave++;
        printWavefrontMap();
        
      }
    }
    
    //FUNCTION print wavefront map to NXT screen
    void printWavefrontMap()
    {
      Integer printLine = y_size-1;
      for(int y = 0; y < y_size; y++)
      {
        String printRow = "";
        for(int x=0; x < x_size; x++)
        {
          if(map[x][y] == 99)
            printRow = printRow + "R ";
          else if(map[x][y] == 2)
            printRow = printRow + "G ";
          else if(map[x][y] == 1)
            printRow = printRow + "X ";
          else if(map[x][y] < 10)
            printRow = printRow + map[x][y] + " ";
          else if(map[x][y] == '*')
            printRow = printRow + "* ";
          else
            printRow = printRow + map[x][y];
        }
        //System.out.println(printLine, printRow);
        printLine--;
      }
      
    }
}
