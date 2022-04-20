package com.example.lotto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int[] ran = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                41, 42, 43, 44, 45, 46}; // 1~45까지의 숫자 배열 지정

    int[] lottoRandom = new int[6]; // 한 회에 6번씩 나오게 할 거기 때문에 6사이즈의 배열 <- 이게 다섯 개 있어야 함(5줄)

    String[] alphabet = {"A", "B", "C", "D", "E"};


    int round = 1; // _ _ _회 글자를 세팅하기 위해서 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNum();
        random();
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

    public void random() {

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
            for(int j=0; j<6; j++) {
                tvNumberArray.append(lottoRandom[j] + "  ");
            }
            tvNumberArray.append("\n\n"); // 한 줄 하고 두 줄 띄우기
        }

    }
}
// 30개의 숫자가 다 다르게 나오게 하는 건 고민해보고..내일...
// 나눔 Lotto 옆에 버튼 하나 추가해서 화면 레이아웃 갤러리에 저장할 수 있는 버튼 생성하기