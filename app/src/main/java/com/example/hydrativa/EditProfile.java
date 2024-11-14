package com.example.hydrativa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hydrativa.retrofit.ProfileService;
import com.example.hydrativa.retrofit.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private ImageView uploadedImage;
    private EditText name, username, email, telp;
    private Uri imageUri;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        uploadedImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.input_name);
        username = findViewById(R.id.input_username);
        email = findViewById(R.id.input_email);
        telp = findViewById(R.id.input_phone);
        updateButton = findViewById(R.id.update_button);

        name.setText("");
        username.setText("");
        email.setText("");
        telp.setText("");
        uploadedImage.setImageDrawable(null);

        // Check for storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        // Set the update button action
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileData();
            }
        });
    }

    public void openImagePicker(View view) {
        // Check if permission is granted before opening the image picker
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            Toast.makeText(this, "Izin akses penyimpanan diperlukan untuk memilih gambar", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadedImage.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image picker
                openImagePicker(null);
            } else {
                Toast.makeText(this, "Izin akses penyimpanan ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e("EditProfile", "Error retrieving real path from URI", e);
        }
        return null;
    }

    private void updateProfileData() {
        String inputName = name.getText().toString().trim();
        String inputUsername = username.getText().toString().trim();
        String inputEmail = email.getText().toString().trim();
        String inputTelp = telp.getText().toString().trim();

        if (inputName.isEmpty() || inputUsername.isEmpty() || inputEmail.isEmpty() || inputTelp.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua informasi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creating RequestBody for each input field
        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"), inputName);
        RequestBody requestUsername = RequestBody.create(MediaType.parse("text/plain"), inputUsername);
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"), inputEmail);
        RequestBody requestTelp = RequestBody.create(MediaType.parse("text/plain"), inputTelp);

        // Set imagePart only if an image is selected
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File file = new File(getRealPathFromURI(imageUri));
            if (file.length() > 2048 * 1024) {
                Toast.makeText(this, "File gambar terlalu besar", Toast.LENGTH_SHORT).show();
                return;
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
        }

        // Make the API call to update profile
        ProfileService apiService = RetrofitClient.getRetrofitInstance(this).create(ProfileService.class);
        Call<Void> call = apiService.updateProfile(requestName, requestUsername, requestEmail, requestTelp, imagePart);

        // Handling API response
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("EditProfile", "Profil berhasil diperbarui: " + response.message());
                    Toast.makeText(EditProfile.this, "Profil berhasil diperbarui", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("EditProfile", "Gagal memperbarui: " + response.code() + " - " + response.message());
                    Toast.makeText(EditProfile.this, "Gagal memperbarui profil: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
