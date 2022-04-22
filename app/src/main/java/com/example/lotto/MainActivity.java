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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int[] ran = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                41, 42, 43, 44, 45}; // 1~45까지의 숫자 배열 지정

    int[] lottoRandom = new int[6]; // 한 회에 6번씩 나오게 할 거기 때문에 6사이즈의 배열 <- 이게 다섯 개 있어야 함(5줄)

    String[] alphabet = {"A", "B", "C", "D", "E"};


    int round = 1; // _ _ _회 글자를 세팅하기 위해서 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        initNum();

    }

    public void initNum() {

        TextView tvRound = (TextView) findViewById(R.id.tvRound); // _ _ _회 알려주기 위해

        tvRound.setText("제 " + round + "회");

        TextView tvPublishedDate = (TextView) findViewById(R.id.tvPublishedDate); // 발행일
        TextView lotteryDate = (TextView) findViewById(R.id.lotteryDate); // 추첨일
        TextView tvPaymentDeadline = (TextView) findViewById(R.id.tvPaymentDeadline); // 지급일

        SimpleDateFormat sdPublishedDate = new SimpleDateFormat("yy/M/d (E) HH:mm:ss"); // 년도/월/일 (요일) 몇시:몇분:몇초 형식으로 포맷하겠다.
        SimpleDateFormat sdLotteryDate = new SimpleDateFormat("yy/M/d (E)"); // 년도/월/일 (요일)로 포맷하겠다.
        SimpleDateFormat sdPaymentDeadline = new SimpleDateFormat("yy/M/d"); // 년도/월/일 형식인데 지급 기한은 발행일로부터 1년 일주일의 기간

        Calendar cLotteryDate = Calendar.getInstance(); // 추첨날을 위한 Calendar 변수 생성, 우선 현재 날짜를 가져옴
        cLotteryDate.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // 추첨날은  매주 토요일

        Calendar cPaymentDeadline = Calendar.getInstance(); // 지급 기한을 위한 새로운 Calendar 변수 생성, 이것 또한 우선 현재 날짜를 가져옴
        cPaymentDeadline.add(Calendar.YEAR, 1);

        tvPublishedDate.setText("발 행 일 : " + sdPublishedDate.format(new Date()));
        lotteryDate.setText("추 첨 일 : " + sdLotteryDate.format(cLotteryDate.getTime()));
        tvPaymentDeadline.setText("지급 기한 : " + sdPaymentDeadline.format(cPaymentDeadline.getTime()));

        // round 올리는 건 더 연구해야됨.

    }

    public void onClickCapture(View view) {

        RelativeLayout rlCaptureLayout = (RelativeLayout) findViewById(R.id.rlCaptureLayout); // 캡쳐할 영역의 레이아웃을 가져옴
        SimpleDateFormat sdf = new SimpleDateFormat("yy/M/d_E"); // 연도/월/일_요일 로 파일명 지정
        LayoutCapture(rlCaptureLayout, sdf.format(new Date()));

    }

    public void random() { // 레이아웃 다 바꾸고 실행하면 여기가 문제가 됩니다..

        TextView tvNumberArray = (TextView) findViewById(R.id.tvNumberArray);

        for(int i = 0; i < ran.length; i++ ){
            int random1 = (int) (Math.random() * 45 + 1); // 원래 random 범위가 0<=random<1인데 *45+1을 해 줌으로서 1<=random<46으로 1에서 45까지가 랜덤으로 나올 수 있게 함
            int deduplication1 = 0; // 중복 제거를 위해 deduplication 이라는 변수 선언
            deduplication1 = ran[i];
            ran[i] = ran[random1];
            ran[random1] = deduplication1;
        }
        lottoRandom = Arrays.copyOf(ran, 30); // lottoRandom 은 ran 배열을 복사한 배열
        for(int i = 0; i<5; i++ ){
            tvNumberArray.append(alphabet[i] + " 자 동 ");
            for(int j=0; j<lottoRandom.length; j++) {
                Log.i("jeongmin", "j 의 값 : " + j);
                if(j % 5 == 0) {
                    tvNumberArray.append("\n");
                    Log.i("jeongmin", "5로 나눴을 때 나머지 0임!!! j 값 : " + j);
                    Log.i("jeongmin", "lottoRandom[" + j + "] : " + lottoRandom[j]);
                }
                tvNumberArray.append(lottoRandom[j] + "  ");
                // 22. 4. 21(목) 수정 중인데 됐다 안됐다 난리..
            }
            tvNumberArray.append("\n\n"); // 한 줄 하고 두 줄 띄우기
        }

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
            Toast.makeText(getApplicationContext(), "갤러리 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("jeongmin", "<try - catch 의 catch 문 속>");
            Log.i("jeongmin", "파일을 찾을 수 없습니다.");
        }
        // 이미지 스캐닝 해서 갤러리에서 보이게 해 주는 코드
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(tempFile)));
    }
}