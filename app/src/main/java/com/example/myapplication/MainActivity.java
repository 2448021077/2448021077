package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
private Button button_log;
private Button button_lnd;
private EditText et_1;
private EditText et_2;
    String username;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_1 =findViewById(R.id.et_1);
        et_2=findViewById(R.id.et_2);
        button_log=findViewById(R.id.btn_2);
        button_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        button_lnd =findViewById(R.id.btn_1);
        button_lnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_1:
                         username=et_1.getText().toString();
                         password=et_2.getText().toString();
                        sendRequestWithOkHttp();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient cient=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("username",username)
                            .add("password",password)
                            .build();
                    Request request =new Request.Builder()
                            .url("http://124.222.59.40:7000/user/login")
                            .post(requestBody)
                            .build();
                    Response response=cient.newCall(request).execute();
                    String responseData =response.body().string();
                    Looper.prepare();
                    parseJsonwithJsonobject(responseData);
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJsonwithJsonobject(String jsonData) throws JSONException {
        JSONObject jsonObject=new JSONObject(jsonData);
        String call=jsonObject.getString("msg");
        try {
            AlertDialog.Builder dialog =new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("系统提示");
            dialog.setMessage(call);
            dialog.setCancelable(false);
            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}