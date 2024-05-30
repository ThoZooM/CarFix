package com.mirea.kt.ribo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private String server = "https://android-for-students.ru";
    private String serverPath = "/coursework/login.php";
    private EditText loginEdT,passwordEdT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEdT = findViewById(R.id.loginTextField);
        passwordEdT = findViewById(R.id.passwordTextField);
        loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(v->{
            String login = loginEdT.getText().toString();
            String password = passwordEdT.getText().toString();
            if (login != null && password!= null &&!login.isEmpty() && !password.isEmpty()) {
                HashMap<String, String> map = new HashMap();
                map.put("lgn", login);
                map.put("pwd", password);
                map.put("g", "RIBO-01-22");
                HTTPRunnable httpRunnable = new HTTPRunnable(server + serverPath, map);
                Thread th = new Thread(httpRunnable);
                th.start();
                try {
                    th.join();
                } catch (InterruptedException e) {
                } finally {
                    try {
                        String response = httpRunnable.getResponseBody();
                        if (response != null) {
                            JSONObject jsonObject = new JSONObject(response);
                            int result = jsonObject.getInt("result_code");
                            if (result == 1) {
                                String titleTask = jsonObject.getString("title");
                                String taskText = jsonObject.getString("task");
                                Log.d("authorization","Успешная авторизация\n"+"ответ от сервера" +
                                        ": "+jsonObject.toString());
//                                System.out.println("Result: " + result);
//                                System.out.println("Json: " + jsonObject);
                                LoginDialog dialog = new LoginDialog(titleTask,taskText);
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                dialog.show(transaction,"login_dialog");
                            }else{
                                Toast.makeText(this,"Неверный логин или пароль",Toast.LENGTH_LONG).show();
                                Log.d("authorization_error","Неверный логин или пароль");
                            }
                        }else{
                            Toast.makeText(this,"Отсутвует подключение к интернету",Toast.LENGTH_LONG).show();
                            Log.d("authorization_error","Отсутвует подключение к интернету");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else{
                Toast.makeText(this,"Введите логин и пароль!",Toast.LENGTH_LONG).show();
                Log.d("authorization_error","Поле с логином или паролем пустое");
            }
        });
    }
}