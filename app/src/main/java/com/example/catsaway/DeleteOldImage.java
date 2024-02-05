package com.example.catsaway;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
public class DeleteOldImage extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "DeleteOldImage";

        // Replace 'your-storage-url' with your Firebase Storage URL
        private static final String STORAGE_URL = "gs://console.firebase.google.com/project/csp650-fyp-c5ddf/database/csp650-fyp-c5ddf-default-rtdb/data/~2F";

        // Define your logic to identify old images (e.g., based on timestamp)
        private long cutoffTimestamp = System.currentTimeMillis() - (300000);

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        @Override
        protected Void doInBackground(Void... voids) {
            // Initialize Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance(STORAGE_URL);
            StorageReference storageRef = storage.getReference();

            try {
                // Get a list of all files in the storage reference
                List<StorageReference> allFiles = new ArrayList<>();
                //listAllFiles(storageRef, allFiles).get();

                // Delete old files
                for (StorageReference fileRef : allFiles) {
                    fileRef.getMetadata().addOnSuccessListener(metadata -> {
                        // Check if the file is old based on your criteria (e.g., timestamp)
                        if (metadata.getCreationTimeMillis() < cutoffTimestamp) {
                            fileRef.delete().addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Deleted old image: " + fileRef.getPath());
                            }).addOnFailureListener(e -> {
                                Log.e(TAG, "Error deleting image: " + fileRef.getPath(), e);
                            });
                        }
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deleting old images", e);
            }

            return null;
        }

        private Task<Void> listAllFiles(StorageReference storageRef, List<StorageReference> allFiles) {
            return storageRef.listAll().continueWithTask(task -> {
                allFiles.addAll(task.getResult().getItems());
                for (StorageReference prefix : task.getResult().getPrefixes()) {
                    listAllFiles(prefix, allFiles);
                }
                return null;
            });
        }
    }


