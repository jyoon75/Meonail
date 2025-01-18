import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE recordTBL (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + //고유 아이디
            "title TEXT, " + //제목
            "category TEXT, " + //카테고리
            "date TEXT, " + //기록 날짜 (yyyy-MM-dd 형식으로 저장하기)
            "rating INTEGER, " + //평점
            "isFavorite INTEGER, " + //즐겨찾기 여부 (1이면 즐겨찾기, 0이면 아님)
            "tags TEXT, " + //태그
            "photoPath TEXT, " + //사진 경로
            "memo TEXT, " + //메모
            "isPublic INTEGER" + //공개 여부(1이면 공개, 0이면 비공개)
            ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}