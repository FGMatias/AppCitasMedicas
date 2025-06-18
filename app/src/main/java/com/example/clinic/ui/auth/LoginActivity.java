package com.example.clinic.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.clinic.MainActivity;
import com.example.clinic.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private boolean isPasswordVisible = false;
    private Button login;
    private SharedPreferences sharedPreferences;
    TextView forgotPassword;
    final String servidor = "http://10.0.2.2/clinic/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("session_user", MODE_PRIVATE);

        setupRegisterLink();

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() -
                            password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() -
                            password.getPaddingRight())) {

                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock, 0, R.drawable.ic_eye_close, 0);
            isPasswordVisible = false;
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            password.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock, 0, R.drawable.ic_eye_open, 0);
            isPasswordVisible = true;
        }
        password.setSelection(password.getText().length());
    }

    private void setupRegisterLink() {
        TextView tvRegisterLink = findViewById(R.id.tvRegisterLink);
        String text = "¿No tienes una cuenta? Crear Cuenta";
        SpannableString spannableString = new SpannableString(text);

        int startPos = text.indexOf("Crear");
        int secondWordPos = text.indexOf("Cuenta", startPos);

        if (startPos != -1 && secondWordPos != -1) {
            int colorPrimary = ContextCompat.getColor(this, R.color.red_500);

            spannableString.setSpan(
                new ForegroundColorSpan(colorPrimary),
                startPos,
                secondWordPos + "Cuenta".length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(colorPrimary);
                    ds.setUnderlineText(false);
                }
            };

            spannableString.setSpan(
                clickableSpan,
                startPos,
                secondWordPos + "Cuenta".length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            tvRegisterLink.setText(spannableString);
            tvRegisterLink.setMovementMethod(LinkMovementMethod.getInstance());
            tvRegisterLink.setHighlightColor(0);
        }
    }

    @Override
    public void onClick(View v) {
        String correo = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (correo.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        iniciarSesion(correo, pass);
    }

    private void iniciarSesion(String correo, String pass) {
        String url = servidor + "login.php";

        RequestParams params = new RequestParams();
        params.put("email", correo);
        params.put("password", pass);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject json = new JSONObject(response);

                    boolean success = json.getBoolean("success");
                    String message = json.getString("message");

                    if (success) {
                        JSONObject data = json.getJSONObject("data");
                        int id_paciente = data.getInt("id_paciente");
                        String nombre = data.getString("nombre");
                        String apellido = data.getString("apellido");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("logged", true);
                        editor.putInt("id_paciente", id_paciente);
                        editor.putString("nombre", nombre);
                        editor.putString("apellido", apellido);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Bienvenido " + nombre, Toast.LENGTH_LONG).show();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(LoginActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

}