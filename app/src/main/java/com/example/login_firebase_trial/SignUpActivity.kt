package com.example.login_firebase_trial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.login_firebase_trial.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class SignUpActivity : AppCompatActivity() {

    // Declare the data binding
    private lateinit var binding:ActivitySignUpBinding

    // Declare the Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set the onClickListener for the going back to Sign In Activity TextView
        binding.tvAlreadyRegister.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // Set the on click event for the Sign Up button
        binding.btnSignUp.setOnClickListener {

            // Assign the email, password, and confirm password to the variables
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()

            // Check if the inputs are not empty, or else prompt a toast message
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                // Check if the password match the confirm password
                if (pass == confirmPass) {
                    // Create the user account
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        // If successful, set the intent to go to the SignIn Activity
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        }else{
                            // If not successful, prompt a toast message
                            Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    Toast.makeText(this, "Password is not matching" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty Fields Are not Allowed" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}