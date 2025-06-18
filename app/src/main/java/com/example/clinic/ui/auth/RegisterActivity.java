package com.example.clinic.ui.auth;

import android.content.Intent;
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

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, lastName, email, password, confirmPassword;
    private Button register;
    private HashMap<EditText, Boolean> passwordVisibilityStates = new HashMap<>();
    final String servidor = "http://10.0.2.2/clinic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        setupLoginLink();

        name = (EditText) findViewById(R.id.etName);
        lastName = (EditText) findViewById(R.id.etLastName);
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        confirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        passwordVisibilityStates.put(password, false);
        passwordVisibilityStates.put(confirmPassword, false);

        setupPasswordToggle(password);
        setupPasswordToggle(confirmPassword);

        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);
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

    private void setupLoginLink() {
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        String text = "¿Ya tienes una cuenta? Iniciar Sesión";
        SpannableString spannableString = new SpannableString(text);

        int startPos = text.indexOf("Iniciar");
        int secondWordPos = text.indexOf("Sesión", startPos);

        if (startPos != -1 && secondWordPos != -1) {
            int colorPrimary = ContextCompat.getColor(this, R.color.red_500);

            spannableString.setSpan(
                new ForegroundColorSpan(colorPrimary),
                startPos,
                secondWordPos + "Sesión".length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                secondWordPos + "Sesión".length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            tvLoginLink.setText(spannableString);
            tvLoginLink.setMovementMethod(LinkMovementMethod.getInstance());
            tvLoginLink.setHighlightColor(0);
        }
    }

    @Override
    public void onClick(View v) {
        String nombre = name.getText().toString().trim();
        String apellido = lastName.getText().toString().trim();
        String correo = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        registrarUsuario(nombre, apellido, correo, pass);
    }

    private void registrarUsuario(String nombre, String apellido, String correo, String pass) {
        String url = servidor + "registrar_usuario.php";

        RequestParams params = new RequestParams();
        params.put("nombres", nombre);
        params.put("apellidos", apellido);
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
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegisterActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}