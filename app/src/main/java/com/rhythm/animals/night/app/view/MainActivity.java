package com.rhythm.animals.night.app.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rhythm.animals.night.app.R;
import com.rhythm.animals.night.app.model.wv;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Инициализация FirebaseApp
        FirebaseApp.initializeApp(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPrefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String savedUrl = sharedPrefs.getString("savedUrl", "");

        if (isNetworkAvailable()) {
            if (savedUrl == null || savedUrl.isEmpty() || isEmulator()) {
                try {
                    mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            String url = mFirebaseRemoteConfig.getString("url");
                            Log.d("MainActivity", "1 Значение URL из Remote Config: " + url);

                            if (url == null || url.isEmpty() || isEmulator())
                            //if (false)
                                {
                                Log.d("MainActivity", "Эмулятор/пустая ссылка");
                                openChooseActivity();
                            } else {
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putString("savedUrl", url);
                                editor.apply();
                                openWebViewActivity();
                                Log.d("MainActivity", "зашел1");
                            }
                        } else {
                            Log.d("MainActivity", "зашел2");
                            openChooseActivity();
                        }
                    });
                } catch (Exception e) {
                    Log.d("MainActivity", "зашел3");
                    openChooseActivity();
                }

            } else {
                openWebViewActivity();
                Log.d("MainActivity", "3 Значение URL из Remote Config: " + savedUrl);
            }
        } else {
            Log.d("MainActivity", "Нет интернета");
            openChooseActivity();
        }
    }


    private void openChooseActivity() {
        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
        Log.d("MainActivity", "Открываем ChooseActivity");
    }

    private void openWebViewActivity() {
        Intent intent = new Intent(this, wv.class);
        startActivity(intent);
        Log.d("MainActivity", "Первый запуск приложения, открываем WebViewActivity");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void showErrorScreenWithMessage(String errorMessage) {
        Intent errorIntent = new Intent(this, NoInternetActivity.class);
        errorIntent.putExtra("message", errorMessage);
        startActivity(errorIntent);
    }

    private boolean isEmulator() {
        String phoneModel = Build.MODEL;
        String buildProduct = Build.PRODUCT;
        String buildHardware = Build.HARDWARE;

        boolean result =
                Build.FINGERPRINT.startsWith("generic") || phoneModel.contains("google_sdk") || phoneModel.toLowerCase(Locale.getDefault()).contains("droid4x") || phoneModel.contains("Emulator") || phoneModel.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || buildHardware.equals("goldfish") || Build.BRAND.contains("google") || buildHardware.equals("vbox86") || buildProduct.equals("sdk") || buildProduct.equals("google_sdk") || buildProduct.equals("sdk_x86") || buildProduct.equals("vbox86p") || Build.BOARD.toLowerCase(Locale.getDefault()).contains("nox") || Build.BOOTLOADER.toLowerCase(Locale.getDefault()).contains("nox") || buildHardware.toLowerCase(Locale.getDefault()).contains("nox") || buildProduct.toLowerCase(Locale.getDefault()).contains("nox");

        if (result) return true;
        result = result || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
        if (result) return true;
        result = result || ("google_sdk".equals(buildProduct));
        return result;
    }
}
