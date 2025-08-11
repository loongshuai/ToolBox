package com.lky.mytoolbox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VaultActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText fileNameEditText;
    private EditText fileContentEditText;
    private Button saveButton;
    private Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        fileNameEditText = findViewById(R.id.file_name_edit_text);
        fileContentEditText = findViewById(R.id.file_content_edit_text);
        saveButton = findViewById(R.id.save_button);
        loadButton = findViewById(R.id.load_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFile();
            }
        });
    }

    private void saveFile() {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String fileName = fileNameEditText.getText().toString();
        String fileContent = fileContentEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fileName.isEmpty() || fileContent.isEmpty()) {
            Toast.makeText(this, "文件名和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String encryptedPassword = encryptPassword(password);
            File file = new File(getFilesDir(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((encryptedPassword + "\n" + fileContent).getBytes());
            fos.close();
            Toast.makeText(this, "文件已保存", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存文件失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFile() {
        String password = passwordEditText.getText().toString();
        String fileName = fileNameEditText.getText().toString();

        if (fileName.isEmpty()) {
            Toast.makeText(this, "文件名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(getFilesDir(), fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();
            String content = new String(buffer);
            String[] parts = content.split("\n", 2);
            if (parts.length != 2) {
                Toast.makeText(this, "文件格式错误", Toast.LENGTH_SHORT).show();
                return;
            }
            String encryptedPassword = parts[0];
            String fileContent = parts[1];
            if (encryptPassword(password).equals(encryptedPassword)) {
                fileContentEditText.setText(fileContent);
                Toast.makeText(this, "文件已加载", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "加载文件失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
