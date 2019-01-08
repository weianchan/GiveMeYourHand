package student.assignment.taruc.givemeyourhand;

import java.util.Date;

public class Comment {

    private String Content;
    private String Date;
    private String Owner;
    private String Post;

    public Comment(String content, String date, String owner, String post) {
        Content = content;
        Date = date;
        Owner = owner;
        Post = post;
    }

    public Comment() {
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }









}
