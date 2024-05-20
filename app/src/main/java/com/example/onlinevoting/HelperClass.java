package com.example.onlinevoting;

public class HelperClass {
    String uname, email, dob, pass;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public HelperClass(String uname, String email, String dob, String pass) {
        this.uname = uname;
        this.email = email;
        this.dob = dob;
        this.pass = pass;
    }

    public HelperClass() {
    }


}