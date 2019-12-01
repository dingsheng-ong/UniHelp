package my.edu.um.fsktm.unihelp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Stack;

import my.edu.um.fsktm.unihelp.MainActivity;
import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.util.Hash;
import my.edu.um.fsktm.unihelp.util.Message;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class LoginActivity extends AppCompatActivity {

    private View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText
                etEmail = findViewById(R.id.et_email),
                etPassword = findViewById(R.id.et_password);

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            password = Hash.compute(password, "MD5");

            if (email.isEmpty()) {
                etEmail.setError("Please fill in your email");
                return;
            }

            Message loginResult = login(email, password);
            if (loginResult == Message.LOGIN_SUCCESSFUL) {
                Preferences.setLogin(LoginActivity.this, email);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } else {
                Toast.makeText(
                    LoginActivity.this,
                    loginResult.toString(),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    };

    private View.OnClickListener clearFocus = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (NullPointerException e) { Log.e("Error", e.toString()); }
            Stack<View> stack = new Stack<>();
            stack.push(findViewById(R.id.login_bg));
            while (!stack.isEmpty()) {
                if (stack.peek() instanceof ViewGroup) {
                    ViewGroup vg = (ViewGroup) stack.pop();
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        View v = vg.getChildAt(i);
                        stack.push(v);
                    }
                }
                else if (stack.peek() instanceof EditText) {
                    stack.pop().clearFocus();
                }
                else { stack.pop(); }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        findViewById(R.id.btn_login).setOnClickListener(login);
        findViewById(R.id.login_bg).setOnClickListener(clearFocus);
    }

    private Message login(String email, String password) {
        // TO-DO: fetch credential from database and login
        if (email.equals("admin@mail.com") && password.equals("098f6bcd4621d373cade4e832627b4f6")) {
            return Message.LOGIN_SUCCESSFUL;
        } else {
            return Message.PASSWORD_INCORRECT;
        }
    }
}
