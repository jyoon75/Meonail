import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecordDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "RecordDB"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "records"
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_RATING = "rating"
        const val COLUMN_TAGS = "tags"
        const val COLUMN_IMAGES = "images"
        const val COLUMN_NOTE = "note"
        const val COLUMN_PRIVATE = "is_private"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_TITLE TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_RATING REAL,
                $COLUMN_TAGS TEXT,
                $COLUMN_IMAGES TEXT,
                $COLUMN_NOTE TEXT,
                $COLUMN_PRIVATE INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRecord(record: ContentValues): Long {
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, record)
    }
}
