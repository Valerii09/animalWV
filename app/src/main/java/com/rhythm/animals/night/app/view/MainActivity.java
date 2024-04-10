package com.rhythm.animals.night.app.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.rhythm.animals.night.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openChooseActivity();
    }
    private void openChooseActivity() {
        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
        Log.d("MainActivity", "Открываем ChooseActivity");
    }

}
