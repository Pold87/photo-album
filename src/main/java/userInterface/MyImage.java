package main.java.userInterface;

import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.awt.GraphicsConfiguration;
import java.net.URL;

/**
 * Created by pold on 12/11/14.
 */
public class MyImage {

    private BufferedImage img; // The actual picture
    private int x, y, width, height; // 2D-coordinates
    private int num; // The number of the image (for selecting it)
    private int rotationDegrees; // Degrees of rotation for image.
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

    public void setImg(BufferedImage img) {
        this.img = img;
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

    public void setNum(int num) {
        this.num = num;
    }

    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(), img.getHeight());
    }

    //Resizes an image to have the given width and height
    public BufferedImage getScaledImage(int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(this.img, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public void incrementRotation(int degree){
    	rotationDegrees += degree;   	
    }
    
    public void setRatation(int degree){
    	rotationDegrees = degree;
    }

    //Deprecated?
    private void getRotatedImage(double degree) throws IOException {
        double angle = Math.toRadians(degree);
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int w = this.getImg().getWidth();
        int h = this.getImg().getHeight();
        int newW = (int) Math.floor(w * cos + h * sin);
        int newH = (int) Math.floor(h * cos + w * sin);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(newW, newH, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((newW - w) / 2, (newH - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(this.img, null);
        g.dispose();
        this.setImg(result);
        this.rotationDegrees = (int) (this.rotationDegrees + degree);
    }
    
    public void paint(Graphics g){
    	System.out.println("Drawing picture: " + num);
    	Graphics2D g2d = (Graphics2D) g;
    	System.out.print("X: "+ x +" Y: "+y );
    	//g2d.translate(0, 0);
    	g2d.rotate(Math.toRadians(rotationDegrees), x+ (width/2), y+(height/2));
    	g2d.drawImage(img, x, y, null);
    	System.out.println(" X: "+ x +" Y: "+y );
    	
    	if(selected){
                //draw blue frame around image if it is now selected
                double thickness = 3;
                Stroke oldStroke = g2d.getStroke();
                g2d.setStroke(new BasicStroke((float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setPaint(Color.blue);
                g2d.drawRect(x, y, width, height);
                g2d.setStroke(oldStroke);
    	}
    	g2d.setTransform(new AffineTransform());
    }

    
    public boolean contains(Point p){
    	
    	
    	return false;
    }

}
