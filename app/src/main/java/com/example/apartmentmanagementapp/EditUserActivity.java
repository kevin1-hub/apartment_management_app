package com.example.apartmentmanagementapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.InputStream;

public class EditUserActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private ImageView passportPhoto, idFrontPhoto, idBackPhoto;
    private String passportUri, idFrontUri, idBackUri;
    private int userId;
    private String imageType;
    private TextView roleTextView;
    private Spinner roleSpinner;
    private final String[] roles = {"Admin", "Staff", "Caretaker"};
    private boolean isInitialLoad = true;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        loadImage(imageUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        firstNameInput = findViewById(R.id.editFirstNameInput);
        lastNameInput = findViewById(R.id.editLastNameInput);
        emailInput = findViewById(R.id.editEmailInput);
        passwordInput = findViewById(R.id.editPasswordInput);
        passportPhoto = findViewById(R.id.editPassportPhoto);
        idFrontPhoto = findViewById(R.id.editIdFrontPhoto);
        idBackPhoto = findViewById(R.id.editIdBackPhoto);
        roleTextView = findViewById(R.id.roleTextView);
        roleSpinner = findViewById(R.id.roleSpinner);
        Button saveButton = findViewById(R.id.saveUserButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isInitialLoad) {
                    roleTextView.setText(roles[position]);
                }
                isInitialLoad = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        passportPhoto.setOnClickListener(v -> openImagePicker("passport"));
        idFrontPhoto.setOnClickListener(v -> openImagePicker("idFront"));
        idBackPhoto.setOnClickListener(v -> openImagePicker("idBack"));

        db = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            loadUserDetails(userId);
        } else {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        saveButton.setOnClickListener(v -> updateUser());
        requestStoragePermission();
    }

    private void openImagePicker(String type) {
        imageType = type;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void loadImage(Uri imageUri) {
        switch (imageType) {
            case "passport":
                passportUri = imageUri.toString();
                loadImageFromUri(passportUri, passportPhoto);
                break;
            case "idFront":
                idFrontUri = imageUri.toString();
                loadImageFromUri(idFrontUri, idFrontPhoto);
                break;
            case "idBack":
                idBackUri = imageUri.toString();
                loadImageFromUri(idBackUri, idBackPhoto);
                break;
        }
    }

    private void loadUserDetails(int userId) {
        User user = db.getUserById(userId);
        if (user != null) {
            firstNameInput.setText(user.getFirstName());
            lastNameInput.setText(user.getLastName());
            emailInput.setText(user.getEmail());
            passwordInput.setText(user.getPassword());
            roleTextView.setText(user.getRole());
            loadImageFromUri(user.getPassportUri(), passportPhoto);
            loadImageFromUri(user.getIdFrontUri(), idFrontPhoto);
            loadImageFromUri(user.getIdBackUri(), idBackPhoto);

            // Set the spinner selection based on the user's role
            int rolePosition = getRolePosition(user.getRole());
            roleSpinner.setSelection(rolePosition);
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private int getRolePosition(String role) {
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equals(role)) {
                return i;
            }
        }
        return 0; // Default to the first role if not found
    }

    private void loadImageFromUri(String uriString, ImageView imageView) {
        if (uriString != null) {
            try {
                Uri uri = Uri.parse(uriString);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    imageView.setImageBitmap(getCircularBitmap(bitmap));
                }
            } catch (Exception e) {
                Log.e("EditUserActivity", "Error loading image", e);
            }
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        BitmapShader shader = new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);

        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);
        return output;
    }

    private void updateUser() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String role = roleTextView.getText().toString(); // Get the updated role

        // Retrieve the latest URIs for images
        String updatedPassportUri = (passportUri != null) ? passportUri : db.getUserById(userId).getPassportUri();
        String updatedIdFrontUri = (idFrontUri != null) ? idFrontUri : db.getUserById(userId).getIdFrontUri();
        String updatedIdBackUri = (idBackUri != null) ? idBackUri : db.getUserById(userId).getIdBackUri();

        User updatedUser = new User(userId, firstName, lastName, email, password, role, updatedPassportUri, updatedIdFrontUri, updatedIdBackUri);
        boolean isUpdated = db.updateUser(updatedUser);

        if (isUpdated) {
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
