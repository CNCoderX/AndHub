package com.cncoderx.github;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.cncoderx.github.utils.TimeUtils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String str = "2017-08-10 03:53:51";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = format.parse(str);
        System.out.println(TimeUtils.formatDate(date));
    }
}