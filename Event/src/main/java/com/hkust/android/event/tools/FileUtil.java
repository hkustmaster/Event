package com.hkust.android.event.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sam on 15/12/12.
 */
public class FileUtil {
    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     * @param c
     * //@param mType 资源类型，参照  MultimediaContentType 枚举，根据此类型，保存时可自动归类
     * @param fileName 文件名称
     * @param bitmap 图片
     * @return
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap, String uEmail) {
        return saveFile(c, "", fileName, bitmap, uEmail);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap, String uEmail) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes, uEmail);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes, String uEmail) {
        String fileFullName = "";
        FileOutputStream fos = null;
        //String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = c.getFilesDir()+ "/event/" + uEmail + "/";

                //Log.i("ppppp",filePath);
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        Log.i("pppppp1", fileFullName);
        return fileFullName;
    }

    public static boolean fileIsExists(String urlFile){
        try{
            File f=new File(urlFile);
            if(!f.exists()){
                return false;
            }

        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
}
