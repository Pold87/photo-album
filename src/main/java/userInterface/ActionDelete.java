package main.java.userInterface;

public class ActionDelete implements Action {
	private MyImage image;
	private Controller controller;
	//private Page page; //Implement when page functionality is added.
	
	public ActionDelete(MyImage image, Controller controller) {
		this.image = image;
		this.controller = controller;
	}

	@Override
	public void redo() {
		controller.selectPicture(image);
		controller.deleteSelectedPicture();
	}

	@Override
	public void undo() {
		controller.addPictureToCurrentPage(image);
		controller.removeLastActionFromList();
	}

}
