package com.example.clinic.ui.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.clinic.R;

import java.util.List;

public class AppointmentAdapter extends BaseAdapter {

    private Context context;
    private List<Appointment> citas;

    public AppointmentAdapter(Context context, List<Appointment> citas) {
        this.context = context;
        this.citas = citas;
    }

    @Override
    public int getCount() {
        return citas.size();
    }

    @Override
    public Object getItem(int position) {
        return citas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_appointment, parent, false);
        }

        // Obtener los datos del contacto
        Appointment cita = citas.get(position);

        // Referenciar las vistas
        TextView tvIdCita = convertView.findViewById(R.id.tvIdCita);
        TextView tvIdPaciente = convertView.findViewById(R.id.tvIdPaciente);
        TextView tvIdDoctor = convertView.findViewById(R.id.tvIdDoctor);
        TextView tvIdEstado = convertView.findViewById(R.id.tvIdEstado);
        TextView tvPaciente = convertView.findViewById(R.id.tvPaciente);
        TextView tvDoctor = convertView.findViewById(R.id.tvDoctor);
        TextView tvEspecialidad = convertView.findViewById(R.id.tvEspecialidad);
        TextView tvFecha = convertView.findViewById(R.id.tvFecha);
        TextView tvHora = convertView.findViewById(R.id.tvHora);
        TextView tvEstado = convertView.findViewById(R.id.tvEstado);

        // Establecer los valores
        tvIdCita.setText(cita.getIdCita().toString());
        tvIdPaciente.setText(cita.getIdPaciente().toString());
        tvIdDoctor.setText(cita.getIdDoctor().toString());
        tvIdEstado.setText(cita.getEstado().toString());
        tvPaciente.setText("Paciente: " + cita.getPaciente());
        tvDoctor.setText("Doctor: " +cita.getDoctor());
        tvEspecialidad.setText("Especialidad: " + cita.getEspecialidad());
        tvFecha.setText("Fecha: " + cita.getFecha());
        tvHora.setText("Hora:" + cita.getHora());
        tvEstado.setText("Estado:" + cita.getEstadoCita());

        return convertView;
    }
}
