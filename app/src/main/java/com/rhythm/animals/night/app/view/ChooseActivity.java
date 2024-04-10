package com.rhythm.animals.night.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.rhythm.animals.night.app.R;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button factsButton = findViewById(R.id.privacy_policy_button);
        Button quizButton = findViewById(R.id.quiz_button);

        factsButton.setOnClickListener(v -> {
            showPrivacyPolicyDialog();
        });

        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this, AnotherActivity.class);
            startActivity(intent);
        });
        Button exitButton = findViewById(R.id.exit);
        exitButton.setOnClickListener(v -> {
            finishAffinity(); // Закрываем все активности приложения
        });

    }

    private void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Privacy Policy");
        builder.setMessage(R.string.privacy_policy); // текст из ресурсов strings.xml
        builder.setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        // Ничего не делать или выполнить другие действия
    }
}