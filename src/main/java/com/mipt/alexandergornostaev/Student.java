package com.mipt.alexandergornostaev;

public interface Student {
    default Object Study(Object obj) {
        return obj;
    }
}
