package com.ajousw.spring.domain.tablejoin;

import lombok.Data;

@Data
public class EmptyTimeTable {
    public static final int dayOfWeek = 7;
    public final int maxDayTime = 288;

    private byte[][] byteArray = new byte[dayOfWeek][maxDayTime];
}
