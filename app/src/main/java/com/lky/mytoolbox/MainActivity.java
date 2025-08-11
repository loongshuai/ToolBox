package com.lky.mytoolbox;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // 使用 GridLayoutManager 替换 LinearLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        List<Tool> tools = new ArrayList<>();
        tools.add(new Tool("镜子", R.drawable.ic_mirror, MirrorActivity.class));
        tools.add(new Tool("笔记", R.drawable.ic_note, NoteActivity.class));
        tools.add(new Tool("指南针", R.drawable.ic_compass, CompassActivity.class));
        tools.add(new Tool("计算器", R.drawable.ic_cacl, CalculatorActivity.class));
        tools.add(new Tool("时间戳", R.drawable.ic_time, TimestampActivity.class));
        tools.add(new Tool("图片剪切", R.drawable.ic_caut, ImageProcessingActivity.class));
        tools.add(new Tool("图片转换", R.drawable.ic_change, ChangeActivity.class));
        tools.add(new Tool("二维码生成", R.drawable.ic_qr, QRCodeActivity.class));
        tools.add(new Tool("二维码识别", R.drawable.ic_qrread, QRCodeReaderActivity.class));
        tools.add(new Tool("文本加解密", R.drawable.ic_101, EncryptionActivity.class));
        tools.add(new Tool("图片文本识别", R.drawable.ic_rotate, TranslationActivity.class));
        tools.add(new Tool("保险箱", R.drawable.ic_vault, VaultActivity.class));
        ToolAdapter adapter = new ToolAdapter(this, tools);
        recyclerView.setAdapter(adapter);
    }
}
