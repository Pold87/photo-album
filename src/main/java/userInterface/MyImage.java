package main.java.userInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;

/**
 * Created by pold on 12/11/14.
 */
public class MyImage extends JComponent {

    private BufferedImage img; // The actual picture
    private int x, y; // 2D-coordinates
    private int num; // The number of the image (for selecting it)
    private int rotationDegrees; // Degress of rotation for image.

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


    public MyImage(BufferedImage img, int x, int y, int num, int rotationDegrees) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.num = num;
        this.rotationDegrees = rotationDegrees;
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


    public MyImage getRotatedImage(double degrees) {

        ImageIcon icon = new ImageIcon(this.img);

        int newH = (int) (Math.sin(Math.toRadians(degrees)) * img.getWidth() +
                Math.cos(Math.toRadians(degrees) * img.getHeight()));

        int newW = (int) (Math.sin(Math.toRadians(degrees)) * img.getHeight() +
                Math.cos(Math.toRadians(degrees) * img.getWidth()));

        BufferedImage rotated = new BufferedImage(newH, newW, BufferedImage.TYPE_INT_RGB);


        int w = img.getWidth();
        int h = img.getHeight();
        AffineTransform xform = AffineTransform.getRotateInstance(Math.toRadians(degrees), w / 2, h / 2);


        Graphics2D g2 = rotated.createGraphics();
        g2.drawImage(img, xform, null);

        MyImage rotatedImage = new MyImage(rotated, this.img.getHeight(), this.img.getWidth(), this.num, this.rotationDegrees);
        return rotatedImage;

    }

}
