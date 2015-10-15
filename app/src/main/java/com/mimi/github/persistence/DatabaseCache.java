package com.mimi.github.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwb on 15-10-15.
 */
public class DatabaseCache {
    private static final String TAG = "DatabaseCache";

    @Inject
    private Provider<CacheHelper> helperProvider;

    protected SQLiteDatabase getWritable(SQLiteOpenHelper helper) {
        try {
            return helper.getWritableDatabase();
        } catch (SQLiteException e1) {
            // Make second attempt
            try {
                return helper.getWritableDatabase();
            } catch (SQLiteException e2) {
                return null;
            }
        }
    }

    protected SQLiteDatabase getReadable(SQLiteOpenHelper helper) {
        try {
            return helper.getReadableDatabase();
        } catch (SQLiteException e1) {
            // Make second attempt
            try {
                return helper.getReadableDatabase();
            } catch (SQLiteException e2) {
                return null;
            }
        }
    }

    /**
     * Load or request given resources
     *
     * @param persistableResource
     * @return resource
     * @throws IOException
     */
    public <E> List<E> loadOrRequest(PersistableResource<E> persistableResource)
            throws IOException {
        SQLiteOpenHelper helper = helperProvider.get();
        try {
            List<E> items = loadFromDB(helper, persistableResource);
            if (items != null) {
                Log.d(TAG, "CACHE HIT: Found " + items.size() + " items for "
                        + persistableResource);
                return items;
            }
            return requestAndStore(helper, persistableResource);
        } finally {
            helper.close();
        }
    }

    /**
     * Request and store given resources
     *
     * @param persistableResource
     * @return resources
     * @throws IOException
     */
    public <E> List<E> requestAndStore(
            PersistableResource<E> persistableResource) throws IOException {
        SQLiteOpenHelper helper = helperProvider.get();
        try {
            return requestAndStore(helper, persistableResource);
        } finally {
            helper.close();
        }
    }

    private <E> List<E> requestAndStore(final SQLiteOpenHelper helper,
                                        final PersistableResource<E> persistableResource)
            throws IOException {
        final List<E> items = persistableResource.request();

        final SQLiteDatabase db = getWritable(helper);
        if (db == null)
            return items;

        db.beginTransaction();
        try {
            persistableResource.store(db, items);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return items;
    }

    private <E> List<E> loadFromDB(final SQLiteOpenHelper helper,
                                   final PersistableResource<E> persistableResource) {
        final SQLiteDatabase db = getReadable(helper);
        if (db == null)
            return null;

        Cursor cursor = persistableResource.getCursor(db);
        try {
            if (!cursor.moveToFirst())
                return null;

            List<E> cached = new ArrayList<E>();
            do
                cached.add(persistableResource.loadFrom(cursor));
            while (cursor.moveToNext());
            return cached;
        } finally {
            cursor.close();
        }
    }

}
