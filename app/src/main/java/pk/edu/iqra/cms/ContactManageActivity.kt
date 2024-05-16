package pk.edu.iqra.cms

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pk.edu.iqra.cms.database.AppDatabase
import pk.edu.iqra.cms.database.Contact
import pk.edu.iqra.cms.databinding.ActivityContactManageBinding
import pk.edu.iqra.cms.utils.DataHolder

class ContactManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactManageBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var uContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactManageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        uContact = DataHolder.contact

        if (uContact != null) {
            binding.edName.setText(uContact?.name)
            binding.edMobileNo.setText(uContact?.mobileNo)
            binding.edEmail.setText(uContact?.email)
        }

        binding.btnContact.setOnClickListener {
            handleContact()
        }
    }

    fun handleContact() {
        val name = binding.edName.getUIText()
        val mobileNo = binding.edMobileNo.getUIText()
        val email = binding.edEmail.getUIText()

        if (name.isNotEmpty() && mobileNo.isNotEmpty() && email.isNotEmpty()) {
            val contact = Contact(name = name, mobileNo = mobileNo, email = email)

            if (uContact != null) {
                contact.id = uContact?.id!!
            }

            coroutineScope.launch {
                val id = AppDatabase
                    .getDatabase(this@ContactManageActivity)
                    .contactDao()
                    .insert(contact)

                if (uContact != null) {
                    showToastMessage("Contact information has been updated with ID = $id")
                } else {
                    showToastMessage("Contact information has been added with ID = $id")
                }
                navigateToList()
            }
        } else {
            showToastMessage("Please fill in all contact information")
        }
    }

    private fun EditText.getUIText() = this.text.toString().trim()

    private fun showToastMessage(message: String) {
        Toast.makeText(this@ContactManageActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToList() {
        val intent = Intent(this, ContactListActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
}
