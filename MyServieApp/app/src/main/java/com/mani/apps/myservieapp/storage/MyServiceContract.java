package com.mani.apps.myservieapp.storage;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyServiceContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.mani.apps";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Sunshine.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class TodoEntry implements BaseColumns {

        /* Table name used by our database*/
        public static final String TABLE_NAME = "todo";

        /* Date is stored as int representing time of creation of task */
        public static final String COLUMN_DATE = "date";
        /* Task is stored as String representing work to be done */
        public static final String COLUMN_TASK = "task";
        /* Status is stored as boolean representing current status of task*/
        public static final String COLUMN_STATUS = "status";

        /* The base CONTENT_URI used to query the Todo table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        /**
         * Builds a URI that adds the task _ID to the end of the todo content URI path.
         * This is used to query details about a single todo entry by _ID. This is what we
         * use for the detail view query.
         *
         * @param id Unique id pointing to that row
         * @return Uri to query details about a single todo entry
         */
        public static Uri buildTodoUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

}
