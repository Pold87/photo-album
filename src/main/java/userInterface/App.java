package main.java.userInterface;

import java.awt.*;
import java.io.IOException;

/**
 * Created by pold on 12/10/14.
 */
public class App {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
//                    WebLookAndFeel.install();
                    new BasicDesign(600,
                            600,
                            "/pictures/");
                            //new String[]{"IMG_1.jpg", "IMG_2.jpg", "IMG_3.jpg", "IMG_4.jpg", "IMG_5.jpg", "IMG_6.jpg", "IMG_7.jpg"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
