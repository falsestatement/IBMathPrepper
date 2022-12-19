package databasemanagement;

import java.io.Serializable;

public class Topic implements Serializable {
    private static final long serialVersionUID = 2L;

    private int id;
    private String name;

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return String.format("id: %d, name: %s", id, name);
    }
}

