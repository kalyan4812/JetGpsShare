package com.saikalyandaroju.jetgpsshare.Pojos;

public class Contacts {
    private String contactname,phone;
public Contacts(){

}
    public Contacts(String contactname, String phone) {
        this.contactname = contactname;
        this.phone = phone;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
