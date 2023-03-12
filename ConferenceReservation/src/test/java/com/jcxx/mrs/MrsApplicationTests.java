package com.jcxx.mrs;

import javafx.scene.input.DataFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class MrsApplicationTests {

    @Test
    void contextLoads() throws ParseException {
        String beginTime = "2018-07-28 14:42:32";
        String endTime = "2018-07-29 12:26:32";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = format.parse(beginTime);
            Date date2 = format.parse(endTime);
    }
}
