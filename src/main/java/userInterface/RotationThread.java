package main.java.userInterface;

/**
 * I think this is a really ugly way to do this rotating,
 * but for some reason it was the only way I could think of at the moment.
 * @author Dennis
 */
public class RotationThread implements Runnable {
	public boolean keepGoing;
	private OurController controller;
	
	public RotationThread(OurController controller){
		this.controller = controller;
	}
	
	@Override
	public void run() {
		synchronized(this){
		keepGoing = true;
		while(keepGoing){
			controller.rotate(1);
			try {
				this.wait(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}

}
