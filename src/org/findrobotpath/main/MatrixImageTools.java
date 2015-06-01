/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.findrobotpath.main;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Class with some static usefol methods that can be used in program.
 * @author frane
 */
public class MatrixImageTools {
    /**
     * Output map to the console.
     */
    public static void printMapToConsole(int[][] map){
        int calc_x=0;
        // to find real y_size we have to find max y_size          
        for (int[] row : map) {
            if((row.length-1)>calc_x)
                calc_x=row.length;
            }
        
        for(int i=0;i<map.length;i++){
            String line="";
            for(int j=0;j<map[0].length;j++){
                line+=map[i][j]+" ";
            }
            System.out.println("Line "+i+": "+line);
        }
    }
    /**
     * Output matrix state to the console during loop.
     */
    public static void printMapState(int[][] map, int iter){

        System.out.println("****************************");
        System.out.println(iter+". iteration:");
        for(int i=0;i<map.length;i++){
            String line="";
            for(int j=0;j<map[0].length;j++){
                line+=map[i][j]+" ";
            }
            System.out.println("    Line "+i+": "+line);
        }
        System.out.println("****************************");
    }
    /**
     * Output map to the console.
     */
    public static void exportMapToCSV(int[][] map, String outputCSVpath){
        
        try
	{
	    FileWriter writer = new FileWriter(outputCSVpath);
 
            for(int i=0;i<map.length;i++){
                String line="";
                for(int j=0;j<map[0].length;j++){
                    line+=map[i][j]+",";
                    writer.append(String.valueOf(map[i][j]));
                    writer.append(',');
                }
                System.out.println("    Line "+i+": "+line);
                writer.append('\n');
            }
	    //generate whatever data you want
 
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
        
    }
    /**
     * Convert array matrix to Image, and export image to output file
     * on path given in argument oututFilePath.
     */
    public static void exportMatrixToImageFile(int[][] map, String oututFilePath)
    {
        
         File outputImageFile=new File(oututFilePath);
         int w=map.length;
         int h=map[0].length;
         
         
         //SampleModel sampleModel=buffImage.getData().getSampleModel();
         //WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
         //WritableRaster raster=(WritableRaster)image.getData();
         
         MultiPixelPackedSampleModel sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE,w,h,1); // one bit per pixel
         WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
         //TiledImage tiledImage = new TiledImage(0,0,w,h,0,0,sampleModel,null); // Create a TiledImage using the SampleModel.
         
        // WritableRaster raster = tiledImage.getWritableTile(0,0); // Get a raster for the single tile.
         
         
         for(int i=0;i<w;i++)
         {
             for(int j=0;j<h;j++)
             {
                 raster.setSample(i,j,0,map[i][j]);
             }
         }
        BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY);
        image.setData(raster);
        try {
            ImageIO.write(image,"png",outputImageFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
