package listeners;

import databasemanagement.UserAnswer;
import java.util.ArrayList;

public interface UserAnswerListener {
    void showUserAnswers(ArrayList<UserAnswer> userAnswers);
}
