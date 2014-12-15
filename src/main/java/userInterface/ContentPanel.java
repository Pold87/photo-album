package main.java.userInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pold on 12/10/14.
 */
public class ContentPanel extends JPanel {
    private ArrayList<MyImage> imageList;
    private Integer oldMouseX, oldMouseY;
    private String whichButton;

    public ContentPanel() throws IOException {
        imageList = new ArrayList<MyImage>();
        whichButton = "none"; //at the beginning no button is selected
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
    }

    public ArrayList<MyImage> getImageList() { return imageList; }

    public String getWhichButton() {
        return whichButton;
    }

    public void setWhichButton(String whichButton) {
        this.whichButton = whichButton;
    }

    public void setImage(MyImage image) {
        if (image.isActive()) {
            this.imageList.add(image);
        }
        else {
            this.imageList.remove(image);
        }
    }

    // For mouse click events (to select and unselect pictures)
    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            super.mouseClicked(mouseEvent);

            // Get current mouse position.
            Point clickPoint = mouseEvent.getPoint();

            // For each image in the image list, get its area and determine if the mouse click occurred in this area.
            boolean noOverlap = true; //to ensure that only the topmost picture can be selected if mouse is clicked in area of two pictures
            Collections.reverse(getImageList());
            for (MyImage i : imageList) {
                    while (noOverlap == true){
                    Rectangle pictureArea = new Rectangle(i.getX(), i.getY(), i.getImg().getWidth(), i.getImg().getHeight());
                    if (noOverlap && pictureArea.contains(clickPoint) && getWhichButton().equals("rotate") && i.isSelected()) {
                        try {
                            i.getRotatedImage(90);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Repaint everything in order to see changes.
                        noOverlap = false;
                        repaint();
                    }
                    else if (noOverlap && pictureArea.contains(clickPoint) && (getWhichButton().equals("select") || getWhichButton().equals("none"))){
                        // Toggle activity status.
                        i.setSelected(!i.isSelected());
                        noOverlap = false;
                        // Repaint everything in order to see changes.
                        repaint();
                    }
                }
            }
            //undo reverse for future uses of imageList
            Collections.reverse(getImageList());
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
