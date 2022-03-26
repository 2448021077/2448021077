package com.example.myapplication;

import android.content.DialogInterface;
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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button button_log;
    private EditText et_3;
    private EditText et_4;
    private EditText et_5;
    private EditText et_6;
    String username;
    String password;
    String Cpassword;
    String invate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter);
        button_log=findViewById(R.id.btn_3);
        et_3=findViewById(R.id.et_3);
        et_4=findViewById(R.id.et_4);
        et_5=findViewById(R.id.et_5);
        et_6=findViewById(R.id.et_6);
        button_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_3:
                        username=et_3.getText().toString();
                        password=et_4.getText().toString();
                        Cpassword=et_5.getText().toString();
                        invate=et_6.getText().toString();
                        if (password.equals(Cpassword)){
                            sendRequestWithOkHttp();
                        }else{
                            Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
                        }
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
                    OkHttpClient okHttpClient=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("icode",invate)
                            .add("username",username)
                            .add("password",password)
                            .build();
                    Request request =new Request.Builder()
                            .url("http://124.222.59.40:7000/user/register")
                            .post(requestBody)
                            .build();
                    Response response=okHttpClient.newCall(request).execute();
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
            AlertDialog.Builder dialog =new AlertDialog.Builder(RegisterActivity.this);
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