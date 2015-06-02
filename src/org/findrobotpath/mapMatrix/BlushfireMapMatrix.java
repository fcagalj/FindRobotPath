/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.findrobotpath.mapMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.findrobotpath.main.MatrixImageTools;

/**
 * Perform blushfire algorithm on entered array matrix map[][] an map
 * all cells in matrix. If array matrix is not entered in argument, class
 * will perform algorithm on predifend value of variable int[][] map.
 * @author frane
 */
public class BlushfireMapMatrix {
    
    private boolean debug=false; //flag to debaug logging
    
    //private File outputImageFile;
    //private File outputCSVFile;
    private String outputImageFilePath;
    private String outputCSVFilePath;
    
    //Array matrix (testin value)
    private int[][] map=    {{  0,   0,   0,   0,    0,   0},
                     {255,   0, 255, 255,    0,   0},
                     {  0,   0,   0,   0,  255,   0},
                     {  0,   0,   0,   0,  255,   0},
                     {255, 255, 255, 255,  255,   0},
                     {255, 255, 255, 255,  255,   0},
                     {  0,   0,   0,   0,  255, 255}};
				  
    private int freeBlockValue=255;
    
    public BlushfireMapMatrix(String outputImagepath, String outputCSVPath)
    {
        this.outputCSVFilePath=outputCSVPath;
        this.outputImageFilePath=outputImagepath;
        ///////////////////////////////////////////////
        //this.outputImageFile=new File(outputImagepath);
        //this.outputCSVFile=new File(outputCSVPath);
    }
    public BlushfireMapMatrix(int[][] map, String outputImagepath, String outputCSVPath)
    {
        this(outputImagepath, outputCSVPath);
        this.map=MatrixImageTools.transponseMatrix255values(map);
        mapMatrix(this.map);
    }
    
