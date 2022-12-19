package databasemanagement;

import java.io.Serializable;

public class Exam implements Serializable {
    private static final long serialVersionUID = 3L;

    private int id, paper, year;
    private String name, month;

    public Exam(int id, String name, int paper, int year, String month){
        this.id = id;
        this.name = name;
        this.paper = paper;
        this.year = year;
        this.month = month;
    }

    public int getId() {
        return id;
    }

    public int getPaper() {
        return paper;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public String getMonth() {
        return month;
    }

    public String toString(){
        return String.format("id: %d, name: %s, paper: %d, year: %d, month: %s", id, name, paper, year, month);
    }
}
