package com.example.askmathadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AnswersFragment extends Fragment {

    private EditText editText;
    private Button saveButton;
    private FirebaseFirestore db;

    // Define the collection path name as a constant
    private static final String COLLECTION_PATH = "user_answers";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answers, container, false);

        db = FirebaseFirestore.getInstance();

        editText = view.findViewById(R.id.editText);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTextToFirestore();
            }
        });

        return view;
    }

    private void saveTextToFirestore() {
        String text = editText.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put("text", text);

        db.collection(COLLECTION_PATH).add(data)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    data.put("documentId", documentId); // Add the document ID as a field
                    updateDocumentWithId(documentReference, data); // Update the document with the document ID field
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error saving text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateDocumentWithId(DocumentReference documentReference, Map<String, Object> data) {
        documentReference.set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Text saved successfully with ID", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error updating document with ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
