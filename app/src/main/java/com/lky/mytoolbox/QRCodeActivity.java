package com.lky.mytoolbox;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRCodeActivity extends AppCompatActivity {
    private EditText inputText;
    private ImageView qrImage;
    private Button saveToAlbumBtn; // 添加对保存按钮的引用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        inputText = findViewById(R.id.input_text);
        qrImage = findViewById(R.id.qr_image);
        saveToAlbumBtn = findViewById(R.id.save_to_album_btn); // 初始化保存按钮

        findViewById(R.id.generate_btn).setOnClickListener(v -> generateQRCode());
        saveToAlbumBtn.setOnClickListener(v -> saveQRCodeToAlbum()); // 设置保存按钮点击事件
    }

    private void generateQRCode() {
        String text = inputText.getText().toString();
        if (text.isEmpty()) return;

        Bitmap bitmap = encodeAsBitmap(text, 500, 500);
        if (bitmap != null) {
            qrImage.setImageBitmap(bitmap);
            saveToAlbumBtn.setVisibility(View.VISIBLE); // 显示保存按钮
        } else {
            Toast.makeText(this, "生成二维码失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static Bitmap encodeAsBitmap(String contents, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集为 UTF-8

            BitMatrix result = writer.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveQRCodeToAlbum() {
        saveImage(qrImage);
    }

    private void saveImage(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "qrcode.png");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (imageUri == null) {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            return;
        }

        try (OutputStream outputStream = getContentResolver().openOutputStream(imageUri)) {
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                Toast.makeText(this, "二维码已保存到相册", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
