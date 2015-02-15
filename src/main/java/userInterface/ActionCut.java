package main.java.userInterface;

public class ActionCut extends Action{

    public ActionCut(MyImage image, OurController ourController) {
		super(image, "Cut", ourController, 0, 0, 0, 0);
    }

    //TODO this still needs to be implemented
    public void redo() {
    	super.redo();
        ourController.selectPicture(image);
        ourController.logger.logAction("Redo");
    }

    //TODO this still needs to be implemented
    public void undo() {
    	super.undo();
		ourController.selectPicture(image);
        ourController.logger.logAction("Undo");
    }
}