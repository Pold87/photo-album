package main.java.userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class MyImage {

    private BufferedImage img, originalImage; // The actual picture
    private int x;
    private int y;

    private int width, originalWidth;
    private int height, originalHeight; // 2D-coordinates
    private int num; // The number of the image (for selecting it)
    private double rotationDegrees; // Degrees of rotation for image.
    private boolean active; //indicates whether image is being displayed on contentPanel
    private boolean selected; //indicates whether image is selected in contentPanel

    public MyImage(String path, int x, int y, int num) throws IOException {
        // Create URL for image (to handle OS difficulties)
        URL url = getClass().getResource(path);
        System.out.println("Creating image: " + url);
        this.img = ImageIO.read(url);
        this.x = x;
        this.y = y;
        this.num = num;
        this.active = false;
        this.selected = false;
        this.rotationDegrees=0;
        setDisplaySize();
        width = img.getWidth();
        height = img.getHeight();
        originalWidth = img.getWidth();
        originalHeight = img.getHeight();
        originalImage = img;
    }

    public boolean isActive() {
        return active;
    }
    public boolean isSelected() {return selected;}

    public void setActive(boolean active) {
        this.active = active;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private void setDisplaySize() {
        int sideLength = 400;
        double width = this.img.getWidth();
        double height = this.img.getHeight();
        double h;
        double w;
        //horizontal image
        if (width>height) {
            w= sideLength;
            h = (w/width)*height;
        }
        //vertical image
        else if (height>width){
            h=sideLength;
            w = (h/height)*width;
        }
        //square image
        else{
            h=sideLength;
            w=sideLength;
        }
        this.img = getScaledImage((int) w, (int) h);
    }

    public BufferedImage getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getNum() {
        return num;
    }

    public void resizeImg(int newW, int newH) {

        width = newW;
        height = newH;

//
//        double dW = (double) newW;
//        double dH = (double) newH;
//
//        BufferedImage before;
//        before = this.img;
//        double w = before.getWidth();
//        double h = before.getHeight();
//
//
//        double dWNew = (double) newW;
//        double dHNew = (double) newH;
//
//
//        System.out.println("New stuff" + dWNew + " " + dHNew);
//
//        double scaleFactorW = dWNew / w;
//        double scaleFactorH = dHNew / h;
//
//        System.out.println("Scale factor:" + scaleFactorW + " " + scaleFactorH);
//
//        BufferedImage after = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
//        AffineTransform at = new AffineTransform();
//        at.scale(scaleFactorW, scaleFactorH);
//        AffineTransformOp scaleOp =
//                new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
//        after = scaleOp.filter(before, after);
//
//
////        BufferedImage after = this.getScaledImage(newW, newH);
//
//        this.img = after;
//
//        this.width = newW;
//        this.height = newH;

    }

    //Resizes an image to have the given width and height
    public BufferedImage getScaledImage(int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(this.img, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public void incrementRotation(double degree){
        rotationDegrees += degree;
    }

    public double getRotationDegrees() {
        return this.rotationDegrees;
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        g2dCopy.rotate(Math.toRadians(rotationDegrees), x+ (width/2), y+(height/2));
        g2dCopy.drawImage(img, x, y, width, height, null);
        g2dCopy.drawString(Integer.toString(this.num + 10), x, y - 5);

        if(selected){
            //draw blue frame around image if it is now selected
            double thickness = 3;
            Stroke oldStroke = g2d.getStroke();
            g2dCopy.setStroke(new BasicStroke((float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2dCopy.setPaint(Color.blue);
            g2dCopy.drawRect(x, y, width, height);
            g2dCopy.setStroke(oldStroke);

        }
        g2dCopy.setTransform(new AffineTransform());
        g2dCopy.dispose();
    }

    public boolean contains(Point p){
    	Rectangle rectangle = new Rectangle(x, y, width, height);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(rotationDegrees), x+ (width/2), y+(height/2));
		Shape pictureArea = transform.createTransformedShape(rectangle);
		boolean contains = pictureArea.contains(p);
		return contains;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
    
    public void snapRotation(){
    	int rot = (int) rotationDegrees % 360;
    	int x = rot/45;
    	int y = rot%45;
    	if(y > 22)
    		x++;
    	rotationDegrees = x*45;
    }

    /**
     * No longer used
     */
    public void resetToOriginalImage(){
    	x = 0;
    	y = 0;
        img = originalImage;
        height = originalHeight;
        width = originalWidth;
        rotationDegrees =  0;
    }
}