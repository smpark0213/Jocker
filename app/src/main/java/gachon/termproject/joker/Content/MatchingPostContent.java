package gachon.termproject.joker.Content;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchingPostContent {
    public String category;
    public String userId;
    public String profileImg;
    public String title;
    public String nickname;
    public String postTime;
    public String postId;
    public String pushToken;
    public String intro;
    public ArrayList<String> location;
    public ArrayList<String> content;
    public ArrayList<String> images;
    public ArrayList<String> locationOfPost;
    public HashMap<String, RequestFromExpertContent> requests = new HashMap<>();
    public boolean isMatched; // 매칭 게시판 작성에 필요한 변수

    public MatchingPostContent() {}

    public MatchingPostContent(String category, String userId, String profileImg, String title, String nickname, String postTime, String postId, String pushToken, String intro, ArrayList<String> location, ArrayList<String> content, ArrayList<String> images, ArrayList<String> locationOfPost, boolean isMatched, HashMap<String, RequestFromExpertContent> requests) {
        this.category = category;
        this.userId = userId;
        this.profileImg = profileImg;
        this.title = title;
        this.nickname = nickname;
        this.postTime = postTime;
        this.postId = postId;
        this.pushToken = pushToken;
        this.intro = intro;
        this.location = location;
        this.content = content;
        this.images = images;
        this.locationOfPost = locationOfPost;
        this.isMatched = isMatched;
        this.requests = requests;
    }

    public String getCategory() { return category; }
    public String getUserId() { return userId; }
    public String getProfileImg() { return profileImg; }
    public String getTitle() {
        return title;
    }
    public String getNickname() { return nickname; }
    public String getPostTime() { return postTime; }
    public String getPostId() { return postId; }
    public String getPushToken() { return pushToken; }
    public String getIntro() { return intro; }
    public ArrayList<String> getLocation() { return location; }
    public ArrayList<String> getContent() { return content; }
    public ArrayList<String> getImages() { return images; }
    public ArrayList<String> getLocationSelected(){ return locationOfPost; }
    public HashMap<String, RequestFromExpertContent> getRequests(){ return requests; }
    public Boolean getIsMatched(){ return isMatched; }
    public void setCategory() { this.category = category; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setProfileImg(String url) { this.profileImg = url; }
    public void setTitle(String title) { this.title = title; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPostTime(String postTime) { this.postTime = postTime; }
    public void setPostId(String postId) { this.postId = postId; }
    public void setContent(ArrayList<String> content) { this.content = content; }
    public void setImages(int index, String image) { this.images.set(index, image); }
    public void setRequests(HashMap<String, RequestFromExpertContent> requestsList) { this.requests = requests; }
    public void setIsMatched(Boolean isMatched) { this.isMatched = isMatched; }
}
