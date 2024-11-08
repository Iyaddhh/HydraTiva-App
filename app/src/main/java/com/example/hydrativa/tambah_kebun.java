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
import androidx.loader.content.CursorLoader;

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

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
        RequestBody idAlat = RequestBody.create(MediaType.parse("text/plain"), id);

        // Konversi URI gambar ke MultipartBody
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File file = new File(getRealPathFromURI(imageUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile); // Pastikan nama parameternya "gambar"
        }

        // Panggil API
        KebunService apiService = RetrofitClient.getRetrofitInstance(this).create(KebunService.class);
        Call<Void> call = apiService.tambahKebun(namaKebun, luasLahan, lokasiKebun, idAlat, imagePart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambah_kebun.this, "Kebun berhasil ditambah", Toast.LENGTH_LONG).show();
                    clearInputFields();
                } else {
                    Toast.makeText(tambah_kebun.this, "Gagal menambah kebun: " + response.message(), Toast.LENGTH_LONG).show(); // Memperbaiki pesan error
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambah_kebun.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearInputFields() {
        namaKebun.setText(""); // Mengosongkan field nama kebun
        lokasiKebun.setText(""); // Mengosongkan field lokasi kebun
        luasLahan.setText(""); // Mengosongkan field luas lahan
        idAlat.setText(""); // Mengosongkan field ID alat
        uploadedImage.setImageURI(null); // Menghapus gambar yang ditampilkan
    }

}
