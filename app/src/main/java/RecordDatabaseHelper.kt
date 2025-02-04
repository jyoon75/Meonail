import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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




    // 데이터 가져오기 (홈에서 사용)
    fun getAllRecords(sortOrder: String): List<ContentValues> {
        val records = mutableListOf<ContentValues>()
        val db = readableDatabase
        /*val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE DESC") // 날짜 내림차순*/
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_DATE $sortOrder" // 정렬 순서에 맞춰서 가져오기
        )

        if (cursor.moveToFirst()) {
            do {
                val record = ContentValues().apply {
                    put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                    put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)))
                    put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                    put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
                    put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
                    put(COLUMN_TAGS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)))
                    put(COLUMN_IMAGES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)))
                    put(COLUMN_NOTE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)))
                    put(COLUMN_PRIVATE, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIVATE)))
                }
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }

    // 데이터 가져오기 (추억 탭에서 사용)
    fun getAllRecords(): List<ContentValues> {
        val records = mutableListOf<ContentValues>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val record = ContentValues().apply {
                    put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                    put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)))
                    put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                    put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
                    put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
                    put(COLUMN_TAGS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)))
                    put(COLUMN_IMAGES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)))
                    put(COLUMN_NOTE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)))
                    put(COLUMN_PRIVATE, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIVATE)))
                }
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }
    fun getAllRecordsWithRatingFive(): List<ContentValues> { // 별점이 5인 것만 가져오기
        val records = mutableListOf<ContentValues>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_RATING = ?",
            arrayOf("5.0"),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val record = ContentValues().apply {
                    put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                    put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)))
                    put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                    put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
                    put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
                    put(COLUMN_TAGS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)))
                    put(COLUMN_IMAGES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)))
                    put(COLUMN_NOTE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)))
                    put(COLUMN_PRIVATE, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIVATE)))
                }
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }
    fun getRecordsWithNoteLengthGreaterThan(): List<ContentValues> { // 노트의 길이가 500 이상인 기록만 가져옴
        val records = mutableListOf<ContentValues>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE LENGTH($COLUMN_NOTE) >= 500", null
        )

        if (cursor.moveToFirst()) {
            do {
                val record = ContentValues().apply {
                    put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                    put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)))
                    put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                    put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
                    put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
                    put(COLUMN_TAGS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)))
                    put(COLUMN_IMAGES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)))
                    put(COLUMN_NOTE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)))
                    put(COLUMN_PRIVATE, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIVATE)))
                }
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }



    // 데이터 가져오기 (상세보기 페이지에서 사용)
    fun getRecordById(recordId: Int): ContentValues? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(recordId.toString()),
            null,
            null,
            null
        )
        val record = if (cursor.moveToFirst()) {
            ContentValues().apply {
                put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)))
                put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
                put(COLUMN_TAGS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)))
                put(COLUMN_NOTE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)))
                put(COLUMN_IMAGES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)))
                put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
            }
        } else {
            null
        }
        cursor.close()
        return record
    }

    // 날짜에 따라 이미지 내보내기
    fun getImageUrisForDate(date: String): List<String> {
        val uriList = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $COLUMN_IMAGES FROM $TABLE_NAME WHERE $COLUMN_DATE = ?", arrayOf(date))

        if (cursor.moveToFirst()) {
            val uris = cursor.getString(0)
            uriList.addAll(uris.split(","))
        }
        cursor.close()
        return uriList
    }

    // 카테고리 탭
    fun getCategoriesWithCount(): Map<String, Int> {
        val categoryCountMap = mutableMapOf<String, Int>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CATEGORY, COUNT(*) FROM $TABLE_NAME GROUP BY $COLUMN_CATEGORY", null)

        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(0) ?: "기타"
                val count = cursor.getInt(1)
                categoryCountMap[category] = count
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categoryCountMap
    }

    fun getRecordsByCategory(category: String, sortOrder: String): List<ContentValues> {
        val records = mutableListOf<ContentValues>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_CATEGORY = ?",
            arrayOf(category),
            null,
            null,
            "$COLUMN_DATE $sortOrder"
        )

        if (cursor.moveToFirst()) {
            do {
                val record = ContentValues().apply {
                    put(COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                    put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)))
                    put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                    put(COLUMN_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
                    put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
                    put(COLUMN_TAGS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)))
                    put(COLUMN_IMAGES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)))
                    put(COLUMN_NOTE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)))
                    put(COLUMN_PRIVATE, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIVATE)))
                }
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }


    // 기록상세 수정 메뉴
    fun deleteRecord(id: Int): Boolean {
        val db = writableDatabase
        val deletedRows = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return deletedRows > 0
    }


    // 기록 수정
    fun updateRecord(values: ContentValues, recordId: Int): Int {
        val db = writableDatabase
        val selection = "${COLUMN_ID} = ?"
        val selectionArgs = arrayOf(recordId.toString())
        return db.update(TABLE_NAME, values, selection, selectionArgs)
    }




}
