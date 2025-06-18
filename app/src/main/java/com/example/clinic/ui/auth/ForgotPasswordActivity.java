package com.example.clinic.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clinic.MainActivity;
import com.example.clinic.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;
    private EditText email, password, confirmPassword;
    private Button change;
    private HashMap<EditText, Boolean> passwordVisibilityStates = new HashMap<>();
    final String servidor = "http://10.0.2.2/clinic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        back = (ImageButton) findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        confirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        passwordVisibilityStates.put(password, false);
        passwordVisibilityStates.put(confirmPassword, false);

        setupPasswordToggle(password);
        setupPasswordToggle(confirmPassword);

        change = (Button) findViewById(R.id.btnChange);
        change.setOnClickListener(this);
    }

    private void setupPasswordToggle(EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() -
                            editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() -
                            editText.getPaddingRight())) {

                        togglePasswordVisibility(editText);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void togglePasswordVisibility(EditText editText) {
        boolean isCurrentlyVisible = passwordVisibilityStates.get(editText);

        if (isCurrentlyVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock, 0, R.drawable.ic_eye_close, 0);
            passwordVisibilityStates.put(editText, false);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock, 0, R.drawable.ic_eye_open, 0);
            passwordVisibilityStates.put(editText, true);
        }

        editText.setSelection(editText.getText().length());
    }

    @Override
    public void onClick(View v) {
        String correo = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (correo.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        cambiarContrasena(correo, pass);
    }

    private void cambiarContrasena(String correo, String pass) {
        String url = servidor + "cambiar_contrasena.php";

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
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();

                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ForgotPasswordActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ForgotPasswordActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}