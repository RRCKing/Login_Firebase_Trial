package com.example.login_firebase_trial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Declare the Firebase Authentication
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Retrieve the account email and display name from the intent passed in
        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")

        // Retrieve the Account Info TextView and set the account info there
        findViewById<TextView>(R.id.textAccountInfo).text = displayName + "\n" + email

        // Retrieve the Sign Out button and set the on click listener to sign out
        findViewById<Button>(R.id.btnSignOut).setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}