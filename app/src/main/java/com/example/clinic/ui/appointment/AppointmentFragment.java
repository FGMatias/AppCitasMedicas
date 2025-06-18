package com.example.clinic.ui.appointment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.clinic.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class AppointmentFragment extends Fragment implements View.OnClickListener {

    ListView lista;
    Button agendar;

    final String servidor = "http://10.0.2.2/clinic/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        lista = (ListView) rootView.findViewById(R.id.lstCitas);
        agendar = (Button) rootView.findViewById(R.id.btnAgendarCita);
        agendar.setOnClickListener(this);

        MostrarDatos();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if( v == agendar ) {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_appointment_to_nav_appointment_schedule);
        }
    }

    private void MostrarDatos() {
        String url = servidor + "cita_mostrar.php";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session_user", getContext().MODE_PRIVATE);
        int idPaciente = sharedPreferences.getInt("id_paciente", -1);

        if (idPaciente == -1) {
            Toast.makeText(getActivity(), "Error: Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("id_paciente", idPaciente);

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject json = new JSONObject(response);

                    boolean success = json.getBoolean("success");
                    String message  = json.getString("message");

                    ArrayList<Appointment> citalist = new ArrayList<>();

                    if (success) {
                        // 4. Leer el array "data"
                        JSONArray dataArray = json.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject citaJson = dataArray.getJSONObject(i);

                            int idCita = citaJson.getInt("idCita");
                            int idPaciente = citaJson.getInt("idPaciente");
                            int idDoctor = citaJson.getInt("idDoctor");
                            String paciente = citaJson.getString("paciente");
                            String doctor = citaJson.getString("doctor");
                            String especialidad = citaJson.getString("especialidad");
                            String fecha = citaJson.getString("fecha");
                            String hora = citaJson.getString("hora");
                            int estado = citaJson.getInt("estado");
                            String estadoCita = citaJson.getString("estadoCita");
                            String motivo = citaJson.getString("motivoConsulta");

                            citalist.add(new Appointment(
                                motivo,
                                estadoCita,
                                estado,
                                hora,
                                fecha,
                                especialidad,
                                doctor,
                                paciente,
                                idPaciente,
                                idCita,
                                idDoctor
                            ));
                        }

                        Log.d("CITAS", "Total citas recibidas: " + citalist.size());

                    } else {
                        // Si no hay citas, solo muestra el mensaje
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }

                    // 5. Cargar el adaptador (lista vacía o con datos)
                    AppointmentAdapter adapter = new AppointmentAdapter(getActivity(), citalist);
                    lista.setAdapter(adapter);

                } catch (JSONException e) {
                    Toast.makeText(getActivity(),
                            "Error al procesar datos del servidor",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }


}