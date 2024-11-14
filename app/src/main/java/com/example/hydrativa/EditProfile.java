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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RadioGroup radioGroup;
    private String jenis_kelamin;
    private EditText name, username, telp;
    private Uri imageUri;
    private Button suntingButton;
    private ProfileService profileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        radioGroup = findViewById(R.id.radioGroup1);
        profileService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ProfileService.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    jenis_kelamin = selectedRadioButton.getText().toString();
                    Toast.makeText(EditProfile.this, "Jenis Kelamin: " + jenis_kelamin, Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadedImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.input_name);
        username = findViewById(R.id.input_username);
        telp = findViewById(R.id.input_phone);
        suntingButton = findViewById(R.id.suntingButton);

        name.setText("");
        username.setText("");
        telp.setText("");
        uploadedImage.setImageDrawable(null);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        suntingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileData();
            }
        });
    }

    public void openImagePicker(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadedImage.setImageURI(imageUri);
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
        String inputTelp = telp.getText().toString().trim();

        if (inputName.isEmpty() || inputUsername.isEmpty() || jenis_kelamin == null || inputTelp.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua informasi", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"), inputName);
        RequestBody requestUsername = RequestBody.create(MediaType.parse("text/plain"), inputUsername);
        RequestBody requestJenis_Kelamin = RequestBody.create(MediaType.parse("text/plain"), jenis_kelamin);
        RequestBody requestTelp = RequestBody.create(MediaType.parse("text/plain"), inputTelp);

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

        Call<Void> call = profileService.updateProfile(requestName, requestUsername, requestJenis_Kelamin, requestTelp, imagePart);

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
