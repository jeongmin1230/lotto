package com.example.lotto;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    public void checkPermission() {
        // storage 접근 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    public void onClickCapture(View view) {

        RelativeLayout rl_capture = (RelativeLayout) findViewById(R.id.rlCaptureLayout); // 캡쳐할 영역의 레이아웃을 가져옴

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss"); // 현재년도몇월며칠_몇시몇분몇초 형식으로 포맷하겠다

        LayoutCapture(rl_capture, sdf.format(new Date()));

    }

    // LayoutCapture 클래스 생성
    public void LayoutCapture(View view, String title) {
        if(view == null) {
            Log.i("jeongmin", "<LayoutCapture 클래스 안> 뷰가 비어있을 경우의 예외처리");
            Log.i("jeongmin", "<LayoutCapture 클래스 안>view is null!!");
            return;
        }
        Log.i("jeongmin", "<LayoutCapture 클래스 안>캡쳐 파일 저장 영역");
        Log.i("jeongmin", "<LayoutCapture 클래스 안>캐시 비트맵 만들기");
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        FileOutputStream fos = null;

        // 파일 저장
        String strPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/lotto";

        Log.i("jeongmin", "<LayoutCapture 클래스 안>경로 : " + strPath);

        File uploadFolder = new File(strPath);
        Log.i("jeongmin", "<LayoutCapture 클래스 안>업로드 경로 설정 : view_capture 라는 폴더 생성해서 그 폴더에 저장 하려고 위에처럼 경로 설정" + uploadFolder);

        String fileName = title + ".jpg";

        File tempFile = new File(strPath, fileName);

        try {
            if(!uploadFolder.exists()) { // 만약 지정 폴더가 없으면 생성
                uploadFolder.mkdirs();
            }

            fos = new FileOutputStream(tempFile); // 경로 + 제목 + .jpg 로 FileOutputStream Setting
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(getApplicationContext(), "이미지 파일을 생성했습니다.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("jeongmin", "<try - catch 의 catch 문 속>");
            Log.i("jeongmin", "파일을 찾을 수 없습니다.");
        }
        // 이미지 스캐닝 해서 갤러리에서 보이게 해 주는 코드
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(tempFile)));
    }
}