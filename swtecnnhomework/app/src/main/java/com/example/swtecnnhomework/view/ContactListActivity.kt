package com.example.swtecnnhomework.view

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtecnnhomework.R
import com.example.swtecnnhomework.model.Contact
import com.example.swtecnnhomework.repository.ContactRepository
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception


class ContactListActivity : AppCompatActivity(), ContactAdapter.ContactAdapterListener {

    private lateinit var contactRepository: ContactRepository

    private var progressDialogCallCount: Int = 0
    private val progressDialog: ProgressDialog by lazy {
        val progressDialog = ProgressDialog(this, R.style.ProgressDialog)
        progressDialog.setCancelable(false)
        progressDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        setSupportActionBar(toolbar)
        supportActionBar?.subtitle = getString(R.string.list_of_contacts)

        requestCallPhonePermission()

        contactRepository = ContactRepository.getInstance(this)

        contactList.layoutManager = LinearLayoutManager(this)
        contactList.adapter = ContactAdapter(this)
        (contactList.adapter as ContactAdapter).setContactAdapterListener(this)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    if (s.length >= MIN_SEARCH_LENGTH) {
                        loadSearchContacts(searchText = s.toString())
                    }
                } else {
                    loadContacts()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        clearButton.setOnClickListener {
            searchEditText.setText("")
            loadContacts()
        }
    }

    override fun onResume() {
        super.onResume()
        loadContacts()
    }

    private fun loadContacts() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                showProgressDialog(true)
                GlobalScope.async { contactRepository.getAllContacts() }.await().let {
                    (contactList.adapter as ContactAdapter).setContacts(it)
                    searchLayout.visibility = View.VISIBLE
                }
            } catch (ex: Exception) {
                Toast.makeText(this@ContactListActivity, ex.message, Toast.LENGTH_LONG).show()
            } finally {
                showProgressDialog(false)
            }
        }
    }

    private fun loadSearchContacts(searchText: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                showProgressDialog(true)
                GlobalScope.async { contactRepository.getSearchResults(searchText) }.await().let {
                    (contactList.adapter as ContactAdapter).setContacts(it)
                    searchLayout.visibility = View.VISIBLE
                }
            } catch (ex: Exception) {
                Toast.makeText(this@ContactListActivity, ex.message, Toast.LENGTH_LONG).show()
            } finally {
                showProgressDialog(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addContact) {
            startActivityForResult(ContactDetailsActivity.getDetailsIntent(this, true, null), RC_HANDLE_DETAILS)
        }
        return true
    }

    override fun onInfoButtonClick(contact: Contact) {
        startActivityForResult(ContactDetailsActivity.getDetailsIntent(this, false, contact), RC_HANDLE_DETAILS)
    }

    override fun onItemClick(contact: Contact) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + contact.phone)
            startActivity(intent)
        } else {
            requestCallPhonePermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_HANDLE_DETAILS) {
            if (resultCode == Activity.RESULT_OK) {
                val contact = data?.getSerializableExtra(ContactDetailsActivity.UPDATED_CONTACT) as Contact
                contactRepository.updateContact(contact)
                loadContacts()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showProgressDialog(show: Boolean, force: Boolean = false) {
        if (show) {
            if (!isFinishing && !isDestroyed) {
                progressDialog.show()
            }
            progressDialogCallCount++
        } else if (force) {
            progressDialogCallCount = 0
            progressDialog.dismiss()
        } else if (progressDialogCallCount > 0) {
            progressDialogCallCount--
            if (!isFinishing && !isDestroyed && progressDialogCallCount == 0) {
                progressDialog.dismiss()
            }
        }
    }

    private fun requestCallPhonePermission() {
        val permissions = arrayOf(Manifest.permission.CALL_PHONE)
        ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_PHONE_CALL_PERM)
    }

    companion object {
        private const val RC_HANDLE_PHONE_CALL_PERM = 56
        private const val RC_HANDLE_DETAILS = 77
        private const val MIN_SEARCH_LENGTH = 3
    }

}