package main.java.userInterface;

public class ActionDelete extends Action {
	
	public ActionDelete(MyImage image, OurController ourController) {
		super(image, "Delete", ourController, 0, 0, 0, 0);
	}


	public void redo() {
		super.redo();
		ourController.selectPicture(image);
		ourController.contentPanel.deleteSelectedPicture();
		ourController.addButtonToLibrary(image);
	}


	public void undo() {
		super.undo();
		ourController.contentPanel.addPictureToCurrentPage(image);
		ourController.selectPicture(image);
		ourController.removeButtonFromLibrary(image);
	}
}