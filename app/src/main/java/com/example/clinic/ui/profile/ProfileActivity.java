package com.example.clinic.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.clinic.MainActivity;
import com.example.clinic.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, lastName, phone, documentNumber;
    private Button edit, back;
    private int idPaciente;
    final String servidor = "http://10.0.2.2/clinic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        name = (EditText) findViewById(R.id.etName);
        lastName = (EditText) findViewById(R.id.etLastName);
        phone = (EditText) findViewById(R.id.etEmail);
        documentNumber = (EditText) findViewById(R.id.etPassword);

        edit = (Button) findViewById(R.id.btnEdit);
        edit.setOnClickListener(this);

        back = (Button) findViewById(R.id.btnBack);
        back.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("session_user", MODE_PRIVATE);
        idPaciente = prefs.getInt("id_paciente", -1);

        if (idPaciente != -1) {
            obtenerUsuario(idPaciente);
        } else {
            Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void obtenerUsuario(int idPaciente) {
        String url = servidor + "obtener_usuario.php";

        RequestParams params = new RequestParams();
        params.put("id_paciente", idPaciente);

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
                        String telefono = data.optString("telefono", "");
                        String dni = data.optString("dni", "");

                        name.setText(data.getString("nombre"));
                        lastName.setText(data.getString("apellido"));
                        if (data.getString("telefono") == null) {
                            phone.setText("");
                            phone.setHint("Telefono");
                        } else {
                            phone.setText(data.getString("telefono"));
                        }

                        if (data.getString("dni") == null) {
                            documentNumber.setText("");
                            documentNumber.setHint("Número de Documento");
                        } else {
                            documentNumber.setText(data.getString("dni"));
                        }

                    } else {
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ProfileActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ProfileActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if ( v == edit) {
            String nombre = name.getText().toString().trim();
            String apellido = lastName.getText().toString().trim();
            String telefono = phone.getText().toString().trim();
            String numeroDocumento = documentNumber.getText().toString().trim();

            if (nombre.isEmpty() ||
                apellido.isEmpty() ||
                telefono.isEmpty() ||
                numeroDocumento.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            editarUsuario(nombre, apellido, telefono, numeroDocumento);
        } else if ( v == back ) {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }

    private void editarUsuario(String nombre, String apellido, String telefono, String numeroDocumento) {
        String url = servidor + "editar_usuario.php";

        RequestParams params = new RequestParams();
        params.put("id_paciente", idPaciente);
        params.put("nombres", nombre);
        params.put("apellidos", apellido);
        params.put("telefono", telefono);
        params.put("numeroDocumento", numeroDocumento);

        Log.d("PARAMS_DEBUG", "id_paciente: " + idPaciente);
        Log.d("PARAMS_DEBUG", "nombres: " + nombre);
        Log.d("PARAMS_DEBUG", "apellidos: " + apellido);
        Log.d("PARAMS_DEBUG", "telefono: " + telefono);
        Log.d("PARAMS_DEBUG", "dni: " + numeroDocumento);

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
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();

                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ProfileActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ProfileActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}