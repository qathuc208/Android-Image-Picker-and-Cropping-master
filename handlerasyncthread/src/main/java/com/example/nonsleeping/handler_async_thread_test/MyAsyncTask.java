package com.example.nonsleeping.handler_async_thread_test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MyAsyncTask extends AsyncTask<Integer, Integer, ArrayList<Integer>>{

    private LinearLayout llrandom, llprime;
    private Random rd = new Random();
    private Activity context;

    public MyAsyncTask(Activity context) {
        this.llrandom = (LinearLayout) context.findViewById(R.id.llrandomnumber);
        this.llprime = (LinearLayout) context.findViewById(R.id.llprimenumber);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Start here",
                Toast.LENGTH_SHORT).show();
        this.llrandom.removeAllViews();
        this.llprime.removeAllViews();
    }

    @Override
    protected ArrayList<Integer> doInBackground(Integer... integers) {
        int step = 1;
        ArrayList<Integer> list = new ArrayList<Integer>();

        int n = integers[0];

        while (isCancelled() == false && step <= n) {
            step++;
            SystemClock.sleep(100);
            int x = rd.nextInt(100) + 1;
            publishProgress(x);

            if (isPrime(x))
                list.add(x);
        }
        return list;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Integer item = values[0];
        Button btn = new Button(context);
        btn.setWidth(100);
        btn.setHeight(30);
        btn.setText(item+"");
        this.llrandom.addView(btn);
    }

    @Override
    protected void onPostExecute(ArrayList<Integer> integers) {
        super.onPostExecute(integers);
        final ArrayList<Integer> list = integers;
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    final int x = list.get(i);
                    SystemClock.sleep(100);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            doRawPrime(x);
                        }
                    });

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Finish",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public void doRawPrime(int x)
    {
        //hàm vẽ các Button là chứac các số nguyên tố
        Button btn=new Button(context);
        btn.setWidth(100);
        btn.setHeight(30);
        btn.setText(x+"");
        //đưa Button vào view bên phải màn hình
        this.llprime.addView(btn);
    }

    //hàm kiểm tra số nguyên tố
    public boolean isPrime(int x)
    {
        if(x<2)return false;
        for(int i=2;i<=Math.sqrt(x);i++){
            if(x%i==0)return false;}
        return true;
    }
}
