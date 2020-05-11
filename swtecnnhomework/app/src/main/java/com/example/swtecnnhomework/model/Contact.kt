package com.example.swtecnnhomework.model

import java.io.Serializable

class Contact(val id: Long,
              var firstname: String,
              var lastname: String,
              var phone: String,
              var email: String) : Serializable