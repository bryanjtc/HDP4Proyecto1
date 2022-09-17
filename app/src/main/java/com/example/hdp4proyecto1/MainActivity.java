package com.example.hdp4proyecto1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("data");
    EditText cedula;
    Button votar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cedula = findViewById(R.id.et_cedula);
        votar = findViewById(R.id.btn_votar);
        votar.setOnClickListener(view -> {
            if (cedula.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Ingrese su cedula",
                        Toast.LENGTH_LONG).show();
            }
            else if (!cedula.getText().toString().contains("-")) {
                Toast.makeText(getApplicationContext(), "Debe ingresar su cedula con guion",
                        Toast.LENGTH_LONG).show();
            }
            else {
                String[] parts = cedula.getText().toString().split("-");
                String part1 = Strings.padStart(parts[0], 2, '0');
                String part2 = Strings.padStart(parts[1], 4, '0');
                String part3 = Strings.padStart(parts[2], 6, '0');
                String cedulaZeros = part1 + "-" + part2 + "-" + part3;
                Query queryCedula = myRef.orderByChild("cedula").equalTo(cedulaZeros);
                Log.d("Cedula", cedulaZeros);
                queryCedula.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String uid = childSnapshot.getKey();
                                Log.d("Firebase database", "Key is: " + uid);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Cedula no encontrada", Toast.LENGTH_LONG).show();
                            Log.d("Firebase database", "No existe");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.w("Firebase database", "Failed to read value.", error.toException());
                    }
                });
            }
        });

    }
}