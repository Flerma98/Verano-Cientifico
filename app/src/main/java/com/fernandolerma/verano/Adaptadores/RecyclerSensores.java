package com.fernandolerma.verano.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandolerma.verano.Clases.Sensores;
import com.fernandolerma.verano.Menu;
import com.fernandolerma.verano.R;

import java.util.ArrayList;

public class RecyclerSensores extends RecyclerView.Adapter<RecyclerSensores.RecyclerViewHolder> implements View.OnClickListener {

    private ArrayList<Sensores> sensores_source;
    public  ArrayList<Sensores> sensores_filtrados;
    private View.OnClickListener listener;
    private Context mContext;
    private View view;
    private FragmentManager fragmentManager;

    public RecyclerSensores(ArrayList<Sensores> sensores, Context mContext, View view, FragmentManager fragment) {
        this.sensores_source = sensores;
        this.sensores_filtrados = sensores;
        this.listener = listener;
        this.mContext = mContext;
        this.view= view;
        this.fragmentManager= fragment;
    }

    @NonNull
    @Override
    public RecyclerSensores.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rb_sensor, viewGroup, false);
        RecyclerViewHolder holder= new RecyclerViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerSensores.RecyclerViewHolder recyclerViewHolder, int i) {
        final Sensores sensor= sensores_filtrados.get(i);
        recyclerViewHolder.txtNombre.setText(sensor.getNombre());
        recyclerViewHolder.txtTemperatura.setText(sensor.getTemperatura() + "°C");
        recyclerViewHolder.txtHumedad.setText(sensor.getHumedad() + "%");

        recyclerViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerViewHolder.layout.getVisibility()==View.GONE){
                    recyclerViewHolder.layout.setVisibility(View.VISIBLE);
                    recyclerViewHolder.txtsetTemperatura.setText(String.valueOf(sensor.getManejo_temperatura()));
                    recyclerViewHolder.txtsetHumedad.setText(String.valueOf(sensor.getManejo_humedad()));
                }else{
                    recyclerViewHolder.layout.setVisibility(View.GONE);
                }
            }
        });

        recyclerViewHolder.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recyclerViewHolder.txtsetHumedad.getText().toString().isEmpty() && !recyclerViewHolder.txtsetTemperatura.getText().toString().isEmpty()) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(mContext);
                    dialogo1.setTitle("Enviar Dato");
                    dialogo1.setMessage("¿ Desea enviar estos datos ?");
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Menu.myRef.child(sensor.getNombre()).child("manejo_humedad").setValue(Double.parseDouble(recyclerViewHolder.txtsetHumedad.getText().toString()));
                            Menu.myRef.child(sensor.getNombre()).child("manejo_temperatura").setValue(Double.parseDouble(recyclerViewHolder.txtsetTemperatura.getText().toString()));
                            recyclerViewHolder.layout.setVisibility(View.GONE);
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            dialogo1.dismiss();
                        }
                    });
                    dialogo1.show();
                }else{
                    Toast.makeText(mContext, "Introdusca valores validos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sensores_filtrados.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombre, txtTemperatura, txtHumedad, txtsetTemperatura, txtsetHumedad;
        CardView cardView;
        LinearLayout layout;
        Button btnEnviar;

        public RecyclerViewHolder(View itemView){
            super(itemView);
            txtNombre= itemView.findViewById(R.id.txtNombre);
            txtTemperatura= itemView.findViewById(R.id.txtTemperatura);
            txtHumedad= itemView.findViewById(R.id.txtHumedad);
            txtsetTemperatura= itemView.findViewById(R.id.rv_setTemperatura);
            txtsetHumedad= itemView.findViewById(R.id.rv_setHumedad);
            btnEnviar= itemView.findViewById(R.id.rv_btnEnviar);
            cardView= itemView.findViewById(R.id.rv_cardview);
            layout= itemView.findViewById(R.id.lyEscondido);
        }
    }
}
