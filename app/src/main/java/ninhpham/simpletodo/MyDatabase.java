package ninhpham.simpletodo;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ninhp on 5/19/2017.
 */

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "MyDatabase";

    public static final int VERSION = 1;
}