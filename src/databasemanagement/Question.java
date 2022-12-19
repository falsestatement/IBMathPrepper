package databasemanagement;

import java.io.Serializable;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name, qDesc, aDesc;
    private int id, topicId, pointVal, type, examId, parts;
    private boolean isExam;

    public Question(int id, int topicId, String name, String qDesc, String aDesc, int pointVal, int type, boolean isExam, int parts, int examId){
        this.id = id;
        this.topicId = topicId;
        this.name = name;
        this.qDesc = qDesc;
        this.aDesc = aDesc;
        this.pointVal = pointVal;
        this.type = type;
        this.isExam = isExam;
        this.parts = parts;
        this.examId = examId;
    }

    public String getName() {
        return name;
    }

    public String getQDesc() {
        return qDesc;
    }

    public String getADesc() {
        return aDesc;
    }

    public int getId() {
        return id;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getPointVal() {
        return pointVal;
    }

    public int getType() {
        return type;
    }

    public int getExamId() {
        return examId;
    }

    public int getParts() {
        return parts;
    }

    public boolean isExam() {
        return isExam;
    }

    public String toString(){
        return String.format("id: %d, topic id: %d, name: %s, question description: %s, " +
                "answer description: %s, point value: %d, type: %d, isExam: %b, " +
                "parts: %d, exam id: %d", id, topicId, name, qDesc, aDesc, pointVal, type, isExam, parts, examId);
    }
}
