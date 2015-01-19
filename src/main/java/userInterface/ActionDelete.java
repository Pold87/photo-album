package main.java.userInterface;

public class ActionDelete implements Action {
	private MyImage image;
	private OurController ourController;
	//private Page page; //Implement when page functionality is added.
	
	public ActionDelete(MyImage image, OurController ourController) {
		this.image = image;
		this.ourController = ourController;
	}

	@Override
	public void redo() {
		ourController.selectPicture(image);
		ourController.deleteSelectedPicture();
	}

	@Override
	public void undo() {
		ourController.addPictureToCurrentPage(image);
		ourController.removeLastActionFromList();
	}

}
