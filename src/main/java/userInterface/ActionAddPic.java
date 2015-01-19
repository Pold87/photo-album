package main.java.userInterface;

public class ActionAddPic implements Action {
	private MyImage picture;
	private OurController ourController;
	//private Page page; //Add when page functionality is available.
	
	public ActionAddPic(MyImage picture, OurController ourController) {
		this.picture = picture;
		this.ourController = ourController;
	}
	
	@Override
	public void redo() {
		ourController.addPictureToCurrentPage(picture);
	}

	@Override
	public void undo() {
		ourController.selectPicture(picture);
		ourController.deleteSelectedPicture();
		ourController.removeLastActionFromList();
	}

}
