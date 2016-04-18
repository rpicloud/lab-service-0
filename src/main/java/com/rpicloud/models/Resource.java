package com.rpicloud.models;

/**
 * Created by mixmox on 01/03/16.
 */
public class Resource {
    String data;

    public Resource(String data) {
        this.data = data;
    }


    public Resource() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
