package my.edu.um.fsktm.unihelp.util;

import java.util.Random;

import my.edu.um.fsktm.unihelp.R;

public class RandomIconGenerator {

    private static final int[] resIds = {
        R.mipmap.bubble,
        R.mipmap.home,
        R.mipmap.store,
    };
    private static final Random index = new Random();

    public static int nextResId() {
        return resIds[index.nextInt(resIds.length)];
    }

    public static int getResId(String id) {
        int index = 0;
        for (char c: id.toCharArray())
            index += c;
        return resIds[index % resIds.length];
    }

}
