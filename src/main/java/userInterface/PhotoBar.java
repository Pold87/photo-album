package main.java.userInterface;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class PhotoBar extends JToolBar {

	private static final long serialVersionUID = 1L;
	private String[] imageNames;
    private ActionListener listener;
    private MyImage [] photos;
    private JButton [] allButtons;
    private int nrOfImages;

    public PhotoBar(String[] images, OurController listener) throws Exception {
    	this.listener = listener;
        setOrientation(SwingConstants.VERTICAL);
        setBackground(Color.white);
        this.imageNames = images;
        nrOfImages = this.imageNames.length;
        this.setFloatable(false);
    }

    //Why is the loading done here? 
    // loads images and returns array of photos, so that photo fields can be accessed easily
    public MyImage[] loadImages(String path, int buttonWidth) throws Exception {
        photos = new MyImage[nrOfImages];
        allButtons = new JButton[nrOfImages];
        int x = 20;
        int y = 20;
        for (int i = 0; i < this.imageNames.length; i++) {
            String pic = this.imageNames[i];
            String imgPath = path + pic;
            System.out.println(imgPath);
            if(pic != null){
            	MyImage photo = new MyImage(imgPath, x, y, i);
            	photos[i] = photo;
            	JButton thumbButton = new JButton(new ImageIcon(photo.getScaledImage(buttonWidth, buttonWidth)));
            	thumbButton.setActionCommand(i + "");
                thumbButton.setText(Integer.toString(photo.getNum()));
            	this.add(thumbButton);
            	thumbButton.addActionListener(listener);
            	x = x+20;
            	y = y+20;
            	allButtons[i] = thumbButton;
            }
        }
        return photos;
    }
    
    public void removeButton(MyImage image){
        SwingUtilities.invokeLater(() -> {
            for(int i = 0; i < nrOfImages; i++){
                if(photos[i].equals(image)){
                    PhotoBar.this.remove(allButtons[i]);
                }
            }
            this.revalidate();
            this.repaint();
        });
    }


    public void addButton(MyImage image){

        SwingUtilities.invokeLater(() -> {
            for(int i = 0; i < nrOfImages; i++){
                if(photos[i].equals(image)){
                    PhotoBar.this.add(allButtons[i]);
                }
            }
            this.revalidate();
            this.repaint();
        });

    }
}
