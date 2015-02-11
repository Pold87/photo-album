package main.java.userInterface;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionListener;

public class PhotoBar extends JToolBar {

	private static final long serialVersionUID = 1L;
	private String[] imageNames;
    private ActionListener listener;
    private MyImage [] photos;

    public PhotoBar(String[] images, OurController listener) throws Exception {
    	this.listener = listener;
        setOrientation(SwingConstants.VERTICAL);
        setBackground(Color.white);
        this.imageNames = images;
    }

    //Why is the loading done here? 
    // loads images and returns array of photos, so that photo fields can be accessed easily
    public MyImage[] loadImages(String path, int buttonWidth) throws Exception {
        photos = new MyImage[this.imageNames.length];
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
            }
        }
        return photos;
    }
}
