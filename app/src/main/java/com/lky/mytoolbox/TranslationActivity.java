package com.lky.mytoolbox;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;

public class TranslationActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_PERMISSION = 2;
    private static final int REQUEST_CAMERA_CAPTURE = 1003; // 拍照请求码
    private TextView tvResult;
    private ProgressBar progressBar;
    private String recognizedText = "";
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        initViews();
        checkStoragePermission();
    }

    private void initViews() {
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        Button btnSelect = findViewById(R.id.btnSelect);
        Button btnCapture = findViewById(R.id.btnCapture); // 拍照识别按钮

        btnSelect.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openImagePicker();
            }
        });

        btnCapture.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            }
        });
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkAndRequestPermission(Manifest.permission.READ_MEDIA_IMAGES, "存储");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "存储");
        } else {
            return true;
        }
    }

    private boolean checkCameraPermission() {
        return checkAndRequestPermission(Manifest.permission.CAMERA, "相机");
    }

    private boolean checkAndRequestPermission(String permission, String permissionName) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                new AlertDialog.Builder(this)
                        .setTitle("权限请求")
                        .setMessage("为了" + permissionName + "功能，我们需要访问您的" + permissionName + "权限。")
                        .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(
                                this, new String[]{permission}, REQUEST_PERMISSION))
                        .setNegativeButton("取消", null)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_CAMERA_CAPTURE);
        } else {
            showToast("设备不支持拍照功能");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    Log.d("TranslationActivity", "选择的图片 Uri: " + imageUri);
                    Bitmap bitmap = getBitmapFromUri(imageUri);
                    if (bitmap != null) {
                        processImage(bitmap);
                    } else {
                    }
                } else {
                    showToast("未选择图片");
                }
            } catch (Exception e) {
                showToast("图片处理失败");
                Log.e("TranslationActivity", "图片处理失败", e);
            }
        } else if (requestCode == REQUEST_CAMERA_CAPTURE && resultCode == RESULT_OK) {
            if (photoUri != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = getBitmapFromUri(photoUri);
                } catch (FileNotFoundException e) {
                    showToast("无法打开文件流");
                    Log.e("TranslationActivity", "无法打开文件流", e);
                }
                if (bitmap != null) {
                    processImage(bitmap);
                } else {
                    showToast("无法获取拍照结果");
                }
            } else {
                showToast("未获取到拍照结果");
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            showToast("无法打开文件流");
            return null;
        }
        try (InputStream inputStreamCloseable = inputStream) {
            return BitmapFactory.decodeStream(inputStreamCloseable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap rotateBitmapOrientation(Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                showToast("无法打开文件流");
                return null;
            }
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                return null;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap preprocessImage(Bitmap bitmap) {
        if (bitmap == null) {
            showToast("Bitmap 为空，无法预处理");
            return null;
        }
        Bitmap grayBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 去饱和度，转为灰度
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return grayBitmap;
    }

    private void processImage(Bitmap bitmap) {
        if (bitmap == null) {
            showToast("Bitmap 为空，无法处理");
            return;
        }
        Bitmap rotatedBitmap = rotateBitmapOrientation(photoUri);
        if (rotatedBitmap == null) {
            rotatedBitmap = bitmap;
        }
        Bitmap preprocessed = preprocessImage(rotatedBitmap);
        if (preprocessed == null) {
            showToast("预处理失败");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        TextRecognizer recognizer = TextRecognition.getClient(
                new ChineseTextRecognizerOptions.Builder().build()
        );
        InputImage image = InputImage.fromBitmap(preprocessed, 0);
        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        if (!text.getText().isEmpty()) {
                            recognizedText = text.getText();
                            tvResult.setText("识别的文字:\n" + recognizedText);
                            tvResult.setVisibility(View.VISIBLE);
                        } else {
                            showToast("未识别到文字");
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        showErrorDialog("OCR 识别失败，请重试", bitmap);
                    });
                });
    }

    private void translateText(String text) {
        progressBar.setVisibility(View.VISIBLE);
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            tvResult.setText("翻译结果:\n暂无翻译功能");
            tvResult.setVisibility(View.VISIBLE);
        });
    }

    private void showErrorDialog(String message, Object retryObject) {
        new AlertDialog.Builder(this)
                .setTitle("操作失败")
                .setMessage(message)
                .setPositiveButton("重试", (dialog, which) -> {
                    if (retryObject instanceof Bitmap) {
                        processImage((Bitmap) retryObject);
                    } else if (retryObject instanceof String) {
                        translateText((String) retryObject);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                }
            } else {
                showToast("需要权限才能继续操作");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
