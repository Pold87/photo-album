package main.java.userInterface;

import java.util.Comparator;

/**
 * Created by pold on 1/26/15.
 */
public class StringAndInt implements Comparable<StringAndInt> {

    String str; // The words
    int i; // its distance

    public StringAndInt(String str, int i) {
        this.str = str;
        this.i = i;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }



    @Override
    public int compareTo(StringAndInt stringAndInt) {

        return this.i - stringAndInt.getI();

    }
}
