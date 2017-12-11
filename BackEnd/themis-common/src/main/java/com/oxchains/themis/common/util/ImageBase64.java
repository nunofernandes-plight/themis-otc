package com.oxchains.themis.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ccl
 * @time 2017-11-03 17:36
 * @name ImageBase64
 * @desc:
 */
public class ImageBase64 {
    private static final String JPG = "data:image/jpeg;base64,";
    private static final String PNG = "data:image/png;base64,";
    /**
     * 将base64编码字符串转换为图片
     * @param imgStr base64编码字符串
     * @param path 图片路径-具体到文件
     * @return
     */
    public static boolean generateImage(String imgStr, String path){
        if(imgStr == null){
            return false;
        }
        if(imgStr.contains(JPG)){
            imgStr = imgStr.replaceAll(JPG, "");
        }
        if(imgStr.contains(PNG)){
            imgStr = imgStr.replaceAll(PNG,"");
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i= 0; i<b.length; i++){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static byte[] getImageBytes(String imgStr){
        if(imgStr == null){
            return null;
        }
        if(imgStr.contains(JPG)){
            imgStr = imgStr.replaceAll(JPG, "");
        }
        if(imgStr.contains(PNG)){
            imgStr = imgStr.replaceAll(PNG,"");
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i= 0; i<b.length; i++){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }

            return b;
        }catch (Exception e){
            return null;
        }
    }



    /**
     * 根据图片地址转换为base64编码字符串
     * @param file
     * @return
     */
    public static String generateImageStr(String file){
        InputStream inputStream =null;
        byte[] data =null;
        try {
            inputStream=new FileInputStream(file);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
