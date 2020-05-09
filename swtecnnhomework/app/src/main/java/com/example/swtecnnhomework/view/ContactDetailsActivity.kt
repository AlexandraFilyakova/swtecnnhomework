package com.example.swtecnnhomework.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.swtecnnhomework.R
import com.example.swtecnnhomework.databinding.ActivityContactDetailsBinding
import com.example.swtecnnhomework.model.Contact
import kotlinx.android.synthetic.main.activity_contact_details.*

class ContactDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailsBinding

    private var isNewContact: Boolean = true
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_details)

        setSupportActionBar(toolbar)
        supportActionBar?.subtitle = "Details"

        isNewContact = intent.getBooleanExtra(IS_NEW_CONTACT, true)
        contact = intent.getSerializableExtra(CONTACT) as Contact?

        if (!isNewContact && contact != null) {
            binding.contact = contact
        } else {
            binding.isEnable = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_toolbar, menu)
        if (isNewContact) {
            menu?.getItem(0)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                //Edit
                binding.isEnable = true
            }
            R.id.save -> {
                //Save
                binding.isEnable = false
                Toast.makeText(this, "Contact successfully saved", Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    companion object {
        private const val CONTACT = "CONTACT"
        private const val IS_NEW_CONTACT = "IS_NEW_CONTACT"

        fun getDetailsIntent(context: Context, isNewContact: Boolean, contact: Contact?): Intent {
            return Intent(context, ContactDetailsActivity::class.java).apply {
                putExtra(CONTACT, contact)
                putExtra(IS_NEW_CONTACT, isNewContact)
            }
        }
    }

}