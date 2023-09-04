package com.example.votingsystem;

public class Electiondata
{
    public String Title, name1, name2, email1, email2, roll1, roll2, Votes1, Votes2, userID, Status, Img1, Img2;

    public Electiondata()
    {

    }
    public Electiondata(String title, String name1, String name2,
                        String email1, String email2, String roll1, String roll2,
                        String votes1, String votes2, String uID, String status,
                        String img1, String img2)
    {
        this.Title = title;
        this.name1 = name1;
        this.name2 = name2;
        this.email1= email1;
        this.email2= email2;
        this.roll1 = roll1;
        this.roll2 = roll2;
        this.Votes1 = votes1;
        this.Votes2 = votes2;
        this.userID = uID;
        this.Status = status;
        this.Img1 = img1;
        this.Img2 = img2;
    }

//    public String getTitle() {
//        return Title;
//    }
//    public String getVotes1() {
//        return Votes1;
//    }
//    public String getVotes2() {
//        return Votes2;
//    }
//    public String getImg1() {
//        return Img1;
//    }
//    public String getImg2() {
//        return Img2;
//    }
//
//    public void setTitle(String title) {
//        Title = title;
//    }
    public void setVotes1(String votes1) {
        votes1 = votes1;
    }
    public void setVotes2(String votes2) {
        votes2 = votes2;
    }
//    public void setImg1(String img1) {
//        Img1 = img1;
//    }
//    public void setImg2(String img2) {
//        Img2 = img2;
//    }
}
