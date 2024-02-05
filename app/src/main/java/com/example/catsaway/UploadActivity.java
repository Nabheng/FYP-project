//package com.example.catsaway;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import android.app.Activity;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Switch;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ServerValue;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//public class UploadActivity extends AppCompatActivity {
//
//    private FloatingActionButton uploadButton;
//    private ImageView uploadImage;
//    EditText uploadCaption;
//    ProgressBar progressBar;
//    private Uri imageUri;
//    final  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
//    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images");
//
//    final SharedPreferences examplePrefs = getSharedPreferences("setting",0);
//    final SharedPreferences.Editor editor = examplePrefs.edit();
//
//
//    public final int[] Notification_priority = new int[1];
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload);
//        ToggleButton Notification_switch = (ToggleButton) findViewById(R.id.toggleButton);
//
//        uploadButton = findViewById(R.id.uploadButton);
//        uploadCaption = findViewById(R.id.uploadCaption);
//        uploadImage = findViewById(R.id.uploadImage);
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.INVISIBLE);
//
//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK){
//                            Intent data = result.getData();
//                            imageUri = data.getData();
//                            uploadImage.setImageURI(imageUri);
//                        } else {
//                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        );
//
//        uploadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent photoPicker = new Intent();
//                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
//                photoPicker.setType("image/*");
//                activityResultLauncher.launch(photoPicker);
//            }
//        });
//
//        uploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (imageUri != null){
//                    uploadToFirebase(imageUri);
//                } else  {
//                    Toast.makeText(UploadActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        createNotification();
//    }
//    //Outside onCreate
//    private void uploadToFirebase(Uri uri)
//    {
//        String caption = uploadCaption.getText().toString();
//        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//
//        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
//            {
//                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        DataClass dataClass = new DataClass(uri.toString(), caption, System.currentTimeMillis());
//                        String key = databaseReference.push().getKey();
//                        databaseReference.child(key).setValue(dataClass);
//                        databaseReference.child(key).child("timestamp").setValue(ServerValue.TIMESTAMP);
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(UploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
//                        showNotification();
//                        Intent intent = new Intent(UploadActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(UploadActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//    private String getFileExtension(Uri fileUri){
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
//    }
//
//
//    private void createNotification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            NotificationChannel channel = new NotificationChannel("default_channel_id", name, Notification_priority[0]);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = (NotificationManager) this.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void showNotification() {
//        String contentText = getString(R.string.channel_description);
//
//        // Create a notification builder
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel_id")
//                .setSmallIcon(R.drawable.baseline_notifications_24)
//                .setContentTitle("CatsAway")
//                .setContentText(contentText)
//                .setPriority(Notification_priority[0]);
//
//        // Show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        notificationManager.notify(1, builder.build());
//    }
//}