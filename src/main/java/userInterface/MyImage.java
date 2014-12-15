package main.java.userInterface;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
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
public class MyImage extends JComponent {

    private BufferedImage img; // The actual picture
    private int x, y; // 2D-coordinates
    private int num; // The number of the image (for selecting it)
    private int rotationDegrees; // Degrees of rotation for image.

    public boolean isActive() {
        return active;
    }
    public boolean isSelected() {return selected;}

    public void setActive(boolean active) {
        this.active = active;
    }
    public void setSelected(boolean selected) { this.selected = selected; }

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
        setBackground(Color.black);
        setDisplaySize();
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


    public void getRotatedImage(double degree) throws IOException {
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

        /*BufferedImage rotated = new BufferedImage(newH, newW, BufferedImage.TYPE_INT_RGB);

        AffineTransform xform = AffineTransform.getRotateInstance(Math.toRadians(degree), w / 2, h / 2);


        Graphics2D g2 = rotated.createGraphics();
        g2.drawImage(img, xform, null);


        return rotatedImage;*/

}
