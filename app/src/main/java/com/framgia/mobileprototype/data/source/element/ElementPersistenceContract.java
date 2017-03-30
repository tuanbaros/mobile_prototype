package com.framgia.mobileprototype.data.source.element;

import android.provider.BaseColumns;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.local
 */
public final class ElementPersistenceContract {
    private ElementPersistenceContract() {
    }

    public static abstract class ElementEntry implements BaseColumns {
        public static final String TABLE_NAME = "elements";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
        public static final String COLUMN_NAME_WIDTH = "width";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_LINK_TO = "link_to";
        public static final String COLUMN_NAME_TRANSITION = "transition";
        public static final String COLUMN_NAME_GESTURE = "gesture";
        public static final String COLUMN_NAME_MOCK_ID = "mock_id";
    }
}
