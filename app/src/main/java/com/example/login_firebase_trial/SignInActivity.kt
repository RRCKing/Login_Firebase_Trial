package com.example.login_firebase_trial

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.login_firebase_trial.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {

    // Declare the data binding
    private lateinit var binding: ActivitySignInBinding

    // Declare the Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Declare Google Sign In Client
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set the onClickListener for the Not Register and Sign Up TextView
        binding.tvNotRegister.setOnClickListener {
            /* Set the intent from this event to the SignUpActivity if the TextView on click
                then the the intent to start the SignUpActivity */
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Set the on click event for the Email Sign In button
        binding.btnSignIn.setOnClickListener {
            // Set the binding of edit text of email and password to the variables respectively
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            // Check if the email and password both are not empty, prompt a toast message if empty
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                    // Call the Sign In function and pass the input email and password for authentication
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                        // If sign in successfully, using the intent to start the MainActivity, otherwise prompt a toast message
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("email", email)
                            intent.putExtra("name", email.substringBefore("@"))
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "Empty Fields Are not Allowed" , Toast.LENGTH_SHORT).show()
            }
        }

        /* Google Sign In */
        // Configure the Google Sign In API and assign to the gso variable
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initialize Google Sign In client by using getClient method
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Retrieve the Google Sign In button and set the on click listnser to call the signInGoogle function
        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
            signInGoogle()
        }
    }

    // Put the SignIn Intent of Google Sign In Client to the launcher
    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // Create a launcher and put the StartActivityForResult as the activity result input
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        // Run a function that checks if a result is ok, getting the data of the sign in account and putting it to the handleResults function
        result->
            if (result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    // Handle result data
    private fun handleResults(task: Task<GoogleSignInAccount>){
        /* If the task is completed successfully, and task result is not null,
        setting it as the account variable and putting it to the update UI function,
        otherwise prompt a toast message */
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // Handle the UI change from the Google Sign In Account
    private fun updateUI(account: GoogleSignInAccount){
        // Set the account ID token as the credential variable
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        // Sign In to the Firebase and set what should be done if complete or prompt a toast message if not complete
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                // If it is successful, set the intent to have ability to call the MainActivity
                val intent : Intent = Intent(this, MainActivity::class.java)
                //  Put the account email and account display name data to the intent
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                // Start the activity with the intent
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}