package com.rhythm.animals.night.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rhythm.animals.night.app.R;
import com.rhythm.animals.night.app.model.Question;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AnotherActivity extends AppCompatActivity {
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;
    private List<Integer> selectedOptions = new ArrayList<>(); // Хранит выбранные варианты ответа

    private TextView questionTextView;
    private LinearLayout optionsLinearLayout;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        questionTextView = findViewById(R.id.questionTextView);
        optionsLinearLayout = findViewById(R.id.buttonContainer);
        submitButton = findViewById(R.id.submitButton);

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
                int selectedOptionIndex = getSelectedOptionIndex();

                if (selectedOptionIndex != -1) {
                    checkAnswer(selectedOptionIndex);
                } else {
                    Toast.makeText(this, "Выберите вариант ответа", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("TAG", "Нет данных вопросов");
            Toast.makeText(this, "Нет данных для вопросов", Toast.LENGTH_SHORT).show();
        }

        // Находим иконку (ImageView) для кнопки возвращения к предыдущему вопросу
        ImageView pointerImageView = findViewById(R.id.pointer);
        // Находим иконку (ImageView) для открытия ChooseActivity
        ImageView menuImageView = findViewById(R.id.menu);
        menuImageView.setOnClickListener(v -> {
            Intent intent = new Intent(AnotherActivity.this, ChooseActivity.class);
            startActivity(intent);
        });

    }

    private void updateQuestionAndOptions() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());

        optionsLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(200, 30, 200, 30); // Отступы между кнопками

        for (String option : currentQuestion.getOptions()) {
            View customButton = LayoutInflater.from(this).inflate(R.layout.custom_button, optionsLinearLayout, false);
            Button optionButton = customButton.findViewById(R.id.quizButton);
            optionButton.setText(option);
            optionButton.setLayoutParams(layoutParams); // Установка параметров макета для кнопки

            // Блокируем кнопку выбора ответа, если пользователь уже выбрал ответ
            if (selectedOptions.contains(currentQuestionIndex)) {
                optionButton.setEnabled(false);
            } else {
                // Добавляем обработчик нажатия на кнопку, если ответ еще не выбран
                optionButton.setOnClickListener(view -> {
                    int selectedOptionIndex = optionsLinearLayout.indexOfChild(customButton);
                    checkAnswer(selectedOptionIndex);
                });
            }

            optionsLinearLayout.addView(customButton);
        }
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < optionsLinearLayout.getChildCount(); i++) {
            View child = optionsLinearLayout.getChildAt(i);
            if (child.isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void checkAnswer(int selectedOptionIndex) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        int correctAnswerIndex = currentQuestion.getOptions().indexOf(currentQuestion.getAnswer());

        Button selectedButton = (Button) optionsLinearLayout.getChildAt(selectedOptionIndex);

        if (selectedOptionIndex == correctAnswerIndex) {
            score++;
            selectedButton.setBackgroundResource(R.drawable.custom_button_background_right);
        } else {
            Button correctButton = (Button) optionsLinearLayout.getChildAt(correctAnswerIndex);
            correctButton.setBackgroundResource(R.drawable.custom_button_background_right);
            selectedButton.setBackgroundResource(R.drawable.custom_button_background_wrong);
        }

        // Блокируем нажатие на кнопки после ответа
        disableOptionButtons();

        // Разблокируем кнопку Next и добавляем обработчик нажатия
        submitButton.setEnabled(true);
        submitButton.setOnClickListener(v -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                updateQuestionAndOptions();
                submitButton.setEnabled(false); // Блокируем кнопку Next снова
            } else {
                showResultDialog();
            }
        });
    }

    private void disableOptionButtons() {
        for (int i = 0; i < optionsLinearLayout.getChildCount(); i++) {
            optionsLinearLayout.getChildAt(i).setEnabled(false);
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
