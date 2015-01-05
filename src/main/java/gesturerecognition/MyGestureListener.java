package main.java.gesturerecognition;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;

/**
 * Created by pold on 12/15/14.
 */
public class MyGestureListener extends Listener {

    public void onConnect(Controller controller) {
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
    }

        public void onFrame (Controller controller) {

            GestureList gestureList = controller.frame().gestures();

            for (Gesture g : gestureList) {

//                System.out.println(controller.frame( + ", " + positionChanges[1]);

            }

//            while (controller.frame().fingers(listener.getPositions()) <= 70) {
//                float[] positionChanges = ges.doGesture(1);
                //System.out.println(positionChanges[0] + ", " + positionChanges[1]);
//                frame.set(positionChanges, maxx, maxy);
                //frame.set(positionChanges);
//            }
//        }
//    }

}
}
