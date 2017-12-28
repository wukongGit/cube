package com.sunc.cube.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sunc.cube.App;

import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

public class DBUtils {
    private static final String DEFAULT_DB_NAME = "CubeDB";
    private static Book BOOK;

    static {
        Paper.init(App.getAppContext());
        create(DEFAULT_DB_NAME);
    }

    public static <T> Book write(@NonNull String key, @NonNull T value) {
        try {
            return BOOK.write(key, value);
        } catch (Throwable e) {
            return BOOK;
        }
    }

    public static <T> T read(@NonNull String key) {
        return read(key, null);
    }

    public static <T> T read(@NonNull String key, @Nullable T defaultValue) {
        try {
            return BOOK.read(key, defaultValue);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public static void delete(@NonNull String key) {
        try {
            BOOK.delete(key);
        } catch (Throwable e) {
        }
    }

    public static boolean exist(@NonNull String key) {
        try {
            return BOOK.exist(key);
        } catch (Throwable e) {
            return false;
        }
    }

    public static List<String> getAllKeys() {
        try {
            return BOOK.getAllKeys();
        } catch (Throwable e) {
            return null;
        }
    }

    public static void destroy() {
        try {
            BOOK.destroy();
        } catch (Throwable e) {
        }
    }

    public static void create(@NonNull String name) {
        BOOK = Paper.book(name);
    }
}
