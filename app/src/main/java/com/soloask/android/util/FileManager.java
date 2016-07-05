package com.soloask.android.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Lebron on 2016/7/2.
 */
public class FileManager {
    public static String getFilePath(String fileName) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoloAsk/" + fileName;
    }

    public static boolean isFileExits(String fileName) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoloAsk/" + fileName;
            File file = new File(path);
            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
