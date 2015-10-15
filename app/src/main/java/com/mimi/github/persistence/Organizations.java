package com.mimi.github.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.google.inject.Inject;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwb on 15-10-15.
 */
public class Organizations implements PersistableResource<User> {

    private final UserService userService;

    private final OrganizationService orgService;

    /**
     * Create organizations cache with services to load from
     *
     * @param orgService
     * @param userService
     */
    @Inject
    public Organizations(OrganizationService orgService, UserService userService) {
        this.orgService = orgService;
        this.userService = userService;
    }


    @Override
    public Cursor getCursor(SQLiteDatabase readableDatabase) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("orgs JOIN users ON (orgs.id = users.id)");
        return builder
                .query(readableDatabase, new String[] { "users.id",
                                "users.name", "users.avatarurl" }, null, null, null,
                        null, null);
    }

    @Override
    public User loadFrom(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setLogin(cursor.getString(1));
        user.setAvatarUrl(cursor.getString(2));
        return user;
    }

    @Override
    public void store(SQLiteDatabase db, List<User> orgs) {
        db.delete("orgs", null, null);
        if (orgs.isEmpty())
            return;

        ContentValues values = new ContentValues(3);
        for (User user : orgs) {
            values.clear();

            values.put("id", user.getId());
            db.replace("orgs", null, values);

            values.put("name", user.getLogin());
            values.put("avatarurl", user.getAvatarUrl());
            db.replace("users", null, values);
        }
    }

    @Override
    public List<User> request() throws IOException {
        User user = userService.getUser();
        List<User> orgs = orgService.getOrganizations();
        List<User> all = new ArrayList<User>(orgs.size() + 1);
        all.add(user);
        all.addAll(orgs);
        return all;
    }
}
