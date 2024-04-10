package com.rhythm.animals.night.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.rhythm.animals.night.app.R;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        String scoreText =  score + "/" + totalQuestions;
        scoreTextView.setText(scoreText);


         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backToChooseMenu = findViewById(R.id.menu);
        backToChooseMenu.setOnClickListener(v -> {

            Intent intent = new Intent(ResultActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backToChooseTextView = findViewById(R.id.pointer);
        backToChooseTextView.setOnClickListener(v -> {

            Intent intent = new Intent(ResultActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Здесь ничего не делаем, чтобы заблокировать кнопку "Назад"
    }
}
