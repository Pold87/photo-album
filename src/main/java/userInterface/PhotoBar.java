package main.java.userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by pold on 12/11/14.
 */
public class PhotoBar extends JToolBar {

    private String[] images;
    private PhotoBarListener listener;

    public PhotoBar(String[] images) throws Exception {

        setOrientation(SwingConstants.VERTICAL);
        setBackground(Color.white);
        this.images = images;
    }

    // loads images and returns array of photos, so that photo fields can be accessed easily
    public MyImage[] loadImages(String path, int buttonWidth) throws Exception {
        MyImage[] photos = new MyImage[this.images.length];
        int x = 20;
        int y = 20;
        for (int i = 0; i < this.images.length; i++) {
            String pic = this.images[i];
            String imgPath = new String(path + pic);
            MyImage photo = new MyImage(imgPath, x, y, i);
            photos[i] = photo;
            ThumbnailAction thumbAction = new ThumbnailAction(photo,buttonWidth, pic);
            JButton thumbButton = new JButton(thumbAction);
            this.add(thumbButton);
            x = x+20;
            y = y+20;
        }
        return photos;
    }

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction {
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
         */
        public void actionPerformed(ActionEvent e) {
            listener.recognizedClick(displayPhoto);
        }
    }

    public void setPhotoBarListener(PhotoBarListener listener) {
        this.listener = listener;
    }

}
