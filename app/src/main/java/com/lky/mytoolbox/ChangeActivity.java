package com.lky.mytoolbox;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangeActivity extends AppCompatActivity {

    // 添加格式定义常量
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_CODE = 2;
    private static final int FORMAT_PDF = 0;
    private static final int FORMAT_PNG = 1;
    private static final int FORMAT_JPG = 2;
    private static final int FORMAT_GIF = 3;
    private static final int FORMAT_SVG = 4;

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    private ProgressDialog progressDialog;

    private Spinner spinnerFormat;
    private Uri selectedUri;
    private TextView statusTextView;
    private ImageView imagePreview;
    private TextView imageFormatTextView;
    private Button btnConfirmConvert;
    private ImageButton btnImageConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        // 初始化Spinner
        spinnerFormat = findViewById(R.id.spinnerFormat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.conversion_formats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormat.setAdapter(adapter);

        // 初始化状态文本视图
        statusTextView = findViewById(R.id.statusTextView);

        // 初始化图片预览和格式显示
        imagePreview = findViewById(R.id.imagePreview);
        imageFormatTextView = findViewById(R.id.imageFormatTextView);

        // 初始化按钮
        btnImageConvert = findViewById(R.id.btnImageConvert);
        btnConfirmConvert = findViewById(R.id.btnConfirmConvert);

        // 修改按钮点击事件
        btnImageConvert.setOnClickListener(v -> pickImageFile());

        // 确认转换按钮点击事件
        btnConfirmConvert.setOnClickListener(v -> {
            int selectedFormat = spinnerFormat.getSelectedItemPosition();
            convertImage(selectedUri, selectedFormat);
        });

        // 图片预览点击事件
        imagePreview.setOnClickListener(v -> pickImageFile());
    }

    private void pickImageFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedUri = data.getData();
            if (requestCode == PICK_IMAGE_CODE) {
                // 隐藏选择图片按钮
                btnImageConvert.setVisibility(View.GONE);

                // 显示图片预览和格式显示
                imagePreview.setVisibility(View.VISIBLE);
                imageFormatTextView.setVisibility(View.VISIBLE);
                btnConfirmConvert.setVisibility(View.VISIBLE);

                // 显示图片预览
                try (InputStream is = getContentResolver().openInputStream(selectedUri)) {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imagePreview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    handleError(e);
                }

                // 显示图片格式
                String mimeType = getMimeType(selectedUri);
                String format = getFormatFromMimeType(mimeType);
                imageFormatTextView.setText("图片格式：" + format);
            }
        }
    }

    private String getMimeType(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        return contentResolver.getType(uri);
    }

    private String getFormatFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/png":
                return "PNG";
            case "image/jpeg":
                return "JPEG";
            case "image/gif":
                return "GIF";
            case "image/svg+xml":
                return "SVG";
            default:
                return mimeType;
        }
    }

    private void convertImage(Uri inputUri, int targetFormat) {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        executorService.execute(() -> {
            try {
                switch (targetFormat) {
                    case FORMAT_PDF:
                        convertToPdf(inputUri);
                        break;
                    case FORMAT_PNG:
                        convertToBitmapFormat(inputUri, Bitmap.CompressFormat.PNG, "image/png", ".png");
                        break;
                    case FORMAT_JPG:
                        convertToBitmapFormat(inputUri, Bitmap.CompressFormat.JPEG, "image/jpeg", ".jpg");
                        break;
                    case FORMAT_GIF:
                        convertToGif(inputUri);
                        break;
                    case FORMAT_SVG:
                        convertToSvg(inputUri);
                        break;
                }
            } catch (Exception e) {
                handleError(e);
            }
        });
    }

    private void convertToPdf(Uri inputUri) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "converted_" + System.currentTimeMillis() + ".pdf");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri pdfUri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            ImageConverter.convertImageToPdf(this, inputUri, pdfUri, new ConversionCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> showSuccess("图片转换为 PDF 成功，保存到手机内存的Download文件夹中！"));
                    Toast.makeText(ChangeActivity.this, "图片转换为 PDF 成功，保存到手机内存的Download文件夹中！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    runOnUiThread(() -> showError("图片转换为 PDF 失败"));
                }
            }, executorService);
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void convertToBitmapFormat(Uri inputUri, Bitmap.CompressFormat format, String mimeType, String extension) {
        try (InputStream is = getContentResolver().openInputStream(inputUri)) {
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "converted_" + System.currentTimeMillis() + extension);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri outputUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try (OutputStream os = getContentResolver().openOutputStream(outputUri)) {
                bitmap.compress(format, 100, os);
                runOnUiThread(() -> showSuccess("转换成功！文件已保存到相册"));
            }
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void convertToGif(Uri inputUri) {
        try {
            throw new Exception("GIF转换功能暂未实现");
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void convertToSvg(Uri inputUri) {
        try {
            throw new Exception("SVG转换功能暂未实现");
        } catch (Exception e) {
            handleError(e);
        }
    }

    private ConversionCallback createCallback() {
        return new ConversionCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> showSuccess("转换成功！"));
            }

            @Override
            public void onFailure() {
                runOnUiThread(() -> showError("转换失败"));
            }
        };
    }

    private void showSuccess(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (statusTextView != null) {
            statusTextView.setText(message);
        }
        // 重置界面
        resetUI();
    }

    private void showError(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (statusTextView != null) {
            statusTextView.setText(message);
        }

        // 重置界面
        resetUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public static class ProgressDialog extends Dialog {
        public ProgressDialog(Context context) {
            super(context);
            setContentView(R.layout.dialog_progress);
            setCancelable(false);
        }
    }

    private void handleError(Exception e) {
        updateStatus("错误: " + e.getMessage());
        Log.e("ChangeActivity", "Error", e);
    }

    private void updateStatus(String message) {
        runOnUiThread(() -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (statusTextView != null) {
                statusTextView.setText(message);
            }
        });
    }

    public static class ImageConverter {
        public static void convertImageToPdf(Context context, Uri inputUri, Uri outputPath, ConversionCallback callback, ExecutorService executorService) throws IOException {
            File tempFile = getTempFileFromUri(context, inputUri);
            String inputPath = tempFile.getAbsolutePath();

            Runnable conversionTask = () -> {
                try (OutputStream fos = context.getContentResolver().openOutputStream(outputPath);
                     PdfWriter writer = new PdfWriter(fos);
                     PdfDocument pdfDoc = new PdfDocument(writer);
                     Document document = new Document(pdfDoc)) {

                    ImageData imageData = ImageDataFactory.create(inputPath);
                    Image image = new Image(imageData);
                    document.add(image);
                    document.close();

                    callback.onSuccess();
                } catch (IOException e) {
                    Log.e("ImageConverter", "Failed to convert image to PDF", e);
                    callback.onFailure();
                }
            };

            executorService.submit(conversionTask);
        }

        private static File getTempFileFromUri(Context context, Uri uri) throws IOException {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File tempFile = File.createTempFile("temp", null, context.getCacheDir());
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                inputStream.close();
            }
            return tempFile;
        }
    }

    public interface ConversionCallback {
        void onSuccess();
        void onFailure();
    }

    private void resetUI() {
        // 隐藏图片预览和格式显示
        imagePreview.setVisibility(View.GONE);
        imageFormatTextView.setVisibility(View.GONE);
        btnConfirmConvert.setVisibility(View.GONE);

        // 显示选择图片按钮
        btnImageConvert.setVisibility(View.VISIBLE);
        statusTextView.setText("请选择一张图片进行转换");
    }
}
