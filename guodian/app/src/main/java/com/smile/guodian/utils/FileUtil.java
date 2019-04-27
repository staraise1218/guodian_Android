package com.smile.guodian.utils;

import android.os.Environment;

import com.smile.guodian.base.Constants;

import java.io.File;


public class FileUtil {
    /**
     * 获取SD卡的路径
     *
     * @return sd卡路径
     */
    public static String getSDCardBasePath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取图片缓存的路径
     *
     * @return 图片缓存file
     */
    public static File getCacheFolder() {
        String path = FileUtil.getSDCardBasePath()
                + Constants.CACHE_FILE_PATH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


}
