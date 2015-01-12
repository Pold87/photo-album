package main.java.userInterface;

public class ActionAddPic implements Action {
	private MyImage picture;
	private Controller controller;
	//private Page page; //Add when page functionality is available.
	
	public ActionAddPic(MyImage picture, Controller controller) {
		this.picture = picture;
		this.controller = controller;
	}
	
	@Override
	public void redo() {
		controller.addPictureToCurrentPage(picture);
	}

	@Override
	public void undo() {
		controller.selectPicture(picture);
		controller.deleteSelectedPicture();
		controller.removeLastActionFromList();
	}

}
