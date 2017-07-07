package com.tyaer.net.phantomjs;

import java.io.File;

/**
 * Created by Twin on 2017/3/22.
 */
public class Test {
    public static void main(String[] args) {
        try {
//            String s = FileUtils.readFileToString(new File("./x/x"));
//            System.out.println(s);
            System.out.println(new File("./x/x").getAbsolutePath());
            System.out.println(System.getProperty("user.dir"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
