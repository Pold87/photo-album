package main.java.userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by pold on 12/11/14.
 */
public class PhotoBar extends JToolBar {

    private String[] images = {"IMG_1.jpg", "IMG_2.jpg"}; // Names of images
    private String path = "/pictures/"; // Path of images

    public PhotoBar() throws Exception {

        setOrientation(SwingConstants.VERTICAL);
        setBackground(Color.white);
        this.loadimages(100, 100, 100);

    }

    // loads images and returns array of photos, so that photo fields can be accessed easily
    private Photo[] loadimages(int buttonWidth, int buttonHeight, double sideLength) throws Exception {
        Photo[] photos = new Photo[images.length];
        for (int i = 0; i < images.length; i++) {
            String pic = images[i];
                Photo photo = new Photo(this.path, pic, buttonWidth, buttonHeight, (float) sideLength);
                photos[i] = photo;
                ThumbnailAction thumbAction = new ThumbnailAction(photo.getDispPic(), photo.getThumbnailIcon(), pic);
                JButton thumbButton = new JButton(thumbAction);
                this.add(thumbButton); // photoBar.getComponentCount()
        }
        return photos;
    }

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private ImageIcon displayPhoto;

        public ThumbnailAction(ImageIcon photo, ImageIcon thumb, String desc) {

            displayPhoto = photo;

            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {




            //setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
        }
    }

}
