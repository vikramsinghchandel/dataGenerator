package com.vsc.dataGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class waterlineDataUtils {


    public static void main(String args[]) {

        //randomStringChooser();
        dateChooser();

    }

    private static void randomStringChooser() {
        String[] gender = {"Male", "Female"};

        for (int i = 0; i < 10; i++) {

            System.out.println(gender[new Random().nextInt(2)]);

        }
    }


    private static void dateChooser() {

        DateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SSS");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");


        Date date = new Date();
        String onlyDate = onlyDateFormat.format(date);
        String dateTime = dateTimeFormat.format(date);
        String year = yearFormat.format(date);


        System.out.println(onlyDate);
        System.out.println(dateTime);
        System.out.println(year);


    }

}
