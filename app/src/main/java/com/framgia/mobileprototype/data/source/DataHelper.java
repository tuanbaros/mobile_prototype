package com.framgia.mobileprototype.data.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.framgia.mobileprototype.data.source.element.ElementPersistenceContract;
import com.framgia.mobileprototype.data.source.mock.MockPersistenceContract;
import com.framgia.mobileprototype.data.source.project.ProjectPersistenceContract;

public class DataHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mobileprototype.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_PROJECT_ENTRIES =
        "CREATE TABLE " + ProjectPersistenceContract.ProjectEntry.TABLE_NAME + " (" +
            ProjectPersistenceContract.ProjectEntry._ID + INTEGER_TYPE + " PRIMARY KEY " +
            "AUTOINCREMENT NOT NULL," +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
            COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_WIDTH + INTEGER_TYPE + COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_ORIENTATION + TEXT_TYPE +
            COMMA_SEP +
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_POSTER + TEXT_TYPE + COMMA_SEP +
            " UNIQUE (" + ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TITLE + ")" +
            " )";
    private static final String SQL_CREATE_MOCK_ENTRIES =
        "CREATE TABLE " + MockPersistenceContract.MockEntry.TABLE_NAME + " (" +
            MockPersistenceContract.MockEntry._ID + INTEGER_TYPE + " PRIMARY KEY " +
            "AUTOINCREMENT NOT NULL," +
            MockPersistenceContract.MockEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
            MockPersistenceContract.MockEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            MockPersistenceContract.MockEntry.COLUMN_NAME_NOTE + TEXT_TYPE + COMMA_SEP +
            MockPersistenceContract.MockEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP +
            MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID + TEXT_TYPE +
            " )";
    private static final String SQL_CREATE_ELEMENT_ENTRIES =
        "CREATE TABLE " + ElementPersistenceContract.ElementEntry.TABLE_NAME + " (" +
            ElementPersistenceContract.ElementEntry._ID + INTEGER_TYPE + " PRIMARY KEY " +
            "AUTOINCREMENT NOT NULL," +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_X + INTEGER_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_Y + INTEGER_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_WIDTH + INTEGER_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_LINK_TO + TEXT_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_TRANSITION + TEXT_TYPE + COMMA_SEP +
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_MOCK_ID + TEXT_TYPE +
            " )";
    private static final String SQL_DROP_PROJECT_ENTRIES =
        "DROP TABLE IF EXISTS " + ProjectPersistenceContract.ProjectEntry.TABLE_NAME;
    private static final String SQL_DROP_MOCK_ENTRIES =
        "DROP TABLE IF EXISTS " + MockPersistenceContract.MockEntry.TABLE_NAME;
    private static final String SQL_DROP_ELEMENT_ENTRIES =
        "DROP TABLE IF EXISTS " + ElementPersistenceContract.ElementEntry.TABLE_NAME;
    protected static final long INSERT_ERROR = -1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PROJECT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_MOCK_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ELEMENT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DROP_PROJECT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DROP_MOCK_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DROP_ELEMENT_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
