package com.fernandolerma.verano;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fernandolerma.verano.Clases.FireBaseReference;
import com.fernandolerma.verano.Clases.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference(FireBaseReference.REFERENCIAUSUARIO);
    private StorageReference mStorageRef;

    Button btnIniciarSesion, btnRegistrarse;
    private BottomSheetBehavior mBottomSheetBehavior;

    //Iniciar Sesión
    EditText txt_Correo, txtContraseña;
    TextInputLayout til_Correo, til_Contraseña;

    //Registarse
    Button  btn_Registrarse_Registrar, btn_Registrarse_Cancelar;
    EditText txt_Registrarse_Nombre, txt_Registrarse_Celular, txt_Registrarse_Correo, txt_Registrarse_Contraseña;
    TextInputLayout til_Registrarse_Correo, til_Registrarse_Contraseña, til_Registrarse_Nombre;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //Iniciar Sesion
        btnIniciarSesion= findViewById(R.id.btn_login_Iniciar_Sesion);
        btnRegistrarse= findViewById(R.id.btn_login_Registrarse);
        txt_Correo = findViewById(R.id.txt_login_Correo);
        txtContraseña = findViewById(R.id.txt_login_Contraseña);
        til_Correo= findViewById(R.id.tilCorreo);
        til_Contraseña= findViewById(R.id.tilContraseña);

        //Registrarse
        btn_Registrarse_Registrar= findViewById(R.id.btn_registrarse_Registrar);
        btn_Registrarse_Cancelar= findViewById(R.id.btn_registrarse_Cancelar);
        txt_Registrarse_Correo= findViewById(R.id.txt_registrarse_Correo);
        txt_Registrarse_Contraseña= findViewById(R.id.txt_registrarse_Contraseña);
        txt_Registrarse_Nombre= findViewById(R.id.txt_registrarse_Nombre);
        txt_Registrarse_Celular= findViewById(R.id.txt_registrarse_Celular);
        til_Registrarse_Nombre= findViewById(R.id.til_Registrarse_Nombre);
        til_Registrarse_Correo= findViewById(R.id.til_Registrarse_Correo);
        til_Registrarse_Contraseña= findViewById(R.id.til_Registrarse_Contraseña);

        Auth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    try {
                        Intent intent = new Intent(MainActivity.this,  Menu.class);
                        startActivity(intent);
                        finish();
                        return;
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Ocurrió un error intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        };

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "Inicaindo Sesión",
                        "Iniciando con su cuenta de Usuario....", true, false);
                if (txt_Correo.getText().toString().trim().isEmpty() || txtContraseña.getText().toString().trim().isEmpty()) {
                    if (txt_Correo.getText().toString().trim().isEmpty()) {
                        til_Correo.setError("No debe dejar vacio este campo");
                    }
                    if (txtContraseña.getText().toString().trim().isEmpty()) {
                        til_Contraseña.setError("No debe dejar vacio este campo");
                    }
                    try {
                        dialog.dismiss();
                    }catch (Exception e){}
                } else {
                    String mail = txt_Correo.getText().toString().trim();
                    String pass = txtContraseña.getText().toString().trim();
                    try {
                        Auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    try {
                                        dialog.dismiss();
                                    }catch (Exception e){}
                                } else {
                                    Toast.makeText(MainActivity.this, "No existe una cuenta con esos datos", Toast.LENGTH_SHORT).show();
                                    try {
                                        dialog.dismiss();
                                    }catch (Exception e){}
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Ocurrió un error intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        btn_Registrarse_Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!txt_Registrarse_Correo.getText().toString().isEmpty() && !txt_Registrarse_Contraseña.getText().toString().isEmpty() && !txt_Registrarse_Nombre.getText().toString().isEmpty() && txt_Registrarse_Contraseña.getText().toString().length() > 6) {
                        try {
                            dialog = ProgressDialog.show(MainActivity.this, "Registrando",
                                    "Registrando Usuario....", true, false);
                            RegistrarUsuario();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Ocurrió un error intentelo de nuevo", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (txt_Registrarse_Nombre.getText().toString().trim().isEmpty()) {
                            txt_Registrarse_Nombre.setError("No debe dejar vacio este campo");
                        }
                        if (txt_Registrarse_Correo.getText().toString().trim().isEmpty()) {
                            txt_Registrarse_Correo.setError("No debe dejar vacio este campo");
                        }
                        if (txt_Registrarse_Contraseña.getText().toString().trim().isEmpty()) {
                            txt_Registrarse_Contraseña.setError("No debe dejar vacio este campo");
                        } else {
                            if (txt_Registrarse_Contraseña.getText().toString().trim().length() < 7) {
                                txt_Registrarse_Contraseña.setError("La contraseña debe llevar mas caracteres");
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

        btn_Registrarse_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                txt_Registrarse_Nombre.setText("");
                txt_Registrarse_Correo.setText("");
                txt_Registrarse_Contraseña.setText("");
            }
        });
    }

    private void RegistrarUsuario(){
        final String Email= txt_Registrarse_Correo.getText().toString().trim();
        final String Password= txt_Registrarse_Contraseña.getText().toString().trim();
        final String Nombre= txt_Registrarse_Nombre.getText().toString().trim();
        final String Celular= txt_Registrarse_Celular.getText().toString().trim();
        if(!Email.isEmpty() && !Password.isEmpty() && !Nombre.isEmpty() && !Celular.isEmpty() && Password.length()>6){
            Auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    try {
                        if (task.isSuccessful()) {
                            Usuarios datos = new Usuarios(Nombre, Celular, Email, Password);
                            myRef.child(Auth.getUid()).child(FireBaseReference.REFERENCIADATOS).setValue(datos);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                try {
                                    dialog.dismiss();
                                }catch (Exception e){}
                                Toast.makeText(getApplicationContext(), "Usuario ya en uso", Toast.LENGTH_SHORT).show();

                            } else {
                                try {
                                    dialog.dismiss();
                                }catch (Exception e){}
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){}
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Auth.addAuthStateListener(firebaseAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Auth.removeAuthStateListener(firebaseAuthListener);
    }
}
