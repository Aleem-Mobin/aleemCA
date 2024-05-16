package pk.edu.iqra.cms.listener

import pk.edu.iqra.cms.database.Contact

interface ListAction {
    fun onClick(contact: Contact)
}