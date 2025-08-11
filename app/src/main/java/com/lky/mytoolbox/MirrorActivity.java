package com.lky.mytoolbox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MirrorActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int REQUEST_PERMISSIONS = 200;
    private Camera mCamera;
    private int currentZoomLevel = 0;
    private int maxZoomLevel = 0;
    private float screenBrightness = 0.5f; // 屏幕亮度，默认50%
    private SeekBar seekBarZoom;
    private SeekBar seekBarScreenBrightness;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Button btnCapture;
    private boolean isRestarting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

        surfaceView = findViewById(R.id.surfaceView);
        btnCapture = findViewById(R.id.btnCapture);
        seekBarZoom = findViewById(R.id.seekBarBeautyLevel);
        seekBarScreenBrightness = findViewById(R.id.seekBarScreenBrightness);

        if (surfaceView != null) {
            holder = surfaceView.getHolder();

            if (holder != null) {
                holder.addCallback(this);
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            } else {
                Log.e("MirrorActivity", "SurfaceHolder is null");
                return;
            }
        } else {
            Log.e("MirrorActivity", "SurfaceView is null");
            return;
        }

        btnCapture.setOnClickListener(v -> takePicture());

        // 设置美颜程度SeekBar监听器
        seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentZoomLevel = progress;
                applyZoom();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 设置屏幕亮度SeekBar监听器
        seekBarScreenBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                screenBrightness = progress / 100f; // 转换为0到1之间的浮点数
                setScreenBrightness(screenBrightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        checkPermissions();
        // 不要在这里重置 isRestarting 标志
    }

    private void checkPermissions() {
        String[] permissions = {Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        } else {
            initCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!isRestarting) { // 检查是否已经重新启动过
                    isRestarting = true; // 设置标志为true
                    recreate(); // 重新启动当前活动
                }
            } else {
                // 权限被拒绝，可以提示用户
                Toast.makeText(this, "需要相机权限才能使用此功能", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initCamera() {
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            setCameraDisplayOrientation();

            Camera.Parameters parameters = mCamera.getParameters();
            // 添加自动对焦设置
            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            List<Integer> formats = parameters.getSupportedPreviewFormats();
            if (formats.contains(ImageFormat.YV12)) {
                parameters.setPreviewFormat(ImageFormat.YV12);
            }
            // 初始化缩放参数
            if (parameters.isZoomSupported()) {
                maxZoomLevel = parameters.getMaxZoom();
                seekBarZoom.setMax(maxZoomLevel);
                parameters.setZoom(currentZoomLevel);
            } else {
                seekBarZoom.setEnabled(false);
                Toast.makeText(this, "该设备不支持缩放", Toast.LENGTH_SHORT).show();
            }
            mCamera.setParameters(parameters);

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e("ERROR", "初始化相机失败: " + e.getMessage());
        }
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result = (info.orientation + degrees) % 360;
        result = (360 - result) % 360;  // compensate the mirror
        mCamera.setDisplayOrientation(result);
        Log.d("MirrorActivity", "Set camera display orientation to " + result);
    }

    private void applyZoom() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {
                parameters.setZoom(currentZoomLevel);
                mCamera.setParameters(parameters);
            }
        }
    }

    private void takePicture() {
        if (mCamera != null) {
            mCamera.takePicture(null, null, (data, camera) -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // 应用数字缩放（如果需要更大的缩放范围）
                Bitmap scaledBitmap = scaleBitmap(bitmap, 1 + (currentZoomLevel / (float)maxZoomLevel));

                saveImage(scaledBitmap);
                camera.startPreview();
            });
        }
    }

    private Bitmap scaleBitmap(Bitmap original, float scaleFactor) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);

        return Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("MirrorActivity", "Surface created");
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("MirrorActivity", "Surface changed");

        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            // 在surfaceChanged方法中尝试设置最佳帧率
            List<int[]> ranges = parameters.getSupportedPreviewFpsRange();
            if (!ranges.isEmpty()) {
                // 选择最高支持的帧率范围
                int[] maxRange = ranges.get(ranges.size() - 1);
                parameters.setPreviewFpsRange(maxRange[0], maxRange[1]);
            }
            // 打印所有支持的预览大小
            for (Camera.Size size : sizes) {
                Log.d("MirrorActivity", "Supported preview size: " + size.width + "x" + size.height);
            }

            Camera.Size optimalSize = getOptimalPreviewSize(sizes, width, height);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);

            // 尝试设置其他参数
            try {
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.e("MirrorActivity", "Failed to set camera parameters: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("MirrorActivity", "Surface destroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        // 获取屏幕方向下的实际宽高
        int targetWidth = Math.min(width, height);
        int targetHeight = Math.max(width, height);

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - (double) targetWidth / targetHeight) > 0.1) {
                continue; // 过滤宽高比差异大的尺寸
            }
            // 选择最接近屏幕尺寸的分辨率
            double diff = Math.abs(size.width - targetWidth) + Math.abs(size.height - targetHeight);
            if (diff < minDiff) {
                minDiff = diff;
                optimalSize = size;
            }
        }
        return optimalSize == null ? sizes.get(0) : optimalSize;
    }

    private void saveImage(Bitmap bitmap) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("ERROR", "Error creating media file, check storage permissions");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            Toast.makeText(this, "照片已保存", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("ERROR", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("ERROR", "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BeautyMirror");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private void setScreenBrightness(float brightness) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightness <= 0 ? WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE : brightness;
        getWindow().setAttributes(layoutParams);
    }

    private void disableCameraFeatures() {
        // 禁用按钮和SeekBar
        btnCapture.setEnabled(false);
        seekBarZoom.setEnabled(false);
        seekBarScreenBrightness.setEnabled(false);
        // 显示提示信息
        Toast.makeText(this, "请在设置中授予相机权限", Toast.LENGTH_LONG).show();
    }
}
