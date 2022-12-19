package databasemanagement;

import java.util.ArrayList;

public abstract class DBArrayManager {
    public static ArrayList<Question> toQuestionArray(ArrayList<String[]> arr){
        String name, qDesc, aDesc;
        int id, topicId, pointVal, type, examId, parts;
        boolean isExam;

        ArrayList<Question> r = new ArrayList<>();

        for(String[] str : arr){
            id = Integer.parseInt(str[0]);
            topicId = Integer.parseInt((str[1]));
            name = str[2];
            qDesc = str[3];
            aDesc = str[4];
            pointVal = Integer.parseInt(str[5]);
            type = Integer.parseInt((str[6]));
            isExam = 1 == Integer.parseInt(str[7]);
            parts = Integer.parseInt(str[8]);
            try {
                examId = Integer.parseInt(str[9]);
            }catch(NumberFormatException e){
                examId = -1;
            }

            r.add(new Question(id, topicId, name, qDesc, aDesc, pointVal, type, isExam, parts, examId));
        }

        return r;
    }

    public static ArrayList<Topic> toTopicArray(ArrayList<String[]> arr){
        int id;
        String name;

        ArrayList<Topic> r = new ArrayList<>();

        for(String[] str : arr){
            id = Integer.parseInt(str[0]);
            name = str[1];

            r.add(new Topic(id, name));
        }

        return r;
    }

    public static ArrayList<Exam> toExamArray(ArrayList<String[]> arr){
        int id, paper, year;
        String name, month;

        ArrayList<Exam> r = new ArrayList<>();

        for(String[] str : arr){
            id = Integer.parseInt(str[0]);
            name = str[1];
            paper = Integer.parseInt(str[2]);
            year = Integer.parseInt(str[3]);
            month = str[4];

            r.add(new Exam(id, name, paper, year, month));
        }

        return r;
    }

    public static <E> void printArray(ArrayList<E> arr){
        for(E element : arr){
            System.out.println(element);
        }
    }
}
