package com.example.bzoom;

public class OwnerBelongings {
    boolean status;
    String name;
    int id;

    public OwnerBelongings(boolean status, String name) {
        this.status = status;
        this.name = name;
    }

    public OwnerBelongings() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
