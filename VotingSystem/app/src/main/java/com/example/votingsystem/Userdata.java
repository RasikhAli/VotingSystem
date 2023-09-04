package com.example.votingsystem;

public class Userdata
{
    public String fullName, age, email, password, section, session, semester, type, Img, Key;

    public Userdata()
    {

    }

    public Userdata(String fullName, String age, String email, String password, String section, String session, String semester, String type, String img, String key)
    {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.section = section;
        this.session = session;
        this.semester = semester;
        this.type = type;
        this.Img = img;
        this.Key = key;
    }

    public Userdata(String fullName, String age, String Img, String password)
    {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.password = password;
    }
}
