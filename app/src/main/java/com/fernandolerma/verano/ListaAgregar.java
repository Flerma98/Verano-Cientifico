package com.fernandolerma.verano;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.fernandolerma.verano.Adaptadores.RecyclerAgregar;
import com.fernandolerma.verano.Adaptadores.RecyclerSensores;
import com.fernandolerma.verano.Clases.Sensores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ListaAgregar extends AppCompatActivity {

    ArrayList<Sensores> lista= new ArrayList<>();
    RecyclerView rv_Sensores;
    RecyclerAgregar adapter;
    FloatingActionButton fabAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agregar);
        setTitle("Agregar Sensores");

        rv_Sensores= findViewById(R.id.rv_lista);
        fabAtras= findViewById(R.id.fab_atras);
        rv_Sensores.setLayoutManager(new GridLayoutManager(ListaAgregar.this, 1));
        adapter = new RecyclerAgregar(lista, ListaAgregar.this, rv_Sensores);
        rv_Sensores.setAdapter(adapter);

        fabAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            Menu.myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lista.clear();
                    for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        Sensores sensor = objSnaptshot.getValue(Sensores.class);
                        lista.add(sensor);
                    }
                    if (lista.isEmpty()) { rv_Sensores.setVisibility(View.GONE); }else{ rv_Sensores.setVisibility(View.VISIBLE); }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }catch (Exception e){
            Toast.makeText(ListaAgregar.this, "Ocurrió un error obteniendo los datos", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busqueda, menu);
        MenuItem searchItem= menu.findItem(R.id.busqueda);
        SearchView searchView= (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}
