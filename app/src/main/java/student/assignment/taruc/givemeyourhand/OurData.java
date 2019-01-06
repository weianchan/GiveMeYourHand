package student.assignment.taruc.givemeyourhand;

import android.widget.ImageView;

public class OurData {

    private String mOwner;
    private String mTitle;
    private String mContent;
    private String mBankAcc;
    private String mContact;
    private String mImage1;
    private String mImage2;
    private String mImage3;
    private String mComment;

    public OurData(){} //Need for database

    public OurData(String mOwner, String mTitle, String mContent, String mBankAcc, String mContact, String mImage1, String mImage2, String mImage3, String mComment)
    {
        this.mOwner = mOwner;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mBankAcc = mBankAcc;
        this.mContact = mContact;
        this.mImage1 = mImage1;
        this.mImage2 = mImage2;
        this.mImage3 = mImage3;
        this.mComment = mComment;
    }

    public String getOwner() { return mOwner; }

    public void setOwner(String owner) { mOwner = owner; }

    public String getTitle() { return mTitle; }

    public void setTitle(String title) { mTitle = title; }

    public String getContent() { return mContent; }

    public void setContent(String content) { mContent = content; }

    public String getBankAcc() { return mBankAcc; }

    public void setBankAcc(String bankAcc) { mBankAcc = bankAcc; }

    public String getContact() { return mContact; }

    public void setContact(String contact) { mContact = contact; }

    public String getImage1() { return mImage1; }

    public void setImage1(String image1) { mImage1 = image1; }

    public String getImage2() { return mImage2; }

    public void setImage2(String image2) { mImage2 = image2; }

    public String getImage3() { return mImage3; }

    public void setImage3(String image3) { mImage3 = image3; }

    public String getComment() { return mComment; }

    public void setComment(String comment) {mComment = comment; }

}


