package com.example.debugtools;
import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private final String TAG = "DEFAULT_TAG";
    private final int min = 20;
    private final int max = 80;
    private final int random = new Random().nextInt((max - min) + 1) + min;
    private int[] array;
    private int thread_count;
    private boolean isRunning = true;
    private Thread backgroundThread; // Переменная для отслеживания потока

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        array = new int[random]; // Инициализация массива

        for (int i = 0; i < random; i++) {
            array[i] = new Random().nextInt();
        }

        for (int i = 0; i < array.length; i++) {
            Log.d(TAG, String.valueOf(array[i]));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundThread == null || !backgroundThread.isAlive()) {
            thread_count++;
            backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        Log.d(TAG, "Number of threads running: " + thread_count);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            backgroundThread.start(); // Запуск потока, если он ещё не запущен
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false; // Остановка потока при приостановке активности
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false; // Убедитесь, что поток остановлен при уничтожении активности
        try {
            backgroundThread.join(); // Ожидание завершения потока
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}