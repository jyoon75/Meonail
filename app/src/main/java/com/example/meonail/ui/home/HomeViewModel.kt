import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _records = MutableLiveData<List<ContentValues>>()
    val records: LiveData<List<ContentValues>> get() = _records

    fun loadRecords(dbHelper: RecordDatabaseHelper) {
        _records.value = dbHelper.getAllRecords()
    }

    fun updateRecords(newRecords: List<ContentValues>) {
        _records.value = newRecords
    }

}
