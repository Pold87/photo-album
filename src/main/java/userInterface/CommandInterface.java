package main.java.userInterface;

import java.awt.Color;
import java.io.FileNotFoundException;

public interface CommandInterface {

	abstract void selectPicture(int nr); //Voice
	abstract void selectPicture(int x, int y); //LEAP is this necessary?
	abstract void nextPage(); //Both?
	abstract void previousPage(); //Both?
	abstract void selectSpecificPage(int nr); //Voice
	abstract void insertPages(); //Insert 2 pages after current page. //Both?
	abstract void removeCurrentPages(); //Both?
	abstract void addPictureFromLibrary(int nr) throws FileNotFoundException; //Both? maybe different parameters for LEAP?
	abstract void deletePicture(int nr); //Both?
	abstract void movePicture(int x, int y); //LEAP
	abstract void setBackground(Color color); //Both? Maybe different parameters.
	abstract void rotate(int degrees); //Both?
	//abstract void 







}
