//package com.example.hydrativa;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.loader.content.CursorLoader;
//
//import com.example.hydrativa.models.Kebun;
//import com.example.hydrativa.retrofit.KebunService;
//import com.example.hydrativa.retrofit.RetrofitClient;
//
//import java.io.File;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class edit_kebun extends AppCompatActivity {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private ImageView uploadedImage;
//    private EditText namaKebun, lokasiKebun, luasLahan, idAlat;
//    private Uri imageUri;
//    private int kebunId; // New variable to store kebun ID
//    private Button updateButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_kebun);
//
//        uploadedImage = findViewById(R.id.uploadedImage);
//        namaKebun = findViewById(R.id.namaKebun);
//        lokasiKebun = findViewById(R.id.LokasiKebun);
//        luasLahan = findViewById(R.id.LuasLahan);
//        idAlat = findViewById(R.id.hydrativa_id);
//        updateButton = findViewById(R.id.updateButton);
//
//        // Retrieve kebun data from the Intent and populate fields
//        kebunId = getIntent().getIntExtra("kebun_id", -1);
//        loadKebunData(kebunId);
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateData();
//            }
//        });
//    }
//
//    public void openImagePicker(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            uploadedImage.setImageURI(imageUri);
//        }
//    }
//
//    private String getRealPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//
//    private void loadKebunData(int kebunId) {
//        // Call your API to fetch the kebun details by ID and set them in the fields
//        // Add appropriate KebunService method to fetch data by ID
//        KebunService apiService = RetrofitClient.getRetrofitInstance(this).create(KebunService.class);
//        Call<Kebun> call = apiService.editKebun(kebunId);
//        call.enqueue(new Callback<Kebun>() {
//            @Override
//            public void onResponse(Call<Kebun> call, Response<Kebun> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Kebun kebun = response.body();
//                    namaKebun.setText(kebun.getNama_kebun());
//                    lokasiKebun.setText(kebun.getLokasi_kebun());
//                    luasLahan.setText(kebun.getLuas_lahan());
//                } else {
//                    Toast.makeText(edit_kebun.this, "Gagal memuat data kebun", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Kebun> call, Throwable t) {
//                Toast.makeText(edit_kebun.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateData() {
//        String nama = namaKebun.getText().toString().trim();
//        String lokasi = lokasiKebun.getText().toString().trim();
//        String luas = luasLahan.getText().toString().trim();
//
//        RequestBody namaKebun = RequestBody.create(MediaType.parse("text/plain"), nama);
//        RequestBody lokasiKebun = RequestBody.create(MediaType.parse("text/plain"), lokasi);
//        RequestBody luasLahan = RequestBody.create(MediaType.parse("text/plain"), luas);
//
//        MultipartBody.Part imagePart = null;
//        if (imageUri != null) {
//            File file = new File(getRealPathFromURI(imageUri));
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//            imagePart = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
//        }
//
//        KebunService apiService = RetrofitClient.getRetrofitInstance(this).create(KebunService.class);
//        Call<Void> call = apiService.editKebun();
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(edit_kebun.this, "Kebun berhasil diperbarui", Toast.LENGTH_LONG).show();
//                    finish();
//                } else {
//                    Toast.makeText(edit_kebun.this, "Gagal memperbarui kebun: " + response.message(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(edit_kebun.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}
