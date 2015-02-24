package main.java.userInterface;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private String s;
    private MyImage selectedImage;
    private int contentpanelHeight;
    private int contentpanelWidth = 0;

    public void run() {

        if (this.selectedImage != null) {
        	int oldWidth = selectedImage.getWidth();
        	int oldHeight = selectedImage.getHeight();
        	double ratio = selectedImage.getOriginalRatio();
        	
            if (s == "enlarge" && selectedImage.getHeight() < 650) {

                double normalizerX = (double) oldWidth / ((double) oldWidth + oldHeight);
                double normalizerY = (double) oldHeight / ((double) oldWidth + oldHeight);

                int newX = (int) (selectedImage.getX() - normalizerX);
                int newY = (int) (selectedImage.getY() - normalizerY);
                int newHeight = (int) (oldHeight + 6 * normalizerY);

                System.out.println("Ratio: " + ratio);
                int newWidth =  (int) (newHeight/ratio);
                System.out.println( " New Width: "+ newWidth );
                //If picture size makes it go out of bounds, make it expand to the other side only.
                selectedImage.setX((contentpanelWidth < newX + newWidth) ? contentpanelWidth - newWidth : newX);
                selectedImage.setY((contentpanelHeight < newY + newHeight) ? contentpanelHeight - newHeight : newY);
                selectedImage.resizeImg(newWidth, newHeight);
            } else if (s == "reduce" && selectedImage.getHeight() > 120) {
                double normalizerX = (double) oldWidth / (double) (oldWidth + oldHeight);
                double normalizerY = (double) oldHeight / (double) (oldWidth + oldHeight);

                selectedImage.setY((int) (selectedImage.getY() + 4 * normalizerY));
                selectedImage.setX((int) (selectedImage.getX() + 4 * normalizerX));
                int newHeight = (int) (oldHeight - 6 * normalizerY);

                int newWidth =  (int) (newHeight/ratio);
                selectedImage.resizeImg(newWidth, newHeight);
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