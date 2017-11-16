package com.eurus.medicoassistant;

import android.media.Image;

/**
 * Created by Kay on 11/16/2017.
 */

public class Doctor {

    private String name;
    private String qualifications;
    private String speciality;
    private String imgurl;

    public String getImgurl() {
        return imgurl;
    }



    public String getEmail() {
        return email;
    }

    private String email;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
//  private Image display_pic;

}
