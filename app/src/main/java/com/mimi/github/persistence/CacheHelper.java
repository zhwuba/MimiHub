package com.mimi.github.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.inject.Inject;

/**
 * Created by zwb on 15-10-15.
 */
public class CacheHelper extends SQLiteOpenHelper {

    private static final String NAME = "cache.db";
    private static final int VERSION = 8;

    @Inject
    public CacheHelper(final Context context){
        super(context,NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE orgs (id INTEGER PRIMARY KEY);");
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "avatarurl TEXT);");
        db.execSQL("CREATE TABLE repos " +
                "(id INTEGER PRIMARY KEY, " +
                "repoId INTEGER, " +
                "orgId INTEGER, " +
                "name TEXT, " +
                "ownerId INTEGER, " +
                "private INTEGER, " +
                "fork INTEGER, " +
                "description TEXT, " +
                "forks INTEGER, " +
                "watchers INTEGER, " +
                "language TEXT, " +
                "hasIssues INTEGER, " +
                "mirrorUrl TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orgs");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS repos");
        onCreate(db);
    }
}
