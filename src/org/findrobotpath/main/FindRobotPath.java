/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.findrobotpath.main;

import org.findrobotpath.mapMatrix.BlushfireMapMatrix;
import org.findrobotpath.findPath.Blushfire;
import org.findrobotpath.findPath.BlushfireNew;

/**
 *
 * @author frane
 */
public class FindRobotPath {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        //String inputImagePath="intel-lab_b.png";
        String inputImagePath="1.png";
        String outpuImagePath="_output//outputImage.png";
        String outpuCSVPath="_output//outputCSV.csv";

        MatrixImage mapFile=new MatrixImage(inputImagePath, outpuImagePath, outpuCSVPath);
        

        
        int[][] map=mapFile.convertImage(1);
        
//        MatrixImageTools.printMapToConsole(map);
        
        BlushfireMapMatrix bfm=new BlushfireMapMatrix(map,"_output//mappedImage.png","_output//mappedCSV.csv");
        
//        BlushfireMapMatrix bfm=new BlushfireMapMatrix("_output//mappedImage.png","_output//mappedCSV.csv");
//        
//        Blushfire path=new Blushfire(map);
//        path.wavefrontSearch();
//        BlushfireNew bf=new BlushfireNew();
        
    }
    
}
