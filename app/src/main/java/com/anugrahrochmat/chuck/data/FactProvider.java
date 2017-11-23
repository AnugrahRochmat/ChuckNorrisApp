package com.anugrahrochmat.chuck.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FactProvider extends ContentProvider {

    public static final int CODE_FACT = 100;
    public static final int CODE_FACT_WITH_ID = 101;

    private FactDbHelper factDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // URI = content://com.anugrahrochmat.chuck/fact/
        matcher.addURI(FactContract.CONTENT_AUTHORITY, FactContract.PATH_FACT, CODE_FACT);

        // URI = content://com.anugrahrochmat.chuck/fact/{fact_id}
        // * wildcard for string, # wildcard for number
        matcher.addURI(FactContract.CONTENT_AUTHORITY, FactContract.PATH_FACT + "/*", CODE_FACT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        factDbHelper = new FactDbHelper(getContext());
        return true;
    }

    /**
     * Method to handle query request
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = factDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case CODE_FACT_WITH_ID:
                String lastPathSegment = uri.getLastPathSegment();
                String[] mSelectArgs = new String[]{lastPathSegment};

                retCursor = db.query(FactContract.FactEntry.TABLE_NAME,
                        projection,
                        FactContract.FactEntry.COLUMN_FACT_ID + " = ? ",
                        mSelectArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_FACT:
                retCursor = db.query(FactContract.FactEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * Method to handle update request
     * Not Yet Implemented
     * @param uri
     * @return
     */
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Method to handle insert request
     * @param uri
     * @param contentValues
     * @return
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = factDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case CODE_FACT:
                long id = db.insert(FactContract.FactEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    retUri = ContentUris.withAppendedId(FactContract.FactEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return retUri;
    }

    /**
     * Method to handle delete request
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = factDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int factDeleted;

        switch (match) {
            case CODE_FACT_WITH_ID:
                String lastPathSegment = uri.getLastPathSegment();
                String[] mSelectArgs = new String[]{lastPathSegment};

                factDeleted = db.delete(FactContract.FactEntry.TABLE_NAME,
                        FactContract.FactEntry.COLUMN_FACT_ID + " = ? ",
                        mSelectArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (factDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return factDeleted;
    }

    /**
     * Method to handle update request
     * Not Yet Implemented
     * @param uri
     * @param contentValues
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
