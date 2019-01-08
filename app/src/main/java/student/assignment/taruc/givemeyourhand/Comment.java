package student.assignment.taruc.givemeyourhand;

public class Comment {


    private String content;
    private String date;
    private String owner;
    private String post;

    public Comment(String content, String date, String owner, String post) {
        this.content = content;
        this.date = date;
        this.owner = owner;
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
