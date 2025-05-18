package com.example.projet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {
    private EditText titleInput, dateInput, descriptionInput;
    private Button saveButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseHelper.getDB();

        titleInput = findViewById(R.id.titleInput);
        dateInput = findViewById(R.id.dateInput); // À toi de gérer le format date
        descriptionInput = findViewById(R.id.descriptionInput);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> saveEvent());
    }

    private void saveEvent() {
        String title = titleInput.getText().toString();
        String date = dateInput.getText().toString();
        String description = descriptionInput.getText().toString();

        Map<String, Object> event = new HashMap<>();
        event.put("title", title);
        event.put("date", date);
        event.put("description", description);

        db.collection("events").add(event)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Événement créé", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

