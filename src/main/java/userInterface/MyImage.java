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

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active; //

    public MyImage(String path, int x, int y, int num) throws IOException {


//        URL url1 = getClass().getResource("/");
//        System.out.println(url1);

        // Create URL for image (to handle OS difficulties)
        URL url = getClass().getResource(path);
        System.out.println(url);
        this.img = ImageIO.read(url);
        this.x = x;
        this.y = y;
        this.num = num;
        this.active = true;
        setBackground(Color.black);
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


    public MyImage getRotatedImage(double degrees) {

        ImageIcon icon = new ImageIcon(this.img);
        BufferedImage blankCanvas = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);


        double locationX = img.getWidth() / 2;
        double locationY = img.getHeight() / 2;
//        AffineTransform tx = AffineTransform.getRotateInstance(degrees, locationX, locationY);

        int newH = (int) (Math.sin(Math.toRadians(degrees)) * img.getWidth() +
                Math.cos(Math.toRadians(degrees) * img.getHeight()));

        int newW = (int) (Math.sin(Math.toRadians(degrees)) * img.getHeight() +
                Math.cos(Math.toRadians(degrees) * img.getWidth()));


        BufferedImage rotated = new BufferedImage(newH, newW, BufferedImage.TYPE_INT_RGB);

//        AffineTransform tx = new AffineTransform();
//        tx.translate(0.5 * img.getHeight(), 0.5 * img.getWidth());
//        tx.rotate(Math.toRadians(degrees));
//        tx.translate(-0.5 * img.getWidth(), -0.5 * img.getHeight());


        int w = img.getWidth();
        int h = img.getHeight();
        AffineTransform xform = AffineTransform.getRotateInstance(Math.toRadians(degrees), w / 2, h / 2);


        Graphics2D g2 = rotated.createGraphics();
        g2.drawImage(img, xform, null);

        MyImage rotatedImage = new MyImage(rotated, this.img.getHeight(), this.img.getWidth(), this.num, this.rotationDegrees);
        return rotatedImage;

//        g2.dispose();

//        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

//        Graphics2D g2 = blankCanvas.createGraphics();
//        g2.rotate(Math.toRadians(degrees), icon.getIconWidth() / 2, icon.getIconHeight() / 2);
//        g2.drawImage(this.img, 0, 0, null);
//        this.img = blankCanvas;

//        g2.drawImage(op.filter(img, null), x, y, null);
//        repaint();

    }

}
