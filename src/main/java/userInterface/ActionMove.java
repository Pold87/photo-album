package main.java.userInterface;

public class ActionMove implements Action {

	private MyImage target;
	private int oldX, oldY, newX, newY;
	private OurController ourController;
	
	public ActionMove(MyImage target, int oldX, int oldY, int newX, int newY, OurController ourController) {
		this.target = target;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.ourController = ourController;

        // Added by Volker because I got a NullPointerException
        if (target != null) {
            ourController.logger.logAction("Move picture " + target.getNum() + " to " + newX + " " + newY);
        }
	}

	public void redo() {
		ourController.selectPicture(target);
		ourController.movePicture(newX, newY);
		ourController.logger.logAction("Redo");
	}

	public void undo() {
		System.out.println("undo move");
		ourController.selectPicture(target);
		ourController.movePicture(oldX, oldY);
		ourController.removeLastActionFromList();
		ourController.logger.logAction("Undo");
	}

}
