import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _records = MutableLiveData<List<ContentValues>>()
    val records: LiveData<List<ContentValues>> get() = _records

    // sortOrder를 추가하여 정렬된 데이터를 가져오도록 수정
    fun loadRecords(dbHelper: RecordDatabaseHelper, sortOrder: String) {
        val sortedRecords = dbHelper.getAllRecords(sortOrder) // sortOrder를 전달
        _records.value = sortedRecords
    }

    fun updateRecords(newRecords: List<ContentValues>) {
        _records.value = newRecords
    }

}
