package com.example.layoutmanagergroup.Config;

public class ItemConfig {

    public static final int DEFAULT_SHOW_ITEM = 5;
    public static final float DEFAULT_SCALE = 0.1f;
    public static final float DEFAULT_TRANSLATE_Y = 20;
    public static final float DEFAULT_TRANSLATE_X = 10;

    public static final int SLIDING_NONE = 1;
    public static final int SLIDING_LEFT = 1 << 2;
    public static final int SLIDING_RIGHT = 1 << 3;
    public static final int SLIDING_UP = 1 << 3;
    public static final int SLIDING_DOWN = 1 << 4;

    public static final int SLIDED_LEFT = 1;
    public static final int SLIDED_RIGHT = 1 << 2;
    public static final int SLIDED_UP = 1 << 3;
    public static final int SLIDED_DOWN = 1 << 4;

    public static final float DEFAULT_ROTATE_DEGREE = 15f;


    public static final int SORT_UP = 1;
    public static final int SORT_DOWN = 2;
    public static final int SORT_LEFT = 3;
    public static final int SORT_RIGHT = 4;

    public static int sortType = SORT_DOWN;

    public static void setSortType(int sortType) {
        ItemConfig.sortType = sortType;
    }

    public static int getSortType() {
        return sortType;
    }

}
