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

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Stack;

import my.edu.um.fsktm.unihelp.MainActivity;
import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.util.Database;
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

            String query = String.format(
                Locale.US,
                "SELECT id, email, password\n" +
                       "  FROM user\n" +
                       " WHERE email = '%s'\n" +
                       "   AND password = '%s'",
                email, password
            );
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        if (data.length() == 0) {
                            Toast.makeText(
                                LoginActivity.this,
                                "Incorrect Credential",
                                Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            JSONObject user = data.getJSONObject(0);
                            String id = user.getString("0");
                            Preferences.setLogin(LoginActivity.this, id);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        Log.e("LA 77", e.toString());
                    }
                }
            };
            Database.sendQuery(LoginActivity.this, query, listener);
        }
    };

    private View.OnClickListener clearFocus = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (NullPointerException e) { Log.e("LA 61", e.toString()); }
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
}
