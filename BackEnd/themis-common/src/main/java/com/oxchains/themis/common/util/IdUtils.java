package com.oxchains.themis.common.util;

import java.util.UUID;

/**
 * @author ccl
 * @time 2017-10-25 17:40
 * @name IdUtils
 * @desc:
 */
public class IdUtils {
    private IdUtils(){}
    public static String getUUID(){
        String uuid= UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

    public static String getUUID10(){
        String[] uuids=UUID.randomUUID().toString().split("-");
        return uuids[uuids.length-1].toUpperCase();
    }


}
