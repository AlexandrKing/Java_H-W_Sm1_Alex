package com.mipt.alexandergornostaev;

public abstract class Worker {
    abstract void work(int num);
    boolean goHome(String str1, String str2) {
        return str1.equals(str2);
    }
}
