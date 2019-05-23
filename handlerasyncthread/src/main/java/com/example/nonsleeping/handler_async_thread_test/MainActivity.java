package com.example.nonsleeping.handler_async_thread_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearLayoutRoot;
    Handler handler = new Handler();
    ArrayList<Integer> arr = new ArrayList<>();
    ArrayAdapter<Integer> adapter = null;
    AtomicBoolean ab = new AtomicBoolean(false);
    Button btnStart;
    MyAsyncTask task;

    private Handler mHandler;
    private static final int MSG_UPDATE_NUMBER = 100;
    private static final int MSG_UPDATE_NUMBER1 = 102;

    private static final int MSG_UPDATE_NUMBER_DONE = 101;
    private static final int MSG_UPDATE_NUMBER_DONE1 = 103;


    private TextView mTextNumber, mTextNumber1;
    private ProgressBar mProgressBar;

    private boolean mIscounting, mIscounting1;
    private boolean mIsState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createLayout();
        //setContentView(linearLayoutRoot);
        setContentView(R.layout.handle_asynctask);

        initViewsNew();

        btnStart = (Button) findViewById(R.id.btnstart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStart();
            }
        });
        //initViews();
        //listenerHandler();
    }

    private void doStart() {
        String s=((EditText)
                this.findViewById(R.id.editnumber))
                .getText().toString();
        //lấy số lượng từ EditText
        hideKeyboard(this);
        int n=Integer.parseInt(s);
        task = new MyAsyncTask(this);
        task.execute(n);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void initViewsNew() {

    }

    private void listenerHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_UPDATE_NUMBER:
                        mIscounting = true;
                        mTextNumber.setText(String.valueOf(msg.arg1));
                        break;
                    case MSG_UPDATE_NUMBER1:
                        mIscounting1 = true;
                        mTextNumber1.setText(String.valueOf(msg.arg1));
                        break;
                    case MSG_UPDATE_NUMBER_DONE:
                        mIscounting = false;
                        mTextNumber.setText("OK");
                        break;
                    case MSG_UPDATE_NUMBER_DONE1:
                        mIscounting1 = false;
                        mTextNumber1.setText("OK");
                        break;
                }
            }
        };
    }


    private void initViews() {
        mTextNumber = findViewById(R.id.text_number);
        mTextNumber1 = findViewById(R.id.text_number1);
        mProgressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.button_count).setOnClickListener(this);
        findViewById(R.id.button_start).setOnClickListener(this);
    }

    public void createLayout() {
        linearLayoutRoot = new LinearLayout(this);
        linearLayoutRoot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayoutRoot.setOrientation(LinearLayout.VERTICAL);

        LinearLayout lnSub1 = new LinearLayout(this);
        lnSub1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lnSub1.setOrientation(LinearLayout.HORIZONTAL);

        Button btDraw = new Button(this);
        btDraw.setText("Draw Button");
        btDraw.setAllCaps(true);
        LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay.gravity = Gravity.TOP;
        btDraw.setLayoutParams(lay);

        final EditText edt = new EditText(this);
        edt.setHint("Nhap so can ve");
        edt.setHintTextColor(Color.RED);
        LinearLayout.LayoutParams lay1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay1.gravity = Gravity.TOP;
        edt.setLayoutParams(lay1);

        lnSub1.addView(btDraw);
        lnSub1.addView(edt);

        LinearLayout lnSub2 = new LinearLayout(this);
        lnSub2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lnSub2.setOrientation(LinearLayout.VERTICAL);

        ListView lst = new ListView(this);
        LinearLayout.LayoutParams lay3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay3.gravity = Gravity.TOP;
        lst.setLayoutParams(lay3);
        lnSub2.addView(lst);

        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, arr);
        lst.setAdapter(adapter);

        linearLayoutRoot.addView(lnSub1);
        linearLayoutRoot.addView(lnSub2);

        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int so = Integer.parseInt(edt.getText()+"");

                final Random rd=new Random();
                Log.d("abc","So = " + so);
                ab.set(false);
                //Tao tien trinh con
                 Thread thCon = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0 ; i < so && ab.get() ; i++) {
                            SystemClock.sleep(150);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("abc","Tesst=== ");
                                    arr.add(rd.nextInt(100));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("abc","Xong === ");
                                Log.d("abc", "Size = " + arr.size());
                                Toast.makeText(MainActivity.this, "Xong rồi đó....", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                ab.set(true);
                thCon.start();
            }
        });
    }

    private void countNumber() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                for (int i =0 ; i < 10 ; i++) {
                    Message message = new Message();
                    Message message1 = mHandler.obtainMessage();
                    message1.arg1 = i;

                    message.what = MSG_UPDATE_NUMBER;
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
/*                    int i = 0;
                    while (i < 10) {
                        Message message = new Message();
                        message.what = MSG_UPDATE_NUMBER;
                        message.arg1 = i;
                        mHandler.sendMessage(message);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                        if (i == 10)
                            i = 0;

                    }*/
                    mHandler.sendEmptyMessage(MSG_UPDATE_NUMBER_DONE);
                }
            }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void countNumber1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =0 ; i < 30 ; i++) {
                    Message message = new Message();
                    message.what = MSG_UPDATE_NUMBER1;
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(MSG_UPDATE_NUMBER_DONE1);
            }
        }).start();
    }

    ProgressAsyncTask mTest = new ProgressAsyncTask();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_count:
/*
                if (!mIscounting) {
                    countNumber();
                }
*/
                mTest.execute();
                break;
            case R.id.button_start:
               /* if (!mIscounting1) {
                    countNumber1();
                }*/
               //new ProgressAsyncTask().execute();
               mTest.cancel(true);
                break;
            default:
                break;
        }
    }


    class ProgressAsyncTask extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... voids) {
            //while (!isCancelled()) {
                for (int i = 0; i <= 100; i++) {
                    publishProgress(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (isCancelled())
                        return null;
                }
                return "DONE";
            //}
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onCancelled() {
            //super.onCancelled();
            Toast.makeText(MainActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
    }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();        }
    }
}
