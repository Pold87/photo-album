package main.java.userInterface;

import javax.swing.*;

import java.awt.Color;
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
    	System.out.println("removing");
    	for(int i = 0; i < nrOfImages; i++){
    		if(photos[i].equals(image)){
    			this.remove(allButtons[i]);
    			System.out.println("Actually removed the button");
    		}
    	}
    }
    
    public void addButton(MyImage image){
    	for(int i = 0; i < nrOfImages; i++){
    		if(photos[i].equals(image)){
    			this.add(allButtons[i]);
    		}
    	}
    }
}
