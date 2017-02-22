package com.framgia.mobileprototype.data.source.mock;

import android.provider.BaseColumns;

public final class MockPersistenceContract {
    private MockPersistenceContract() {
    }

    public static abstract class MockEntry implements BaseColumns {
        public static final String TABLE_NAME = "mocks";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_PROJECT_ID = "project_id";
    }
}
