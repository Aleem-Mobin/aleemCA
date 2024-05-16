package pk.edu.iqra.cms.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("Id") var id:Long = 0,
    @ColumnInfo("Name") var name:String,
    @ColumnInfo("MobileNo") var mobileNo:String,
    @ColumnInfo("Email") var email:String
)
