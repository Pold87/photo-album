package main.java.userInterface;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pold on 12/10/14.
 */
public class ContentPanel extends JPanel {

    private final ArrayList<MyImage> imageList;
    private Integer oldMouseX, oldMouseY;

    public ContentPanel(ArrayList<MyImage> imageList) throws IOException {
        this.imageList = imageList;
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        for (MyImage image:imageList){
            if(image.isActive()){
                this.add(image);
            }
        }
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
    }

    public ArrayList<MyImage> getImageList() { return imageList; }

    public void setImage(MyImage image) {
        for (MyImage i : this.imageList)
            if (i.getNum() == image.getNum())
                i = image;
    }


    // For mouse click events (to select and unselect pictures)
    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            super.mouseClicked(mouseEvent);

            // Get current mouse position.
            Point clickPoint = mouseEvent.getPoint();

            // For each image in the image list, get its area and determine if the mouse click occurred in this area.
            for (MyImage i : imageList) {
                //MyImage rotatedImage = i.getRotatedImage(1);
                //i.setImg(rotatedImage.getImg());
                Rectangle pictureArea = new Rectangle(i.getX(), i.getY(), i.getImg().getWidth(), i.getImg().getHeight());
                if (pictureArea.contains(clickPoint)) {
                    // Toggle activity status.
                    i.setSelected(!i.isSelected());
                }
            }
            // Repaint everything in order to see changes.
            repaint();
        }
    }

    class MyMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent){

            // Set mouse position, if there is no old Mouse position.
            if (oldMouseX == null | oldMouseY == null) {
                oldMouseX = mouseEvent.getX();
                oldMouseY = mouseEvent.getY();
            } else {
                // Get current mouse position
                int mouseX = mouseEvent.getX();
                int mouseY = mouseEvent.getY();

                // Get difference between old mouse position and current position
                Integer diffX = mouseX - oldMouseX;
                Integer diffY = mouseY - oldMouseY;

                // Update position for every image in the image list.
                for(MyImage i:imageList)
                    if(i.isSelected()){
                        i.setX(i.getX() + diffX);
                        i.setY(i.getY() + diffY);
                    }

                // Set old mouse position to current position.
                oldMouseX = mouseX;
                oldMouseY = mouseY;
            }

            // Repaint everything in order to see changes.
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
        }

    }


    public void paintComponent(Graphics g) {

        // Casting to 2D seems to be beneficial.
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // Draw each image in the image list (if it's active)
        for (MyImage i : imageList) {
            if (i.isActive()) {


                //double rotationRequired = Math.toRadians(45.0);

                //AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, i.getX(), i.getY());
                //AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

//                g2.drawLine(i.getX(), i.getY(), i.getImg().getWidth(), i.getImg().getHeight());
                g2.drawImage(i.getImg(), i.getX(), i.getY(), null);
                if (i.isSelected()){
                    //draw blue frame around image if it is now selected
                    double thickness = 3;
                    Stroke oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke((float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setPaint(Color.blue);
                    g2.drawRect(i.getX(), i.getY(), i.getImg().getWidth(), i.getImg().getHeight());
                    g2.setStroke(oldStroke);
                }
//                g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
//                i.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.black, Color.black));

            }

        }

    }


}
