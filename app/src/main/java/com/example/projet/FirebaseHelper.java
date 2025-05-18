package com.example.projet;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projet.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";

    private static FirebaseFirestore db;
    private static FirebaseAuth auth;


    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseFirestore getDB() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }
    public void addUser(String id, String name, String email, int age) {
        User user = new User(id, name, email, age);
        db.collection("users")
                .document(id)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Utilisateur ajouté avec succès"))
                .addOnFailureListener(e -> Log.e(TAG, "Erreur ajout utilisateur", e));
    }

    public void checkUserRole(String userId, RoleCallback callback) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        callback.onRoleRetrieved(role);
                    } else {
                        callback.onRoleRetrieved(null);
                    }
                })
                .addOnFailureListener(e -> callback.onRoleRetrieved(null));
    }

    public interface RoleCallback {
        void onRoleRetrieved(String role);
    }

    // Autres méthodes comme addEvent, getEvents, etc. à rajouter selon besoin
}
