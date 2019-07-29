package com.fernandolerma.verano.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandolerma.verano.Clases.Sensores;
import com.fernandolerma.verano.Menu;
import com.fernandolerma.verano.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RecyclerAgregar extends RecyclerView.Adapter<RecyclerAgregar.RecyclerViewHolder> implements View.OnClickListener, Filterable {

    private ArrayList<Sensores> sensores_source;
    public  ArrayList<Sensores> sensores_filtrados;
    private View.OnClickListener listener;
    private Context mContext;
    private View view;

    public RecyclerAgregar(ArrayList<Sensores> sensores, Context mContext, View view) {
        this.sensores_source = sensores;
        this.sensores_filtrados = sensores;
        this.mContext = mContext;
        this.view = view;
    }

    @NonNull
    @Override
    public RecyclerAgregar.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_agregar, viewGroup, false);
        RecyclerAgregar.RecyclerViewHolder holder= new RecyclerAgregar.RecyclerViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAgregar.RecyclerViewHolder recyclerViewHolder, int i) {
        final Sensores sensor= sensores_filtrados.get(i);
        recyclerViewHolder.txtNombre.setText(sensor.getNombre());
        recyclerViewHolder.txtTemperatura.setText("Temperatura: " + sensor.getTemperatura() + "°C");
        boolean encontrado= false;
        StringTokenizer st = new StringTokenizer(sensor.getUsuarios(), ";");
        while (st.hasMoreTokens()) {
            String Usuario = st.nextToken();
            if(Usuario.equals(Menu.Auth.getUid())) {
                encontrado = true;
            }
        }
        if(encontrado){
            recyclerViewHolder.btnAgregar.setEnabled(false);
            recyclerViewHolder.btnAgregar.setText("Agregado");
        }else{
            recyclerViewHolder.btnAgregar.setEnabled(true);
            recyclerViewHolder.btnAgregar.setText("Agregar");
        }

        recyclerViewHolder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sensor.getUsuarios().length()>0){
                    Menu.myRef.child(sensor.getNombre()).child("usuarios").setValue(sensor.getUsuarios() + ";" + Menu.Auth.getUid());
                }else{
                    Menu.myRef.child(sensor.getNombre()).child("usuarios").setValue(Menu.Auth.getUid());
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString().trim();
                if(searchString.isEmpty()){
                    sensores_filtrados= sensores_source;
                }else{
                    ArrayList<Sensores> resultList= new ArrayList<>();
                    for(Sensores item: sensores_source){
                        if(item.getNombre().toLowerCase().contains(searchString.toLowerCase())){
                            resultList.add(item);
                        }
                    }
                    sensores_filtrados= resultList;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= sensores_filtrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sensores_filtrados= (ArrayList<Sensores>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombre, txtTemperatura;
        Button btnAgregar;

        public RecyclerViewHolder(View itemView){
            super(itemView);
            txtNombre= itemView.findViewById(R.id.rv_txtNombre);
            txtTemperatura= itemView.findViewById(R.id.rv_txtTemperatura);
            btnAgregar= itemView.findViewById(R.id.rv_btnAgregar);
        }
    }
}
