package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Comparator;

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.fragment.ChatFragment;
import gachon.termproject.joker.fragment.CommunityFragment;
import gachon.termproject.joker.fragment.HomeFragment;
import gachon.termproject.joker.fragment.MatchingExpertFragment;
import gachon.termproject.joker.fragment.MatchingUserFragment;
import gachon.termproject.joker.fragment.MyInfoFragment;
import gachon.termproject.joker.UserInfo;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private TextView action_bar_title;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment home;
    private CommunityFragment community;
    private MatchingUserFragment matchingUser;
    private MatchingExpertFragment matchingExpert;
    private ChatFragment chat;
    private MyInfoFragment myInfo;
    private int backPressed = 0;
    public static ArrayList<String> userPostsIdList;
    public static ArrayList<PostContent> userPostsList;
    public static ArrayList<String> userCommentsIdList;
    public static ArrayList<PostContent> postsOfCommentsList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 토큰은 기기당 하나에 배정돼서 다른 기기에서 앱 로그인했을 때 새 토큰 줘야함
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.getUserId()).update("pushToken", task.getResult());
                }
            }
        });

        //toolbar~~~~~~~~~~toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        action_bar_title = findViewById(R.id.main_toolbar_textview);
        //toolbar~~~~~~~~~end

        // 하단 내비게이션 바 설정
        bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.community:
                        setFrag(1);
                        break;
                    case R.id.matching:
                        setFrag(2);
                        break;
                    case R.id.chat:
                        setFrag(3);
                        break;
                    case R.id.myInfo:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        // 자기가 작성한 포스트 아이디 가져오기
        Intent intent = getIntent();
        userPostsIdList = intent.getStringArrayListExtra("userPostsIdList");
        userCommentsIdList = intent.getStringArrayListExtra("userCommentsIdList");
        userPostsList = intent.getParcelableArrayListExtra("userPostsList");
        postsOfCommentsList = intent.getParcelableArrayListExtra("postsOfCommentsList");
        if (userPostsIdList == null) userPostsIdList = new ArrayList<>();
        if (userCommentsIdList == null) userCommentsIdList = new ArrayList<>();
        if (userPostsList == null) userPostsList = new ArrayList<>();
        else userPostsList.sort(new Comparator<PostContent>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(PostContent o1, PostContent o2) {
                long o1Id = Long.parseUnsignedLong(o1.getPostId());
                long o2Id = Long.parseUnsignedLong(o2.getPostId());

                if (o1Id < o2Id) return 1;
                else return -1;
            }
        });
        if (postsOfCommentsList == null) postsOfCommentsList = new ArrayList<>();
        else postsOfCommentsList.sort(new Comparator<PostContent>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(PostContent o1, PostContent o2) {
                long o1Id = Long.parseUnsignedLong(o1.getPostId());
                long o2Id = Long.parseUnsignedLong(o2.getPostId());

                if (o1Id < o2Id) return 1;
                else return -1;
            }
        });


        // 일반인인지 전문가인지에 따라 매칭 화면 다르게 설정
        FragmentManager fm = getSupportFragmentManager();
        if (UserInfo.getIsPublic()) { //user라면
            if (matchingUser == null) {
                matchingUser = new MatchingUserFragment();
                fm.beginTransaction().add(R.id.main_frame, matchingUser).commit();
            }
        }
        else {
            if (matchingExpert == null) {
                matchingExpert = new MatchingExpertFragment();
                fm.beginTransaction().add(R.id.main_frame, matchingExpert).commit();
            }
        }

        setFrag(0); // 로그인 후 이동하는 첫 화면을 홈으로 설정
    }

    //back arrow button 눌렀을 때 무조건 home으로
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager(); // 프래그먼트 간의 이동을 도와주는 것

        switch (item.getItemId()) {
            case android.R.id.home:
                if (home != null) fm.beginTransaction().show(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("JOKER");
                actionBar.setDisplayHomeAsUpEnabled(false); //뒤로가기 버튼 없애주깅
                bottomNavigationView.setSelectedItemId(R.id.home);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 한번 누를 시 홈으로 이동
    // 두번 누를 시 앱 종료
    @Override
    public void onBackPressed() {
        if (backPressed == 0) {
            setFrag(0);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            backPressed++;
        }
        else finish();
    }

    // 하단 내비게이션바에서 누른 버튼 작동 함수
    private void setFrag(int n) {
        FragmentManager fm = getSupportFragmentManager(); // 프래그먼트 간의 이동을 도와주는 것

        // 모든 프래그먼트에서 작업했던 내역 유지를 하며 프로세스를 전환시켜주는 코드
        /*
        replace를 써서 프래그먼트를 교체하면 기존의 프로세스를 없애고 새 것으로 교체 하는 행위임.
        따라서 각 프래그먼트는 첫 실행 시에만 스택에 수동으로 추가를 해주고
        프래그먼트 간의 전환이 있으면 숨기기, 보여주기 기능으로 실행시킴.
         */

        //fragment 전환될 때, Action Bar 내용물도 같이 바꿔줍니다

        switch (n) {
            case 0:
                if (home == null) {
                    home = new HomeFragment();
                    fm.beginTransaction().add(R.id.main_frame, home).commit();
                }
                if (home != null) fm.beginTransaction().show(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("JOKER");
                actionBar.setDisplayHomeAsUpEnabled(false); //뒤로가기 버튼 없애주깅

                break;

            case 1:
                if (community == null) {
                    community = new CommunityFragment();
                    fm.beginTransaction().add(R.id.main_frame, community).commit();
                }
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().show(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("커뮤니티");
                actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 생기고 누르면 뒤로감

                break;

            case 2:
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().show(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().show(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("전문가 매칭");
                actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 생기고 누르면 뒤로감

                break;

            case 3:
                if (chat == null) {
                    chat = new ChatFragment();
                    fm.beginTransaction().add(R.id.main_frame, chat).commit();
                }
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().show(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("채팅방");
                actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 생기고 누르면 뒤로감

                break;

            case 4:
                if (myInfo == null) {
                    myInfo = new MyInfoFragment();
                    fm.beginTransaction().add(R.id.main_frame, myInfo).commit();
                }
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().show(myInfo).commit();

                action_bar_title.setText("내 정보");
                actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 생기고 누르면 뒤로감

                break;
        }
    }
}