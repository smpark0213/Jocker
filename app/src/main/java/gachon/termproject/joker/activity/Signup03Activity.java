package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class Signup03Activity extends AppCompatActivity {
    public static String nickname; // 회원가입을 위한 전역변수
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup03_nickname);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        Button nextButton = findViewById(R.id.signup03_button01);
        EditText nicknameText = findViewById(R.id.signup03_edittext01);
        tv = findViewById(R.id.signup03_tv);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임을 입력받음
                String temp = nicknameText.getText().toString();

                if (temp.length() == 0)
                    tv.setText("닉네임을 입력해주세요");
                else if (temp.length() > 6)
                    tv.setText("닉네임은 6자이하로 설정해주세요");
                else { //데이터베이스에서 중복되는 닉네임 있는지 확인!!!
                    FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                List<DocumentSnapshot> list = querySnapshot.getDocuments();

                                if (list.size() == 0) {
                                    nickname = temp;
                                    startActivity(new Intent(getApplicationContext(), Signup04Activity.class));
                                } else {
                                    for (int i = 0; i < list.size(); i++) {
                                        DocumentSnapshot snapshot = list.get(i);
                                        String nicknameCheck = snapshot.getString("nickname");
                                        if (temp.compareTo(nicknameCheck) == 0) {
                                            tv.setText("중복된 닉네임 입니다");
                                            break;
                                        } else if (i == list.size() - 1) {
                                            nickname = temp;
                                            startActivity(new Intent(getApplicationContext(), Signup04Activity.class));
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}