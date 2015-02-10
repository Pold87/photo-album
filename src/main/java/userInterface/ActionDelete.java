package main.java.userInterface;

public class ActionDelete implements Action {
	private MyImage image;
	private OurController ourController;
	//private Page page; //Implement when page functionality is added.
	
	public ActionDelete(MyImage image, OurController ourController) {
		this.image = image;
		this.ourController = ourController;
		ourController.logger.logAction("Delete picture " + image.getNum());
	}


	public void redo() {
		ourController.selectPicture(image);
		ourController.deleteSelectedPicture();
		ourController.logger.logAction("Redo");
	}


	public void undo() {
		ourController.addPictureToCurrentPage(image);
		ourController.removeLastActionFromList();
		ourController.logger.logAction("Undo");
	}

}
