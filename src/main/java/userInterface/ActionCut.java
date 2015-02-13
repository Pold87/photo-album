package main.java.userInterface;

public class ActionCut implements Action{

    private MyImage picture;
    private OurController ourController;

    public ActionCut(MyImage picture, OurController ourController) {
        this.picture = picture;
        this.ourController = ourController;
        long time = (System.currentTimeMillis() - App.startTime)/1000;
        ourController.logger.logAction("Cut picture " + picture.getNum()+ "," + time);
    }

    public void redo() {
        ourController.selectPicture(picture);
        ourController.logger.logAction("Redo");
    }


    public void undo() {
        System.out.println("undo cut");
        ourController.selectPicture(picture);
        ourController.removeLastActionFromList();
        ourController.logger.logAction("Undo");
    }

}
