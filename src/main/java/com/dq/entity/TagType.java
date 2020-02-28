package com.dq.entity;

/**
 * Tags of TF_IDF and Hiearchical method 
 * 
 * @author DaiQing
 */
public enum TagType {
	TF_IDF_033(0), TF_IDF_050(1), TF_IDF_060(2), TF_IDF_080(3), HIER(4);
	
	private final int value;
	
    TagType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
