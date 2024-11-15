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

public class edit_kebun extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private int kebunId;
    private ImageView uploadedImage;
    private EditText namaKebun, lokasiKebun, luasLahan, idAlat;
    private Uri imageUri;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kebun);

        kebunId = getIntent().getIntExtra("kebun_id", -1);
        uploadedImage = findViewById(R.id.uploadedImage);
        namaKebun = findViewById(R.id.namaKebun);
        lokasiKebun = findViewById(R.id.lokasiKebun);
        luasLahan = findViewById(R.id.luasKebun);
        idAlat = findViewById(R.id.hydrativa_id);
        updateButton = findViewById(R.id.updateButton);

        namaKebun.setText("");
        lokasiKebun.setText("");
        luasLahan.setText("");
        idAlat.setText("");
        uploadedImage.setImageDrawable(null);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
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
            Log.e("EditKebun", "Error retrieving real path from URI", e);
        }
        return null;
    }

    private void updateData() {
        String nama = namaKebun.getText().toString().trim();
        String lokasi = lokasiKebun.getText().toString().trim();
        String luas = luasLahan.getText().toString().trim();
        String id = idAlat.getText().toString().trim();

        if (nama.isEmpty() || lokasi.isEmpty() || luas.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua informasi", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody namaKebunRequest = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody lokasiKebunRequest = RequestBody.create(MediaType.parse("text/plain"), lokasi);
        RequestBody luasLahanRequest = RequestBody.create(MediaType.parse("text/plain"), luas);
        RequestBody idAlatRequest = RequestBody.create(MediaType.parse("text/plain"), id);

        // Mengatur imagePart hanya jika gambar dipilih
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

        KebunService apiService = RetrofitClient.getRetrofitInstance(this).create(KebunService.class);
        Call<Void> call = apiService.updateKebun(kebunId, namaKebunRequest, luasLahanRequest, lokasiKebunRequest, idAlatRequest, imagePart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("edit_kebun", "Kebun berhasil diperbarui: " + response.message());
                    Toast.makeText(edit_kebun.this, "Kebun berhasil diperbarui", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("edit_kebun", "Gagal memperbarui: " + response.code() + " - " + response.message());
                    Toast.makeText(edit_kebun.this, "Gagal memperbarui kebun: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(edit_kebun.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}