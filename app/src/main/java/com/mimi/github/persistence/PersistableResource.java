package com.mimi.github.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.List;

/**
 * Created by zwb on 15-10-15.
 */
public interface PersistableResource<E> {

    /**
     * @param readableDatabase
     * @return a cursor capable of reading the required information out of the
     *         database.
     */
    Cursor getCursor(SQLiteDatabase readableDatabase);

    /**
     * @param cursor
     * @return a single item, read from this row of the cursor
     */
    E loadFrom(Cursor cursor);

    /**
     * Store supplied items in DB, removing or updating prior entries
     *
     * @param writableDatabase
     * @param items
     */
    void store(SQLiteDatabase writableDatabase, List<E> items);

    /**
     * Request the data directly from the GitHub API, rather than attempting to
     * load it from the DB cache.
     *
     * @return list of items
     * @throws IOException
     */
    List<E> request() throws IOException;
}
