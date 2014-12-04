package userInterface;

public interface commandInterface {

	abstract void selectPicture(int nr); //Voice
	abstract void nextPage();//Voice and/or LEAP
	abstract void previousPage(); //Voice and/or LEAP
	abstract void selectSpecificPage(int nr); //Voice
	abstract void insertPages(); //Insert 2 page after current page. //Voice or LEAP?
	abstract void removeCurrentPages(); //Voice or LEAP?
	abstract void addPictureFromLibrary(int nr); //Voice or LEAP?
	







}
