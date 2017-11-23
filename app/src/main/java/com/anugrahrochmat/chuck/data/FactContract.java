package com.anugrahrochmat.chuck.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FactContract {

    // Content Authority
    public static final String CONTENT_AUTHORITY = "com.anugrahrochmat.chuck";
    // Base Content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Path directory
    public static final String PATH_FACT = "fact";

    public static final class FactEntry implements BaseColumns {

        // Content URI for query fact table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FACT).build();

        public static final String TABLE_NAME = "fact";
        public static final String COLUMN_FACT_ID = "fact_id";
        public static final String COLUMN_FACT_URL = "url";
        public static final String COLUMN_FACT_VALUE = "value";

        public static final String[] FACT_COLUMNS = {
                COLUMN_FACT_ID,
                COLUMN_FACT_URL,
                COLUMN_FACT_VALUE,
        };
    }
}
