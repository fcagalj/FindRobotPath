package PathPlan;

import java.awt.*;
import java.util.*;
import java.awt.image.*;

// Class for loading maps from images.

public class MapFile {

    // graphic for the map
    public Image mapImage = null;

    // map size
    public int width, height;

    // extract grayscale data from an image
    public float[][] convertImage(Image im, float subsample, float floor) {
	
	// Image size
	int w = im.getWidth(null);
	int h = im.getHeight(null);

	// Scaled size
	int scw = (int) (w / subsample);
	int sch = (int) (h / subsample);

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


    public float[][] convertImage(float subsample) {
	return convertImage(mapImage, subsample, 0.9f);
    }


    // convert a 2d float array (as from convertImage) to a 1d double
    // array of costs by stacking rows and scaling
    public static double[] stack(float[][] im, double scale) {
	if (im.length == 0)
	    return new double[0];
	double [] res = new double[im.length * im[0].length];
	for (int i = 0; i < im.length; i++)
	    for (int j = 0; j < im[0].length; j++)
		res[j + im[0].length * i] = (1.0 - im[i][j]) * scale;
	return res;
    }


    // load a map from a file -- use convertImage to get the
    // data as an array of doubles, draw to display
    public void loadMap(String file) {
	if (file == null)
	    throw new RuntimeException("loadMap: null filename");
	Toolkit tk = Toolkit.getDefaultToolkit();
	mapImage = tk.createImage(file);
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
	width = mapImage.getWidth(null);
	height = mapImage.getHeight(null);
    }


    public void draw(Graphics g, int x, int y, int w, int h) {
        if (mapImage != null)
            g.drawImage(mapImage, x, y, w, h, Color.black, null);
    }


    public MapFile(String file) {
	loadMap(file);
    }
}
