package com.lky.mytoolbox;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EncryptionActivity extends AppCompatActivity {
    private RadioGroup rgEncryption;
    private EditText etInput;
    private TextView tvResult;
    private Button btnEncrypt, btnDecrypt, btnCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        rgEncryption = findViewById(R.id.rg_encryption);
        etInput = findViewById(R.id.et_input);
        tvResult = findViewById(R.id.tv_result);
        btnEncrypt = findViewById(R.id.btn_encrypt);
        btnDecrypt = findViewById(R.id.btn_decrypt);
        btnCopy = findViewById(R.id.btn_copy);
        updateButtonVisibility();
        btnCopy.setOnClickListener(v -> copyToClipboard());
        rgEncryption.setOnCheckedChangeListener((group, checkedId) -> {
            updateButtonVisibility();
            tvResult.setText("");
        });

        btnEncrypt.setOnClickListener(v -> {
            String input = etInput.getText().toString();
            try {
                String result = encrypt(input, getSelectedMethod());
                tvResult.setText("加密结果: " + result);
            } catch (Exception e) {
                tvResult.setText("加密失败: " + e.getMessage());
            }
        });

        btnDecrypt.setOnClickListener(v -> {
            String input = etInput.getText().toString();
            try {
                String result = decrypt(input, getSelectedMethod());
                tvResult.setText("解密结果: " + result);
            } catch (Exception e) {
                tvResult.setText("解密失败: " + e.getMessage());
            }
        });
    }

    private void copyToClipboard() {
        String textToCopy = tvResult.getText().toString();
        if (textToCopy.isEmpty() ||
                textToCopy.equals("解密结果: ") ||
                textToCopy.equals("加密结果: ")) {
            Toast.makeText(this, "没有内容可复制", Toast.LENGTH_SHORT).show();
            return;
        }

        textToCopy = removePrefix(textToCopy, "解密结果: ");
        textToCopy = removePrefix(textToCopy, "加密结果: ");

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    private String removePrefix(String text, String prefix) {
        if (text.startsWith(prefix)) {
            return text.substring(prefix.length());
        }
        return text;
    }

    private void updateButtonVisibility() {
        int checkedId = rgEncryption.getCheckedRadioButtonId();
        boolean showDecrypt = checkedId != R.id.rb_md5;
        btnDecrypt.setVisibility(showDecrypt ? View.VISIBLE : View.GONE);
    }

    private String getSelectedMethod() {
        int checkedId = rgEncryption.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_base64) {
            return "BASE64";
        } else if (checkedId == R.id.rb_aes) {
            return "AES";
        } else if (checkedId == R.id.rb_des) {
            return "DES";
        } else if (checkedId == R.id.rb_md5) {
            return "MD5";
        }
        return "";
    }

    private String encrypt(String input, String method) throws Exception {
        switch (method) {
            case "BASE64":
                return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
            case "AES":
                return AesUtils.encrypt(input);
            case "DES":
                return DesUtils.encrypt(input);
            case "MD5":
                return MD5Util.md5(input);
            default:
                throw new IllegalArgumentException("未知加密方式");
        }
    }

    private String decrypt(String input, String method) throws Exception {
        switch (method) {
            case "BASE64":
                return new String(Base64.decode(input, Base64.DEFAULT));
            case "AES":
                return AesUtils.decrypt(input);
            case "DES":
                return DesUtils.decrypt(input);
            default:
                throw new UnsupportedOperationException("该加密方式不支持解密");
        }
    }
}
