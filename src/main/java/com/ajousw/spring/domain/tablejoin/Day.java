package com.ajousw.spring.domain.tablejoin;

public enum Day {

    MONDAY(0, "월요일"),
    TUESDAY(1, "화요일"),
    WEDNESDAY(2, "수요일"),
    THURSDAY(3, "목요일"),
    FRIDAY(4, "금요일"),
    SATURDAY(5, "토요일"),
    SUNDAY(6, "일요일");

    private final int value;
    private final String label;

    Day(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Day fromValue(int value) {
        for (Day day : Day.values()) {
            if (day.value == value) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid day value: " + value);
    }

    public String getLabel() {
        return this.label;
    }
}