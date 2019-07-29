package com.fernandolerma.verano;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.fernandolerma.verano.Adaptadores.RecyclerSensores;
import com.fernandolerma.verano.Clases.FireBaseReference;
import com.fernandolerma.verano.Clases.Sensores;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Menu extends AppCompatActivity {

    public static FirebaseAuth Auth = FirebaseAuth.getInstance();
    public static ArrayList<Sensores> lista= new ArrayList<>();
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference myRef = database.getReference(FireBaseReference.REFERENCIASENSORES);

    RecyclerView rv_Sensores;
    FloatingActionButton btnAgregar;
    RecyclerSensores adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("Sensores");

        rv_Sensores= findViewById(R.id.rv_sensores);
        btnAgregar= findViewById(R.id.fab_agregar);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, ListaAgregar.class));
            }
        });

        rv_Sensores.setLayoutManager(new GridLayoutManager(Menu.this, 1));
        adapter = new RecyclerSensores(lista, Menu.this, rv_Sensores, getSupportFragmentManager());
        rv_Sensores.setAdapter(adapter);

        try {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lista.clear();
                    for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        Sensores sensor = objSnaptshot.getValue(Sensores.class);
                        StringTokenizer st = new StringTokenizer(sensor.getUsuarios(), ";");
                        while (st.hasMoreTokens()) {
                            String Usuario = st.nextToken();
                            if(Usuario.equals(Auth.getUid())) {
                                lista.add(sensor);
                                if(sensor.getTemperatura()>sensor.getManejo_temperatura())
                                Notificacion("Alerta de tempertarua", "El Horno " + sensor.getNombre() + " alcanzó una temperatura de " + sensor.getTemperatura() + "°C");
                            }
                        }
                    }
                    if (lista.isEmpty()) { rv_Sensores.setVisibility(View.GONE); }else{ rv_Sensores.setVisibility(View.VISIBLE); }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }catch (Exception e){
            Toast.makeText(Menu.this, "Ocurrió un error obteniendo los datos", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.salir, menu);
        MenuItem ayuda= menu.findItem(R.id.salir);
        ayuda.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Menu.this);
                dialogo1.setTitle("Cerrar Sesión");
                dialogo1.setMessage("¿ Desea cerrar sesión ?");
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Auth.signOut();
                        startActivity(new Intent(Menu.this, MainActivity.class));
                        finish();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.dismiss();
                    }
                });
                dialogo1.show();
                return false;
            }
        });
        return true;
    }

    private void Notificacion(String Titulo, String Contenido) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Horno";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Alerta", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_logo)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(Titulo)
                .setContentText(Contenido)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
    }
}