    /**
     * Representing each cell in matrix with x, y values.
     */
    private class Cell{
        int x,y;
        //int value;
        Cell(int x, int y){
            this.x=x;this.y=y;
        }
        //This method is used when tw Cells are compareing, when 
        //adding or removing from the list.
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Cell other = (Cell) obj;
            return ((other.x==this.x)&&(other.y==this.y));
        }
        
    }
    /**
     * Loop trough matrix and mapping each cell.
     * 
     * At the end export output matrix in 
     * outptImage and outputCSV file.
     */
    public void mapMatrix(int[][] map){
        List<Cell> openedList=new ArrayList();
        List<Cell> closedList=new ArrayList();
        populateOpenedAndClosedList(map, openedList, closedList); // To populate Opened and closed list
        if(debug){
            System.out.println("Start mapping values: ");
            MatrixImageTools.printMapToConsole(map);
            MatrixImageTools.exportMapToCSV(map, "_output//startMappingCSV.csv");
            MatrixImageTools.exportMatrixToImageFile(map, "_output//startMappingImage.png");
            
        }
        int map_y=map[0].length;    //matrix y size
        int map_x=map.length; //matrix x size
        
        int itter = 0;
        while(!openedList.isEmpty()){ //isempty(open_list)==0
            itter++;
            for(int i=0;i<map_x;i++){ //for i = 1:length(X) 
//                boolean isInBorder=isInBorder(i,map);
//                if(isInBorder){
//                    addColumnToList(closedList, map, i);
//                    removeColumnFromList(openedList, map, i);
//                }
//                else if(!isInBorder){
//                    if(debug){
//                        System.out.println("*****************************\n  "+itter+". iterration, "+i+" row.");
//                        System.out.println("    Opened cells: ");this.printListState(openedList);
//                        System.out.println("    Closed cells: ");this.printListState(closedList);
//                    }
                    
                    for(int j=0;j<map_y;j++)//loop trought column for each row
                    {
                        //if(debug)System.out.println("        Current cell (freBlock): |"+i+","+j+"| (value "+map[i][j]+")");
                        Cell current=new Cell(i,j);
                        //if(debug){System.out.println("      Current cell: |"+i+","+j+"| (value "+map[i][j]+")");}
                        if((openedList.contains(current))){//ignore obstacles //&&((map[i][j]==freeBlockValue)||(map[i][j]<itter))
                            
                            if((map[i][j])==freeBlockValue){
                                map[i][j]=itter;
//                                closedList.add(current);
//                                openedList.remove(current);
                            }
                            else if((map[i][j])<itter){
                                map[i][j]++;
                                closedList.add(current);
                                openedList.remove(current);
                            }
                            List<Cell> neigh_X=new ArrayList();
                            findCellNeighbors(neigh_X, map, i, j);
                            //findNeighborsForColumnFreeElments(neigh_X, map,i); //Store Neighbors in list neigh_X
                            int maxItter=0;
                            for(Cell neighbor:neigh_X){
                                if(debug){
                                    System.out.println("             Current neighbor: |"+neighbor.x+","+neighbor.y+"|(value="+map[neighbor.x][neighbor.y]+", itter="+itter+") ");
                                }
//                                if((map[neighbor.x][neighbor.y])!=0){
//                                    maxItter=map[neighbor.x][neighbor.y];
//                                }
                                //if(openedList.contains(neighbor)){
                                if(((map[neighbor.x][neighbor.y])!=0)&&((map[neighbor.x][neighbor.y])!=this.freeBlockValue)){
                                    if(debug){System.out.println("                 Neighbor become "+itter);}
                                    map[neighbor.x][neighbor.y]++;
                                    closedList.add(neighbor);
                                    openedList.remove(neighbor);
                                }else if(((map[neighbor.x][neighbor.y])!=0)&&((map[neighbor.x][neighbor.y])==this.freeBlockValue)){
                                    if(debug){System.out.println("                 Neighbor become "+itter);}
                                    map[neighbor.x][neighbor.y]=itter;
//                                    closedList.add(neighbor);
//                                    openedList.remove(neighbor);
                                }/*else if(((map[neighbor.x][neighbor.y])!=0)&&((map[neighbor.x][neighbor.y])<itter)){
                                    map[neighbor.x][neighbor.y]++;
                                    closedList.add(neighbor);
                                    openedList.remove(neighbor);
                                }*/
                            }
//                            if(debug){  
//                                //System.out.println(i+". itter, "+j+" column, freeBlocValue.");
//                                System.out.println("        Opened cells: ");this.printListState(openedList);
//                                System.out.println("        Closed cells: ");this.printListState(closedList);
//                                //System.out.println("        Neigburghs cells: ");this.printListState(neigh_X);
//                            }
//                            System.out.println("        "+itter+". itter; Current cell (freBlock): |"+i+","+j+"| (value "+map[i][j]+")");
//                            MatrixImageTools.printMapToConsole(map);
                        }
                    }
//                }
            }
//            System.out.println("     I T E R A T I O N: "+itter);
//
//            
//            String csvFilename=this.outputCSVFilePath.substring(0,outputCSVFilePath.length()-4)+"_itter_"+itter+".csv";
//            String imageFilename=this.outputImageFilePath.substring(0,outputImageFilePath.length()-4)+"_itter_"+itter+".png";
//            
//            MatrixImageTools.printMapToConsole(map);
//            MatrixImageTools.exportMapToCSV(map, csvFilename);
//            MatrixImageTools.exportMatrixToImageFile(map, imageFilename);
        }
        System.out.println("*******************\n E N D  S T A T E: ");
        //MatrixImageTools.printMapState(map, itter);
        MatrixImageTools.printMapToConsole(map);
        MatrixImageTools.exportMapToCSV(map, this.outputCSVFilePath);
        MatrixImageTools.exportMatrixToImageFile(map, this.outputImageFilePath);
    }
