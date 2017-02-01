package com.yzkj.common.util;

import java.util.UUID;

/**
 * Created by Mr.Zhou on 2016/12/29.
 */
public class UuidKit {

    public static String generate(){
        return UUID.randomUUID().toString();
    }

}
