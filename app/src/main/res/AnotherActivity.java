import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class AnotherActivity extends AppCompatActivity {
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("quiz.json")));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String jsonString = stringBuilder.toString();

            Gson gson = new Gson();
            TypeToken<List<Question>> listType = new TypeToken<List<Question>>() {};
            questions = gson.fromJson(jsonString, listType.getType());

            if (questions != null && !questions.isEmpty()) {
                updateQuestionAndOptions();

                submitButton.setOnClickListener(v -> {
                    int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();

                    if (selectedOptionId != -1) {
                        checkAnswer();
                    } else {
                        Toast.makeText(this, "Выберите вариант ответа", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("TAG", "Нет данных вопросов");
                Toast.makeText(this, "Нет данных для вопросов", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ex) {
            Log.e("TAG", "Ошибка при загрузке данных: " + ex);
            Toast.makeText(this, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuestionAndOptions() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());
        
        optionsRadioGroup.removeAllViews();
        for (String option : currentQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(option);
            optionsRadioGroup.addView(radioButton);
        }
        optionsRadioGroup.clearCheck();
    }

    private void checkAnswer() {
        int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();

        if (selectedOptionId != -1) {
            int selectedOptionIndex = optionsRadioGroup.indexOfChild(findViewById(selectedOptionId));
            Question currentQuestion = questions.get(currentQuestionIndex);
            int correctAnswerIndex = currentQuestion.getOptions().indexOf(currentQuestion.getAnswer());

            if (selectedOptionIndex == correctAnswerIndex) {
                score++;
            }

            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                updateQuestionAndOptions();
            } else {
                showResultDialog();
            }
        } else {
            Toast.makeText(this, "Выберите вариант ответа", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResultDialog() {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("score", score);
        resultIntent.putExtra("totalQuestions", questions.size());
        startActivity(resultIntent);
    }

    @Override
    public void onBackPressed() {
        // Блокировка кнопки "Назад"
    }
}
