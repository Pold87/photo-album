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
                    new BasicDesign(600,
                            600,
                            "/pictures/");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
