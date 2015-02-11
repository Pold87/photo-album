package main.java.userInterface;

public class ActionAddPic implements Action {
	private MyImage picture;
	private OurController ourController;
	//private Page page; //Add when page functionality is available.
	
	public ActionAddPic(MyImage picture, OurController ourController) {
		this.picture = picture;
		this.ourController = ourController;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Added picture " + picture.getNum()+ " Timestamp: " + time);

	}
	

	public void redo() {
		ourController.addPicture(picture);
		ourController.logger.logAction("Redo");
	}


	public void undo() {
		ourController.selectPicture(picture);
		ourController.deleteSelectedPicture();
		ourController.removeLastActionFromList();
		ourController.logger.logAction("Undo");
	}

}
