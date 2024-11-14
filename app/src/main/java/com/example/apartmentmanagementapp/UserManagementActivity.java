package com.example.apartmentmanagementapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout usersContainer;

    private static final int REQUEST_PASSPORT_PHOTO = 1;
    private static final int REQUEST_ID_FRONT_PHOTO = 2;
    private static final int REQUEST_ID_BACK_PHOTO = 3;

    private Uri passportUri, idFrontUri, idBackUri;
    private int currentRequestCode;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        switch (currentRequestCode) {
                            case REQUEST_PASSPORT_PHOTO:
                                passportUri = selectedImageUri;
                                setImage(findViewById(R.id.passportPhoto), selectedImageUri);
                                break;
                            case REQUEST_ID_FRONT_PHOTO:
                                idFrontUri = selectedImageUri;
                                setImage(findViewById(R.id.idFrontPhoto), selectedImageUri);
                                break;
                            case REQUEST_ID_BACK_PHOTO:
                                idBackUri = selectedImageUri;
                                setImage(findViewById(R.id.idBackPhoto), selectedImageUri);
                                break;
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        EditText firstNameInput = findViewById(R.id.firstNameInput);
        EditText lastNameInput = findViewById(R.id.lastNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Spinner roleSpinner = findViewById(R.id.roleSpinner);
        ImageView passportPhoto = findViewById(R.id.passportPhoto);
        ImageView idFrontPhoto = findViewById(R.id.idFrontPhoto);
        ImageView idBackPhoto = findViewById(R.id.idBackPhoto);
        Button addUserButton = findViewById(R.id.addUserButton);
        Button manageUsersButton = findViewById(R.id.manageUsersButton);

        usersContainer = findViewById(R.id.usersContainer);

        db = new DatabaseHelper(this);
        setUpRoleSpinner(roleSpinner);

        loadUsers();

        addUserButton.setOnClickListener(v ->
                addUser(firstNameInput, lastNameInput, emailInput, passwordInput, roleSpinner, passportPhoto, idFrontPhoto, idBackPhoto)
        );
        manageUsersButton.setOnClickListener(v -> manageUsers());

        passportPhoto.setOnClickListener(v -> pickImage(REQUEST_PASSPORT_PHOTO));
        idFrontPhoto.setOnClickListener(v -> pickImage(REQUEST_ID_FRONT_PHOTO));
        idBackPhoto.setOnClickListener(v -> pickImage(REQUEST_ID_BACK_PHOTO));
    }

    private void loadUsers() {
        usersContainer.removeAllViews();

        List<User> newUserList = db.getAllUsers();
        for (User user : newUserList) {
            addUserEntry(user.getFirstName(), user.getLastName(), user.getEmail());
        }
    }

    private void setUpRoleSpinner(Spinner roleSpinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, R.layout.user_role_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
    }

    private void addUserEntry(String firstName, String lastName, String email) {
        LinearLayout userLayout = new LinearLayout(this);
        userLayout.setOrientation(LinearLayout.VERTICAL);
        userLayout.setPadding(10, 10, 10, 10);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(getString(R.string.user_name, firstName, lastName));

        TextView emailTextView = new TextView(this);
        emailTextView.setText(getString(R.string.user_email, email));

        userLayout.addView(nameTextView);
        userLayout.addView(emailTextView);

        usersContainer.addView(userLayout);
    }

    private void addUser(EditText firstNameInput, EditText lastNameInput, EditText emailInput,
                         EditText passwordInput, Spinner roleSpinner,
                         ImageView passportPhoto, ImageView idFrontPhoto,
                         ImageView idBackPhoto) {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                role.isEmpty() || passportUri == null || idFrontUri == null || idBackUri == null) {
            Toast.makeText(this, "Please fill in all fields and upload all photos", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(firstName, lastName, email, password, role,
                passportUri.toString(), idFrontUri.toString(), idBackUri.toString());
        try {
            if (db.addUser(user)) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                loadUsers();
                clearInputs(passportPhoto, idFrontPhoto, idBackPhoto);
            } else {
                Toast.makeText(this, "Error adding user", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("UserManagementActivity", "Database error", e);
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void manageUsers() {
        Intent intent = new Intent(UserManagementActivity.this, ManageEachUserActivity.class);
        startActivity(intent);
    }

    public void onManageUsersButtonClick(View view) {
        Intent intent = new Intent(UserManagementActivity.this, ManageEachUserActivity.class);
        startActivity(intent);
    }

    private void clearInputs(ImageView passportPhoto, ImageView idFrontPhoto, ImageView idBackPhoto) {
        passportPhoto.setImageResource(R.drawable.upload_placeholder);
        idFrontPhoto.setImageResource(R.drawable.upload_placeholder);
        idBackPhoto.setImageResource(R.drawable.upload_placeholder);

        passportUri = null;
        idFrontUri = null;
        idBackUri = null;
    }

    private void pickImage(int requestCode) {
        currentRequestCode = requestCode;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void setImage(ImageView imageView, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Bitmap circularBitmap = getCircularBitmap(bitmap);
            imageView.setImageBitmap(circularBitmap);
        } catch (IOException e) {
            Log.e("UserManagementActivity", "Error setting image", e);
        }
    }

    // Method to create a circular bitmap
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int minSize = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(minSize, minSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        float radius = minSize / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        return output;
    }
}
