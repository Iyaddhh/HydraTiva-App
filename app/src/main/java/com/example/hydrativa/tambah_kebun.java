package com.example.hydrativa;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tambah_kebun extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView uploadedImage;
    private EditText namaKebun, lokasiKebun, luasLahan, idAlat;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kebun);

        uploadedImage = findViewById(R.id.uploadedImage);
        namaKebun = findViewById(R.id.namaKebun);
        lokasiKebun = findViewById(R.id.LokasiKebun);
        luasLahan = findViewById(R.id.LuasLahan);
        idAlat = findViewById(R.id.hydrativa_id);
        Button simpanButton = findViewById(R.id.tambahButton);

        simpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void openImagePicker(View view) {
        Intent intent = new Intent();
        intent.setType("image/*"); // Menentukan tipe file yang dipilih
        intent.setAction(Intent.ACTION_GET_CONTENT); // Mengambil konten dari galeri
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Mengambil URI gambar yang dipilih
            uploadedImage.setImageURI(imageUri); // Menampilkan gambar yang dipilih di ImageView
        }
    }

    private void saveData() {
        String nama = namaKebun.getText().toString().trim();
        String lokasi = lokasiKebun.getText().toString().trim();
        String luas = luasLahan.getText().toString().trim();
        String id = idAlat.getText().toString().trim();

        if (nama.isEmpty() || lokasi.isEmpty() || luas.isEmpty() || id.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Harap lengkapi semua informasi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Konversi data ke RequestBody
        RequestBody namaKebun = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody lokasiKebun = RequestBody.create(MediaType.parse("text/plain"), lokasi);
        RequestBody luasLahan = RequestBody.create(MediaType.parse("text/plain"), luas);
        RequestBody kodeAlat = RequestBody.create(MediaType.parse("text/plain"), id);

        // Konversi URI gambar ke MultipartBody
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File file = new File(getRealPathFromURI(imageUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        // Panggil API
        KebunService apiService = RetrofitClient.getRetrofitInstance(this).create(KebunService.class);
        Call<Void> call = apiService.tambahKebun(namaKebun, luasLahan, lokasiKebun, kodeAlat, imagePart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambah_kebun.this, "Kebun berhasil ditambah", Toast.LENGTH_LONG).show();
                    clearInputFields();
                } else {
                    Toast.makeText(tambah_kebun.this, "Gagal menambah kebun" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambah_kebun.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Fungsi untuk mendapatkan path file dari URI
    private String getRealPathFromURI(Uri uri) {
        String path = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }

    private void clearInputFields() {
        namaKebun.setText("");
        lokasiKebun.setText("");
        luasLahan.setText("");
        idAlat.setText("");
        uploadedImage.setImageResource(R.drawable.tambah_gambar); // Reset gambar ke default
        imageUri = null;
    }


}