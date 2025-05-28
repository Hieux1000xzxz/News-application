package com.example.newspaper.common.pojo;

import com.example.newspaper.common.models.TypeEmotion;

public class EmotionCount {
    private TypeEmotion type;
    private long count;

    // Constructor
    public EmotionCount(TypeEmotion type, long count) {
        this.type = type;
        this.count = count;
    }

    // Getter và Setter
    public TypeEmotion getType() {
        return type;
    }

    public void setType(TypeEmotion type) {
        this.type = type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