//    /**
//     * Printing matrix state.
//     */
//    public void printMapState(int[][] map, int iter){
//
//        System.out.println("****************************");
//        System.out.println(iter+". iteration:");
//        for(int i=0;i<map.length;i++){
//            String line="";
//            for(int j=0;j<map[0].length;j++){
//                line+=map[i][j]+" ";
//            }
//            System.out.println("    Line "+i+": "+line);
//        }
//        System.out.println("****************************");
//    }
    /**
     * Printing matrix state.
     */
    public void printListState(List<Cell> list){

        //System.out.println("    ******** L I S T *******");
        //System.out.println(iter+". iteration:");
//        int list_x=0, list_y=0;
//        for(Cell cell:list){
//            if(cell.x>list_x)
//                list_x=cell.x;
//            if(cell.y>list_y)
//                list_y=cell.y;
//        }
//        
//        for(int i=0;i<list_x;i++){
//            String line="";
//            for(int j=0;j<list_y;j++){
//                line+=list.gmap[i][j]+" ";
//            }
//            System.out.println("    Line "+i+": "+line);
//        }
        
        for(Cell cell:list){
            System.out.print("|"+cell.x+","+cell.y);
        }
        System.out.print("|\n");
        //System.out.println("    * * * * * * * * * * * *");
    }
    /**
     * Looping trought matrix and createnig 
     * @param map
     * @return 
     */
    private void populateOpenedAndClosedList(int[][] map, List<Cell> openedList, List<Cell> closedList){
        //System.out.println("list_x: "+map[0].length+" List_y: "+map.length);
        //System.out.println("Map : "+map[6][5]);
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                //System.out.println("Map: "+i+", "+j+", value= "+map[i][j]+" freeBlockVal="+freeBlockValue);
                if(map[i][j]==freeBlockValue){
                    //System.out.println("Adding to opeblocks: "+i+", "+j+", value= "+map[i][j]+" freeBlockVal="+freeBlockValue);
                    Cell openedCell=new Cell(i,j);
                    openedList.add(openedCell);
                }
                else{
                    Cell closedCell=new Cell(i,j);
                    closedList.add(closedCell);
                }
            }
        }
    }
    /**
     * Determine is it entered matrix in edge
     * @return 
     */
    private boolean isInBorder(int x, int[][] map){
        
        
        return false;
    }
    /** 
     * Serching for neighbors of column elements that are not zero.
     * It doesnt matter if cell is on edge or similar...
     */
    private void findNeighborsForColumnFreeElments(List<Cell> listToAddNeighb, int[][] map, int x){
        for(int j=0;j<map[0].length;j++){
            if(map[x][j]==freeBlockValue){
                findCellNeighbors(listToAddNeighb, map, x, j);
            }
        }
    }
    /**
     * Searching arround given cell (x, y), and founded neighbors add to 
     * list in argument. It is not important if the cell is on the edge; if 
     * doesnt exist it just wont be added.
     * 
     * Neighbors of X are signed as folows:
     *             "a", "b", "c"
     *             "d", "X", "e"
     *             "f", "g", "h"
     * @param listToAddNeighb
     * @param map
     * @param x
     * @param y 
     */
    private void findCellNeighbors(List<Cell> listToAddNeighb, int[][] map, int x, int y){
        //Cell a = null,b = null,c = null,d = null,e = null,f = null,g = null,h=null;
        //a
        if(((x-1)>=0)&&((y-1)>=0)){
//            if(map[x-1][y-1]==this.freeBlockValue){
                Cell a=new Cell((x-1),(y-1));
                listToAddNeighb.add(a);
//            }
        }
        //b
        if(((y-1)>=0)){
//            if(map[x][y-1]==this.freeBlockValue){
                Cell b=new Cell((x),(y-1));
                listToAddNeighb.add(b);
//            }
        }
        //c
        if(((x+1)<=(map.length-1)) && ((y-1)>=0)){
//            if(map[x+1][y-1]==this.freeBlockValue){
                Cell c=new Cell((x+1),(y-1));
                listToAddNeighb.add(c);
//            }
        }
        //d
        if(((x-1)>=0)){
//            if(map[x-1][y]==this.freeBlockValue){
                Cell d=new Cell((x-1),(y));
                listToAddNeighb.add(d);
//            }
        }
        //e
        if(((x+1)<=(map.length-1))){
//            if(map[x+1][y]==this.freeBlockValue){
                Cell e=new Cell((x+1),(y));
                listToAddNeighb.add(e);
//            }
        }
        //f
        if(((x-1)>=0) && ((y+1)<=(map[0].length-1))){
//            if(map[x-1][y+1]==this.freeBlockValue){
                Cell f=new Cell((x-1),(y+1));
                listToAddNeighb.add(f);
//            }
        }
        //g
        if((y+1)<=(map[0].length-1)){
//            if(map[x][y+1]==this.freeBlockValue){
                Cell g=new Cell((x),(y+1));
                listToAddNeighb.add(g);
//            }
        }
        //h
        if(((x+1)<=(map.length-1))&&(y+1)<=(map[0].length-1)){
//            if(map[x+1][y+1]==this.freeBlockValue){
                Cell h=new Cell((x+1),(y+1));
                listToAddNeighb.add(h);
//            }
        }
    }
    /**
     * Add column at given index to the list.
     * @param robot_x
     * @param robot_y
     * @return 
     */
    private void addColumnToList(List<Cell> listToAdd, int[][] map, int x){
        for(int j=0;j<map[x].length;j++){
            Cell cellToAdd=new Cell(x,j);
            listToAdd.add(cellToAdd);
        }
    }
    /**
     * Remove column at given index from the list.
     * @param robot_x
     * @param robot_y
     * @return 
     */
    private void removeColumnFromList(List<Cell> listToRemoveFrom, int[][] map, int x){
        for(Cell cell:listToRemoveFrom){
            if(cell.x==x){
                listToRemoveFrom.remove(cell);
            }
        }
    }
    /************************************************/
    /****************Getters, Setters****************/
    /************************************************/
    
    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int getFreeBlockValue() {
        return freeBlockValue;
    }

    public void setFreeBlockValue(int freeBlockValue) {
        this.freeBlockValue = freeBlockValue;
    }
}
