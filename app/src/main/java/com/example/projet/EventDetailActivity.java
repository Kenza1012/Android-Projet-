package com.example.projet;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailActivity extends AppCompatActivity {

    private TextView titleTextView, dateTextView, descriptionTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        titleTextView = findViewById(R.id.eventDetailTitle);
        dateTextView = findViewById(R.id.eventDetailDate);
        descriptionTextView = findViewById(R.id.eventDetailDescription);

        db = FirebaseFirestore.getInstance();

        // Récupérer l'ID de l'événement envoyé par l'intent
        String eventId = getIntent().getStringExtra("event_id");

        if (eventId != null) {
            loadEventDetails(eventId);
        } else {
            Toast.makeText(this, "Aucun ID d'événement reçu", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadEventDetails(String eventId) {
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        String date = documentSnapshot.getString("date");
                        String description = documentSnapshot.getString("description");

                        titleTextView.setText(title);
                        dateTextView.setText(date);
                        descriptionTextView.setText(description != null ? description : "Pas de description");
                    } else {
                        Toast.makeText(this, "Événement non trouvé", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur de chargement : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
