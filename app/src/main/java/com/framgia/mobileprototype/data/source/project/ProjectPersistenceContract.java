/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framgia.mobileprototype.data.source.project;

import android.provider.BaseColumns;

public final class ProjectPersistenceContract {
    private ProjectPersistenceContract() {
    }

    public static abstract class ProjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_WIDTH = "width";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_ORIENTATION = "orientation";
        public static final String COLUMN_NAME_POSTER = "poster";
    }
}
