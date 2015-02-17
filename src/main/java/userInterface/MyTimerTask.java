package main.java.userInterface;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private String s;
    private MyImage selectedImage;
    private int contentpanelHeight;
    private int contentpanelWidth = 0;

    public void run() {

        if (this.selectedImage != null) {

            if (s == "enlarge" && selectedImage.getHeight() < 650) {

                double normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
                double normalizerY = (double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight());

                int newX = (int) (selectedImage.getX() - normalizerX);
                int newY = (int) (selectedImage.getY() - normalizerY);
                int newWidth = (int) (selectedImage.getWidth() + 6 * normalizerX);
                int newHeight = (int) (selectedImage.getHeight() + 6 * normalizerY);

                //If picture size makes it go out of bounds, make it expand to the other side only.
                selectedImage.setX((contentpanelWidth < newX + newWidth) ? contentpanelWidth - newWidth : newX);
                selectedImage.setY((contentpanelHeight < newY + newHeight) ? contentpanelHeight - newHeight : newY);
                selectedImage.resizeImg(newWidth, newHeight);
            } else if (s == "reduce" && selectedImage.getHeight() > 120) {
                double normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
                double normalizerY = (double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight());

                selectedImage.setY((int) (selectedImage.getY() + 4 * normalizerY));
                selectedImage.setX((int) (selectedImage.getX() + 4 * normalizerX));

                selectedImage.resizeImg((int) (selectedImage.getWidth() - 8 * normalizerX), (int) (selectedImage.getHeight() - 8 * normalizerY));
            }
        }
    }

    public void addVariables(MyImage selectedImage, String s, int contentpanelWidth, int contentpanelHeight) {
        this.selectedImage = selectedImage;
        this.s = s;
        this.contentpanelHeight = contentpanelHeight;
        this.contentpanelWidth = contentpanelWidth;
    }

    public boolean isRunning() {
        if(s == null) {
            return false;
        }
        return true;
    }
}