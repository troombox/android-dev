package com.example.afinal;

public class Fact {
    public static final int FACT_TYPE_CAT = 0;
    public static final int FACT_TYPE_DOG = 1;

    private int factID;
    private int factType;
    private String factText;

    public Fact(int factID, int factType, String factText){
        this.factID = factID;
        this.factType = factType;
        this.factText = factText;
    }

    public Fact(){
        this(0,0,"");
    }

    public int getFactID() {
        return factID;
    }

    public void setFactID(int factID) {
        this.factID = factID;
    }

    public int getFactType() {
        return factType;
    }

    public void setFactType(int factType) {
        this.factType = factType;
    }

    public String getFactText() {
        return factText;
    }

    public void setFactText(String factText) {
        this.factText = factText;
    }

    @Override
    public String toString() {
        return "Fact{" +
                "factID=" + factID +
                ", factType=" + factType +
                ", factText='" + factText + '\'' +
                '}';
    }
}
