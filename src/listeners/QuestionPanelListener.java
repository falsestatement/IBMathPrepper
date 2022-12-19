package listeners;

import databasemanagement.Question;

import java.util.ArrayList;

public interface QuestionPanelListener {
    public void showQuestionPanel(ArrayList<Question> questions);
}
