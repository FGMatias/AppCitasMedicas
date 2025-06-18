package com.example.clinic.ui.appointment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clinic.R;
import com.example.clinic.ui.Item;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class AppointmentAdd extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    final String servidor = "http://10.0.2.2/clinic/";
    int idEspecialidad = -1;
    int idDoctor = -1;
    private ArrayList<String> diasPermitidos = new ArrayList<>();
    private String horaInicioGlobal = "";
    private String horaFinGlobal = "";
    private TextView fecha;
    private EditText motivo;
    private Spinner especialidad, doctor, hora;
    private Button agendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment_schedule, container, false);

        motivo = rootView.findViewById(R.id.etMotivo);
        fecha = rootView.findViewById(R.id.tvFecha); // es un TextView, simula DatePicker
        especialidad = rootView.findViewById(R.id.spEspecialidad);
        doctor = rootView.findViewById(R.id.spDoctor);
        hora = rootView.findViewById(R.id.spHora); // Spinner de hora
        agendar = rootView.findViewById(R.id.btnAgendar);

        especialidad.setOnItemSelectedListener(this);
        doctor.setOnItemSelectedListener(this);
        agendar.setOnClickListener(this);

        ObtenerEspecialidades();

        fecha.setOnClickListener(v -> mostrarDatePicker());

        return rootView;
    }

    private void ObtenerEspecialidades() {
        String url = servidor + "especialidad_mostrar.php";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONArray jsonArray = json.getJSONArray("data");

                    ArrayList<Item> lista = new ArrayList<>();
                    lista.add(new Item(-1, "Seleccione Especialidad"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.getInt("id_especialidad");
                        String nombre = obj.getString("nombre_especialidad");

                        lista.add(new Item(id, nombre));
                    }

                    ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            lista
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    especialidad.setAdapter(adapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void ObtenerDoctores(Integer idEspecialidad) {
        String url = servidor + "doctor_mostrar.php";

        RequestParams params = new RequestParams();
        params.put("id_especialidad", idEspecialidad);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONArray jsonArray = json.getJSONArray("data");

                    ArrayList<Item> lista = new ArrayList<>();
                    lista.add(new Item(-1, "Seleccione un Doctor"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.getInt("id_doctor");
                        String nombre = obj.getString("nombreDoctor");

                        lista.add(new Item(id, nombre));
                    }

                    ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            lista
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    doctor.setAdapter(adapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == agendar) {
            String fechaSeleccionada = fecha.getText().toString();
            String horaSeleccionada = hora.getSelectedItem().toString();
            String motivoConsulta = motivo.getText().toString();

            if (idEspecialidad == -1 || idDoctor == -1 || fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
                Toast.makeText(getActivity(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getActivity().getSharedPreferences("session_user", Context.MODE_PRIVATE);
            int idPaciente = prefs.getInt("id_paciente", -1);

            if (idPaciente == -1) {
                Toast.makeText(getActivity(), "Sesión no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Confirmar")
                    .setMessage("¿Deseas agendar esta cita?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        RegistrarCita(idPaciente, idDoctor, fechaSeleccionada, horaSeleccionada, motivoConsulta);
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }

    private void RegistrarCita(int idPaciente, int idDoctor, String fechaSeleccionada, String horaSeleccionada, String motivoConsulta) {
        String url = servidor + "cita_agendar.php";

        //Enviar parámetros
        RequestParams requestParams = new RequestParams();
        requestParams.put("id_paciente", idPaciente);
        requestParams.put("id_doctor", idDoctor);
        requestParams.put("fecha", fechaSeleccionada);
        requestParams.put("hora", horaSeleccionada);
        requestParams.put("motivo_consulta", motivoConsulta);

        //Envio al web service y respuesta
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                Toast.makeText(getActivity(), "Respuesta: " + respuesta, Toast.LENGTH_LONG).show();

                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.nav_appointment);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String mensaje = "Error: " + statusCode + " - " + error.getMessage();
                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LimpiarCampos() {
        motivo.setText("");
        fecha.setText("Selecciona una fecha");
        hora.setAdapter(null);
        especialidad.setSelection(0);
        doctor.setAdapter(null);
        idEspecialidad = -1;
        idDoctor = -1;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == especialidad) {
            Item itemSeleccionado = (Item) parent.getItemAtPosition(position);
            idEspecialidad = itemSeleccionado.getId();

            if (idEspecialidad != -1) {
                ObtenerDoctores(idEspecialidad);
            }
        } else if(parent == doctor) {
            Item itemSeleccionado = (Item) parent.getItemAtPosition(position);
            idDoctor = itemSeleccionado.getId();
            ObtenerHorariosDoctor(idDoctor);
        }
    }

    private void ObtenerHorariosDoctor(int idDoctor) {
        String url = servidor + "horario_doctor_mostrar.php";
        RequestParams params = new RequestParams();
        params.put("id_doctor", idDoctor);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getBoolean("success")) {
                        JSONArray horarios = json.getJSONArray("data");
                        diasPermitidos.clear();
                        for (int i = 0; i < horarios.length(); i++) {
                            JSONObject h = horarios.getJSONObject(i);
                            diasPermitidos.add(h.getString("dia_semana"));
                            horaInicioGlobal = h.getString("hora_inicio");
                            horaFinGlobal = h.getString("hora_fin");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private void mostrarDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            int diaSemana = selected.get(Calendar.DAY_OF_WEEK);

            String diaTexto = convertirDia(diaSemana); // Ej: "Lunes"

            if (!diasPermitidos.contains(diaTexto)) {
                Toast.makeText(getActivity(), "Este día no está disponible para el doctor", Toast.LENGTH_SHORT).show();
            } else {
                String fechaFormateada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                fecha.setText(fechaFormateada);
                cargarHorasDisponibles(horaInicioGlobal, horaFinGlobal);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private String convertirDia(int dia) {
        switch (dia) {
            case Calendar.SUNDAY: return "Domingo";
            case Calendar.MONDAY: return "Lunes";
            case Calendar.TUESDAY: return "Martes";
            case Calendar.WEDNESDAY: return "Miércoles";
            case Calendar.THURSDAY: return "Jueves";
            case Calendar.FRIDAY: return "Viernes";
            case Calendar.SATURDAY: return "Sábado";
            default: return "";
        }
    }

    private void cargarHorasDisponibles(String inicio, String fin) {
        ArrayList<String> horas = new ArrayList<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date horaIni = sdf.parse(inicio);
            Date horaFin = sdf.parse(fin);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(horaIni);

            while (calendar.getTime().before(horaFin)) {
                String horaActual = sdf.format(calendar.getTime());
                horas.add(horaActual);
                calendar.add(Calendar.MINUTE, 30); // cada 30 minutos
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, horas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hora.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}




