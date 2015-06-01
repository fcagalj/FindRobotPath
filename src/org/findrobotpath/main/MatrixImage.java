package org.findrobotpath.main;

import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Class MatrixImage is used for converting image in array marix. Class use string
 input as constructor argument or trought loadMap(String path). Proces of 
 converting image is called trought int[][] convertImageORIG() method.
 Class also have methods for printing maps.
 * @author frane
 */

public class MatrixImage {

    private int map[][];
    private int map_x;
    private int map_y;
    
    // graphic for the map
    private Image mapImage;// = ImageIO.read(imageFile);
    private BufferedImage buffImage;
    
    private File inputImageFile;
    private File outputImageFile;
    private File outputCSVFile;
    // map size
    public int origImgWidth, origImgHeight;
    
    //Property used internally for converting image to/from array matrix
    private SampleModel sampleModel;

    public MatrixImage(String inputImagePath, String outputImagePath, String outputCSVPath) {
	loadMap(inputImagePath);
        this.outputImageFile=new File(outputImagePath);
        this.outputCSVFile=new File(outputCSVPath);
    }
    /**
     * Load private inputFile from entered String path, and set
 image info variables:
      int origImgWidth
      int origImgHeight
      Image mapImage
      File inputImageFile
     * @param inputImagePath 
     */
    public void loadMap(String inputImagePath) {
	if (inputImagePath == null)
	    throw new RuntimeException("loadMap: null filename");
        inputImageFile=new File(inputImagePath);
	Toolkit tk = Toolkit.getDefaultToolkit();
	mapImage = tk.createImage(inputImagePath);
	int tries = 0;
	while (!tk.prepareImage(mapImage, -1, -1, null)) {
	    if (tries >= 20)
		throw new RuntimeException("map not ready after " +
					   tries + " tries");
	    tries++;
	    try {
		Thread.sleep(100);
	    } catch (Exception e) {
		System.out.println("loadMap: " + e);
		System.exit(-1);
	    }
	}
	origImgWidth = mapImage.getWidth(null);
	origImgHeight = mapImage.getHeight(null);
        
    }

    
    public int[][] convertImage(float subsample) {
	
        return convertImage(mapImage, subsample, 0.9f);
        //return convertImageByte(mapImage, subsample, 0.9f);
    }
    /**
     * Convert Image to array matrix and save matrix to private array 
     * int[][] map. First scale image by subsample prop.
     * Also saves output matrx to outputCSV and outptImage file.
     * @param im
     * @param subsample
     * @param floor
     * @return 
     */
    private int[][] convertImage(Image im, float subsample, float floor)
    {
        // Image size
	int w = im.getWidth(null);
	int h = im.getHeight(null);

	// Scaled size
	int scw = (int) (w / subsample);
	int sch = (int) (h / subsample);
        
        map_x=scw;
        map_y=sch;
        
//	// Draw the image into a grayscale buffer
//	BufferedImage bufGraySc = 
//	    new BufferedImage(scw, sch, BufferedImage.TYPE_USHORT_GRAY);//TYPE_USHORT_GRAY
//	Graphics g = bufGraySc.getGraphics();
//	g.drawImage(im, 0, 0, scw, sch, Color.black, null);
        // Brighten image before applay B/W
        // Draw the image into a black/white buffer
        BufferedImage bufBW = 
           new BufferedImage(scw, sch, BufferedImage.TYPE_BYTE_BINARY); //TYPE_BYTE_INDEXED
        //this.buffImage=bufBW;
        //   BufferedImage bufBW = convertToBW(scw, sch, this.inputImageFile.getPath());
//        Graphics2D g2d = bufBW.createGraphics();
//        g2d.drawImage(im, 0, 0, scw, sch, null);
	Graphics g2 = bufBW.getGraphics();
	g2.drawImage(im, 0, 0, scw, sch, Color.black, null);
        //BufferedImage bufFinal = brighten(bufBW,7);
        //g2d.dispose();


        ///////////////////////////////////////
        try 
        {
            Raster raster=bufBW.getData();
//            int w=raster.getWidth();
//            int h=raster.getHeight();
            int[][] res=new int[scw][sch];
            for (int x=0;x<scw;x++)
            {
                for(int y=0;y<sch;y++)
                {
                    sampleModel = raster.getSampleModel();
                    res[x][y]=raster.getSample(x,y,0);
                    //res[x][y]=bufBW.getRGB(x,y);
                }
            }
            //exportMatrixToImageFile(res, "outputImageFileEmbed.png");
            MatrixImageTools.exportMatrixToImageFile(res, "outputImage.png");
            MatrixImageTools.exportMapToCSV(res, "outputCSVFile.csv");
            
            map=res;
            return res;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Convert array back to Image, and export image to output file.
     */
    public Image exportMatrixToImageFile(int pixels[][], String oututFilePath)
    {
        
         outputImageFile=new File(oututFilePath);
         int w=pixels.length;
         int h=pixels[0].length;
         WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
         //WritableRaster raster=(WritableRaster)image.getData();
         for(int i=0;i<w;i++)
         {
             for(int j=0;j<h;j++)
             {
                 raster.setSample(i,j,0,pixels[i][j]);
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
        
        return image;
    }
    /**
     * Export image to file.
     * @param image
     * @param oututFilePath 
     */
    public void exportImageToFile(Image image, String oututFilePath)
    {
         outputImageFile=new File(oututFilePath);
        if(!(image instanceof BufferedImage)){
            System.out.println("Unrecognized image "+image.toString()+"to export!");
            return;
        }
        BufferedImage bi= (BufferedImage) image;
         try {
            ImageIO.write(bi,"png",outputImageFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**************************************************************************/
    /**************************************************************************/
    /*****Bellow is code used during development, not currently  used**********/
    /**************************************************************************/
    /**************************************************************************/
    
    
    /**
     * Convert image to matrix array. Used during development, not used now!.
     * @param im
     * @param subsample
     * @param floor
     * @return 
     */
    @Deprecated
    private int[][] convertImageByte(Image im, float subsample, float floor)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
        BufferedImage img = null;
        int map_x = 0;
        int map_y = 0;
        String base64String=null;
        try {
            img = ImageIO.read(inputImageFile);
            ImageIO.write(img, "png", baos);
            baos.flush();
            map_x=img.getWidth();
            map_y=img.getHeight();
            base64String=Base64.encode(baos.toByteArray());
            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(MatrixImage.class.getName()).log(Level.SEVERE, null, ex);
        }


        byte[] bytearray = Base64.decode(base64String);

        BufferedImage imag = null;
        try {
            imag = ImageIO.read(new ByteArrayInputStream(bytearray));
            ImageIO.write(imag, "png", new File("snapj.png"));
        } catch (IOException ex) {
            Logger.getLogger(MatrixImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        map=new int[map_x][map_y];
        int count=0;
        for(int i=0;i<map_x;i++){
            for(int j=0;j<map_y;j++){
                if(count<bytearray.length)
                {
                    //System.out.println(bytearray.length+" sized, first value: "+bytearray[0]+" count: "+count);
                    map[i][j]=bytearray[count];
                    count++;
                }else{
                    break;
                }
            }
        }
        
        
        return map;
    }
    
    /**
     * Convert image to matrix array. Used during development, not used now!.
     * // extract grayscale data from an image
     * @param im
     * @param subsample
     * @param floor
     * @return 
     */
    /*
Try using the Raster origImgWidth and origImgHeight variables instead of the BufferedImage origImgWidth and origImgHeight variable. Also use Raster.getMinX() and Raster.getMinY()

Every value in the float array isn't a pixel value. Every value is a color component value. So a 2x1 image would actually need to be of length 4, as you have ARGB color components. To make it a 2x1 image red for example, would require something like...

int numColorComponents = 4;
float[] data = new float[imgWidth*imgHeight*numColorComponents];
raster.setPixels(minX,minY, rasterWidth,rasterHeight, data);

Also, unlike other graphics frameworks, the float buffer here isn't a buffer of normalized values. Its value between [0, 255]. So, to set 2x1 image to opaque red, your buffer would be:

float alpha = 255;
float red = 255;   
float[] buffer = new float[]{alpha,red,0,0,alpha,red,0,0};
    
    */
    @Deprecated
    private float[][] convertImageORIG(Image im, float subsample, float floor) {
	
	// Image size
	int w = im.getWidth(null);
	int h = im.getHeight(null);

	// Scaled size
	int scw = (int) (w / subsample);
	int sch = (int) (h / subsample);
        
        map_x=scw;
        map_y=sch;
        
	// Draw the image into a grayscale buffer
	BufferedImage buf = 
	    new BufferedImage(scw, sch, BufferedImage.TYPE_USHORT_GRAY);
	Graphics g = buf.getGraphics();
	g.drawImage(im, 0, 0, scw, sch, Color.black, null);

	// extract the pixels and scale so a grey level of floor is
	// fully occupied
	float[][] res = new float[sch][scw];
	Raster rast = buf.getData();
	for (int i = 0; i < sch; i++) {
	    res[i] = rast.getPixels(0, i, scw, 1, res[i]);
	    for (int j = 0; j < scw; j++) {
		res[i][j] *= .0000152590; // 1/65535
		res[i][j] = (res[i][j] - floor) / (1 - floor);
		if (res[i][j] < 0) res[i][j] = 0;
	    }
	}
	return res;
    }
    /**
     * Createing image from float array. Not used!
     * @param data
     * @param w
     * @param h
     * @return 
     */
    @Deprecated
    private BufferedImage getImageFromFloatArray(float[] data, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        System.out.println("Image pixel array size: "
                        + ((DataBufferInt) img.getRaster().getDataBuffer())
                                .getData().length);
        System.out.println("Datasize: " + data.length);
        WritableRaster raster = img.getRaster();
        raster.setPixels(0, 0, w, h, data);
        return img;
    }
    /**
     * Not used!
     * convert a 2d float array (as from convertImageORIG) to a 1d 
     * double array of costs by stacking rows and scaling
     */
    @Deprecated
    public static double[] stack(float[][] im, double scale) {
	if (im.length == 0)
	    return new double[0];
	double [] res = new double[im.length * im[0].length];
	for (int i = 0; i < im.length; i++)
	    for (int j = 0; j < im[0].length; j++)
		res[j + im[0].length * i] = (1.0 - im[i][j]) * scale;
	return res;
    }


    /**
     * Not used
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h 
     */
    private void draw(Graphics g, int x, int y, int w, int h) {
        if (mapImage != null)
            g.drawImage(mapImage, x, y, w, h, Color.black, null);
    }

     /**
      * NOT USED!!!
      * Returns the supplied src image brightened by a float value from 0 to 10.
      * Float values below 1.0f actually darken the source image.
      */
    public static BufferedImage brighten(BufferedImage src, float level) {
        BufferedImage dst = new BufferedImage(
                src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        float[] scales = {level, level, level};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);

        Graphics2D g = dst.createGraphics();
        g.drawImage(src, rop, 0, 0);
        g.dispose();

        return dst;
    }
   public static BufferedImage convertToBW(int width, int height, String imageToCenvertPath) 
    {
        BufferedImage original=null;
        BufferedImage binarized=null;
        try 
        {
         original = ImageIO.read(new File(imageToCenvertPath));
         binarized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

         int red;
         int newPixel;
         int threshold =230;

            for(int i=0; i<original.getWidth(); i++) 
            {
                for(int j=0; j<original.getHeight(); j++)
                {

                    // Get pixels
                  red = new Color(original.getRGB(i, j)).getRed();

                  int alpha = new Color(original.getRGB(i, j)).getAlpha();

                  if(red > threshold)
                    {
                        newPixel = 0;
                    }
                    else
                    {
                        newPixel = 255;
                    }
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    binarized.setRGB(i, j, newPixel);

                }
            } 
            //ImageIO.write(binarized, "png",new File("blackwhiteimage") );
         }
        catch (IOException e) 
        {
                e.printStackTrace();
        }    
        return binarized;
    }

     private static int colorToRGB(int alpha, int red, int green, int blue) {
            int newPixel = 0;
            newPixel += alpha;
            newPixel = newPixel << 8;
            newPixel += red; newPixel = newPixel << 8;
            newPixel += green; newPixel = newPixel << 8;
            newPixel += blue;

            return newPixel;
        }
    public int[][] getMap() {
        return map;
    }

    public BufferedImage getBuffImage() {
        return buffImage;
    }

}
