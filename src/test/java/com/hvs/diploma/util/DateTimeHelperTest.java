package com.hvs.diploma.util;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

class DateTimeHelperTest {

    @Test
    void parseTime() {
        String t1 = "22-30";
        String t2 = "17:50";
        String t3 = "31:22";
        ArrayList<String> strings = new ArrayList<>();
        strings.add(t1);
        strings.add(t2);
        strings.add(t3);
        for (String string : strings) {
            try {
                LocalTime localTime = DateTimeHelper.parseTime(string);
                System.out.println(localTime);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}