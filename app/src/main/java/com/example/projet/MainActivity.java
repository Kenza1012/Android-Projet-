package com.example.projet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private TextView textEventsList;
    private Button buttonAddUser, buttonCreateEvent;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        firebaseHelper = new FirebaseHelper();

        textEventsList = findViewById(R.id.textEventsList);
        buttonAddUser = findViewById(R.id.buttonAddUser);
        buttonCreateEvent = findViewById(R.id.buttonCreateEvent);

        buttonAddUser.setOnClickListener(view -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebaseHelper.addUser(userId, "Kenza", "kenza@example.com", 25);
        });

        buttonCreateEvent.setOnClickListener(view -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebaseHelper.checkUserRole(userId, role -> {
                if ("admin".equals(role)) {
                    startActivity(new Intent(MainActivity.this, CreateEventActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Accès refusé : seuls les admins peuvent créer un événement.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        fetchEvents();
    }

    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder eventsText = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String description = document.getString("description");
                            Timestamp date = document.getTimestamp("date");
                            String location = document.getString("location");
                            Long capacity = document.getLong("capacity");

                            eventsText.append("Titre: ").append(title)
                                    .append("\nDescription: ").append(description)
                                    .append("\nDate: ").append(date != null ? date.toDate() : "Inconnue")
                                    .append("\nLieu: ").append(location)
                                    .append("\nCapacité: ").append(capacity)
                                    .append("\n\n");
                        }
                        textEventsList.setText(eventsText.toString());
                    } else {
                        Log.w(TAG, "Erreur récupération événements", task.getException());
                        textEventsList.setText("Erreur lors de la récupération des événements.");
                    }
                });
    }
}
