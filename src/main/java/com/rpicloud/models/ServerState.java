package com.rpicloud.models;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mixmox on 01/03/16.
 */
public class ServerState {

    private String exception = null;
    private int delay = 0;
    private int amount = 10;


    private ArrayList<Resource> resources;

    public ServerState() {
        resources = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            resources.add(new Resource("Dummy data from service 0"));
        }
    }

    public void invoke() throws Exception {
        if(delay != 0){
            try {
                Thread.sleep(delay);

            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        if (exception != null){
            if(exception.toLowerCase().equals("ioexception")){

                throw new IOException();
            }
            else if(exception.toLowerCase().equals("nullpointerexception")){
                throw new NullPointerException();
            }
            else {
                throw new Exception();
            }

        }
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount > 50)
            this.amount = 50;
        else if(amount > 0)
            this.amount = amount;

    }
}