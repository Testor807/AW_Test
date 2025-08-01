package com.example.aw_test;

public class StringC {
    public void main(String[] args){
        String text = "临沂";
        String anotherText = "临沂";

        if (text.equals(anotherText)) {
            System.out.println("兩個中文字符相同");
        } else {
            System.out.println("兩個中文字符不同");
        }
    }
}
