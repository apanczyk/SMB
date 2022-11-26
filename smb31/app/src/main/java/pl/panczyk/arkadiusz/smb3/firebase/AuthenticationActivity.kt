package pl.panczyk.arkadiusz.smb3.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pl.panczyk.arkadiusz.smb3.MainActivity
import pl.panczyk.arkadiusz.smb3.R
import pl.panczyk.arkadiusz.smb3.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        createRegisterButton()
        createLoginButton()
    }

    private fun createRegisterButton() {
        binding.registerButton.setOnClickListener {
            withExceptionWrapped {
                auth.createUserWithEmailAndPassword(
                    binding.emailText.text.toString(),
                    binding.passwordText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Can't be registered", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun createLoginButton() {
        binding.loginButton.setOnClickListener {
            withExceptionWrapped {
                auth.signInWithEmailAndPassword(
                    binding.emailText.text.toString(),
                    binding.passwordText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Can't be logged", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun withExceptionWrapped(operation: () -> Any) {
        try {
            operation()
        } catch (err: Exception) {
            Toast.makeText(this, "Exception occured ${err.message}", Toast.LENGTH_LONG).show()
        }
    }
}