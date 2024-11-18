package com.example.hydrativa;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hydrativa.models.User;
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
        setContentView(R.layout.activity_edit_profile);

        ImageView settingLink = findViewById(R.id.settingLink);

        settingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, Profile.class);
                startActivity(intent);
            }
        });

        radioGroup = findViewById(R.id.radioGroup1);
        uploadedImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.input_name);
        username = findViewById(R.id.input_username);
        telp = findViewById(R.id.input_phone);
        suntingButton = findViewById(R.id.suntingButton);

        profileService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ProfileService.class);

        // Memuat data pengguna
        loadUserProfile();

        suntingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileData();
            }
        });
    }

    private void loadUserProfile() {
        // Mengambil data profil pengguna dari API
        Call<User> call = profileService.getProfile();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // Menampilkan data pengguna ke dalam EditText dan RadioGroup
                    name.setText(user.getName());
                    username.setText(user.getUsername());
                    telp.setText(user.getTelp());

                    // Set jenis kelamin berdasarkan data yang diterima
                    if (user.getJenis_kelamin() != null) {
                        if (user.getJenis_kelamin().equalsIgnoreCase("Laki-laki")) {
                            radioGroup.check(R.id.radio_laki);
                        } else if (user.getJenis_kelamin().equalsIgnoreCase("Perempuan")) {
                            radioGroup.check(R.id.radio_wanita);
                        }
                    }

                    // Menampilkan gambar profil jika ada
                    if (user.getGambar() != null && !user.getGambar().isEmpty()) {
                        User userProfile = response.body();
                        String imageUrl = userProfile.getGambar();

                        if (imageUrl != null && imageUrl.startsWith("http://")) {
                            imageUrl = "https://" + imageUrl.substring(7); // Mengganti http:// menjadi https://
                        }

                        Glide.with(EditProfile.this)
                                .load(imageUrl)
                                .into(uploadedImage);
                    }
                } else {
                    Toast.makeText(EditProfile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        } else {
            uploadedImage.setImageResource(R.drawable.default_profile);
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
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_laki) {
            jenis_kelamin = "Laki-laki";
        } else if (selectedId == R.id.radio_wanita) {
            jenis_kelamin = "Perempuan";
        } else {
            jenis_kelamin = null;  // jika tidak ada pilihan yang dipilih
        }

        // Validasi input
        if (inputName.isEmpty() || inputUsername.isEmpty() || jenis_kelamin == null || inputTelp.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua informasi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Persiapkan data untuk dikirim
        RequestBody requestName = RequestBody.create(MediaType.parse("text/plain"), inputName);
        RequestBody requestUsername = RequestBody.create(MediaType.parse("text/plain"), inputUsername);
        RequestBody requestJenis_Kelamin = RequestBody.create(MediaType.parse("text/plain"), jenis_kelamin);
        RequestBody requestTelp = RequestBody.create(MediaType.parse("text/plain"), inputTelp);

        // Menyiapkan image jika ada
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File file = new File(getRealPathFromURI(imageUri));
            if (file.length() > 2048 * 1024) {  // 2MB max size
                Toast.makeText(this, "File gambar terlalu besar", Toast.LENGTH_SHORT).show();
                return;
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
        }

        // Kirim data ke server menggunakan Retrofit
        Call<Void> call = profileService.updateProfile(requestUsername, requestJenis_Kelamin, requestName, requestTelp, imagePart);

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
