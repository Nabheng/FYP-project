package com.example.catsaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import kotlin.random.Random;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;

    private GridView gridView;
    private ArrayList<DataClass> dataList;
    private MyAdapter adapter;
    private Uri imageUri;
    boolean mNotificationState;
    Notification mNotification;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images2");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build());

        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);

        gridView = findViewById(R.id.gridView);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList);
        gridView.setAdapter(adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("Images2") != null) {
                        String base64Data = dataSnapshot.getValue(String.class);
//                        String pureBase64Encoded = removeBase64Prefix(base64Data);
                        final String pureBase64Encoded = base64Data.substring(base64Data.indexOf(",") + 1);
                        byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        final Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                            FirebaseHelper firebaseHelper = new FirebaseHelper();
                            firebaseHelper.uploadBitmapAndSaveUrl(decodedBitmap, storageReference);


                            Intent photoPicker = new Intent();

                        try {
                            DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                            dataList.add(dataClass);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            // Log the dataSnapshot or relevant information for debugging.
                        }
                    } else {
                        Log.e("Firebase", "Missing or invalid data in dataSnapshot");
                    }


                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
    }
//    Outside onCreate

    public class FirebaseHelper {

        private static final String TAG = "FirebaseHelper";

        public void uploadBitmapAndSaveUrl(final Bitmap decodedBitmap, final StorageReference storageReference) {
            // Get a reference to Firebase Storage
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            // Create a StorageReference for the image
            StorageReference imageReference = storageReference.child("images2/" + System.currentTimeMillis() + ".jpg");

            // Convert the bitmap to a byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageReference.putBytes(imageData);

            // Add an OnCompleteListener to get the download URL
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        // Get the download URL
                        Uri downloadUrl = task.getResult().getStorage().getDownloadUrl().getResult();

                        // Save the download URL to the Realtime Database
                        databaseReference.setValue(downloadUrl.toString());

                    } else
                    {

                    }
                }
            });
        }
    }

    private static String removeBase64Prefix(String base64StringWithPrefix)
    {
        // Define the prefix to be removed
        String prefix = "data:image/jpeg;base64,";

        // Check if the base64StringWithPrefix starts with the specified prefix
        if (base64StringWithPrefix.startsWith(prefix))
        {
            // Remove the prefix using substring
            return base64StringWithPrefix.substring(prefix.length());
        }
        else
        {
            // If the prefix is not present, return the original string
            return base64StringWithPrefix;
        }
    }
}
