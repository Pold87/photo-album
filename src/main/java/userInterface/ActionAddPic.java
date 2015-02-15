package main.java.userInterface;


public class ActionAddPic extends Action {
	//private Page page; //Add when page functionality is available.
	
	public ActionAddPic(MyImage image, OurController ourController) {
		super(image, "Add", ourController, 0, 0, 0, 0);
	}
	
	public void redo() {
		super.redo();
		ourController.contentPanel.addPictureToCurrentPage(image);
		ourController.selectPicture(image);
		ourController.removeButtonFromLibrary(image);
	}

	public void undo() {
		super.undo();
		ourController.selectPicture(image);
		ourController.contentPanel.deleteSelectedPicture();
		ourController.addButtonToLibrary(image);
	}


}
