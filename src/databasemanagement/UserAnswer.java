package databasemanagement;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAnswer implements Serializable {
    private static final long serialVersionUID = 4L;

    private Question q;
    private String answer;

    public UserAnswer(Question q, String answer){
        this.q = q;
        this.answer = answer;
    }

    public Question getQuestion() {
        return q;
    }

    public String getAnswer() {
        return answer;
    }

    public static void updateLocal(ArrayList<UserAnswer> userAnswers){
        DatabaseSerialiser<UserAnswer> userAnswerSerializer = new DatabaseSerialiser<>("useranswers", userAnswers);
        userAnswerSerializer.serialize();
    }

    public String toString(){
        return q.toString() + String.format(" | Answer: %s", answer);
    }
}
