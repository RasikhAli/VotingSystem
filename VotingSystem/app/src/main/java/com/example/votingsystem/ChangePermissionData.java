package com.example.votingsystem;

public class ChangePermissionData
{
    String Img, Key, age, email, fullName, password, section, semester, session, type;

    ChangePermissionData()
    {

    }

    ChangePermissionData(String Img, String age, String email, String fullName, String password, String section, String semester, String session, String type)
    {
        this.Img = Img;
        this.age = age;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.section = section;
        this.semester = semester;
        this.session = session;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }
    public String getSection() {
        return section;
    }
    public String getSemester() {
        return semester;
    }
    public String getSession() {
        return session;
    }
    public String getType() {
        return type;
    }
    public String getKey() {
        return Key;
    }
    public String getAge() {
        return age;
    }
    public String getEmail() {
        return email;
    }
    public String getImg() {
        return Img;
    }
    public String getPassword() {
        return password;
    }
}


