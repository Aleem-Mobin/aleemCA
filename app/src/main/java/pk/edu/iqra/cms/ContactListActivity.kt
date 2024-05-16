package pk.edu.iqra.cms

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pk.edu.iqra.cms.adapter.ContactAdapter
import pk.edu.iqra.cms.database.AppDatabase
import pk.edu.iqra.cms.database.Contact
import pk.edu.iqra.cms.databinding.ActivityContactListBinding
import pk.edu.iqra.cms.listener.ListAction
import pk.edu.iqra.cms.utils.DataHolder

class ContactListActivity : AppCompatActivity(), ListAction {
    private lateinit var binding: ActivityContactListBinding
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeRV()

        binding.btnManageContact.setOnClickListener {
            addContact()
        }
    }

    private fun initializeRV() {
        adapter = ContactAdapter(arrayListOf(), this)
        binding.rvContactList.layoutManager = LinearLayoutManager(this)
        binding.rvContactList.adapter = adapter
        // Initially hide RecyclerView and show "No contacts added yet" message
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        GlobalScope.launch(Dispatchers.IO) {
            val contacts = AppDatabase.getDatabase(this@ContactListActivity).contactDao().getAllContacts()
            launch(Dispatchers.Main) {
                adapter.updateList(contacts)
                updateUI()
            }
        }
    }

    private fun updateUI() {
        if (adapter.itemCount > 0) {
            // Show RecyclerView if there are contacts
            binding.rvContactList.visibility = android.view.View.VISIBLE
            binding.textNoContacts.visibility = android.view.View.GONE
        } else {
            // Show "No contacts added yet" message if no contacts
            binding.rvContactList.visibility = android.view.View.GONE
            binding.textNoContacts.visibility = android.view.View.VISIBLE
        }
    }

    override fun onClick(contact: Contact) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(this)
            .setTitle("Choose Action")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> editContact(contact)
                    1 -> deleteContact(contact)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun addContact() {
        DataHolder.contact = null
        navigateToManage()
    }

    private fun navigateToManage() {
        val intent = Intent(this, ContactManageActivity::class.java)
        startActivity(intent)
    }

    private fun deleteContact(contact: Contact) {
        GlobalScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(this@ContactListActivity).contactDao().delete(contact)
            refreshList()
        }
    }

    private fun editContact(contact: Contact) {
        DataHolder.contact = contact
        navigateToManage()
    }
}
