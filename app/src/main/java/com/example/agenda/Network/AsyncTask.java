package com.example.agenda.Network;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTask {

    private final Executor executor = Executors.newCachedThreadPool();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public <R> void executeAsync(Callable<R> asyncOperation, Callback<R> mainThreadOperation){
        try {
            executor.execute(getAsyncTask(asyncOperation, mainThreadOperation));
        }catch(Exception e){
            Log.e("Async task error","executor failed "+ e.getMessage());
        }
    }

    @NonNull
    private  <R> Runnable getAsyncTask (Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        return () -> {
            try {
                R result = asyncOperation.call();
                handler.post(runAsyncTask(result, mainThreadOperation));
            } catch (Exception e) {
                Log.e("Async task error", "executeAsync failed " + e.getMessage());
            }
        };
    }

    @NonNull
    private static <R> Runnable runAsyncTask(R result, Callback<R> mainThreadOperation) {
        return () -> mainThreadOperation.getInfoOnUiThread(result);
    }

}
