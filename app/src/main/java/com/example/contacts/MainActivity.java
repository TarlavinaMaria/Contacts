package com.example.contacts;

import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    Button btnRead;
    Button btnClear;

    EditText editText;
    EditText editText1;

    TextView tvUsers;

    SharedPreferences sPref;

    AppDatabase db;
    UserDao userDao;
    String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnClear = findViewById(R.id.btnClear);
        editText = findViewById(R.id.etName);
        editText1 = findViewById(R.id.etEmail);
        tvUsers = findViewById(R.id.tvUsers);

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                String email = editText1.getText().toString();
                Log.d("TAG","--- Insert in users: ----");
                Executors.newSingleThreadExecutor().execute(()->{
                    userDao.insert(new User(name,email));
                });
                Log.d("TAG","--- User added: "+name+", "+email);
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","--- Row in users: ----");

                Executors.newSingleThreadExecutor().execute(()->{
                    List<User> users = userDao.getAllUsers();
                    String[] userStrings = new String[users.size()];
                    for(int i = 0; i < users.size(); i++){
                        User user = users.get(i);
                        userStrings[i] = "ID: " + user.id + ", Name: " + user.name + ", Email: " + user.email;
                    }
                    String usersText = String.join("\n", userStrings);
                    runOnUiThread(() -> {
                        tvUsers.setText(usersText);
                    });
                });
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","--- Clear users: ----");
                Executors.newSingleThreadExecutor().execute(()->{
                    userDao.clearAll();
                    runOnUiThread(() -> {
                        tvUsers.setText("");
                    });
                    Log.d("TAG","Все юзеры удалены");
                });
            }
        });

        Log.d("TAG","onCreate");
    }

    private void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String load = sPref.getString(SAVED_TEXT,"");
        editText.setText(load);
        Toast.makeText(this, "Текст загружен", Toast.LENGTH_SHORT).show();
    }

    private void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT,editText.getText().toString());
        ed.commit();
        Toast.makeText(this, "Текст сохранен", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG","onDestroy");
    }
}