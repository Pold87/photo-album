package main.java.userInterface;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionListener;

/**
 * Created by pold on 12/11/14.
 */
public class PhotoBar extends JToolBar {

	private static final long serialVersionUID = 1L;
	private String[] imageNames;
    private ActionListener listener;
    private MyImage [] photos;

    public PhotoBar(String[] images, Controller listener) throws Exception {
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
            String imgPath = new String(path + pic);
            System.out.println(imgPath);
            if(pic != null){
            	MyImage photo = new MyImage(imgPath, x, y, i);
            	photos[i] = photo;
            	//ThumbnailAction thumbAction = new ThumbnailAction(photo,buttonWidth, pic);
            	JButton thumbButton = new JButton(new ImageIcon(photo.getScaledImage(buttonWidth, buttonWidth)));
            	thumbButton.setActionCommand(i + "");
            	this.add(thumbButton);
            	thumbButton.addActionListener(listener);
            	x = x+20;
            	y = y+20;
            }
        }
        return photos;
    }

    /**
     * Action class that shows the image specified in it's constructor.
     * Dennis: I may have deprecated this stuff.
     */
    /*private class ThumbnailAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private MyImage displayPhoto;
        private ImageIcon thumb;

        public ThumbnailAction(MyImage photo, int bWidth, String desc) {
        	displayPhoto = photo;
            thumb = new ImageIcon(photo.getScaledImage(bWidth, bWidth));

            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         
        public void actionPerformed(ActionEvent e) {
        	//listener.recognizedClick(displayPhoto);
        }
    } */
}
