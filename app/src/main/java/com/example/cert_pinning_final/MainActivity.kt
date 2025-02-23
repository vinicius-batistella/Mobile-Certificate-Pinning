package com.example.cert_pinning_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

class MainActivity : ComponentActivity() {

    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var loginButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {

            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            val loginRequest = LoginRequest(username, password)

            val retrofit = Retrofit.provideRetrofit(this).create(ApiService::class.java)
            val request = retrofit.login(loginRequest)

            request.enqueue(object : Callback<LoginRequest> {
                override fun onResponse(
                    call: Call<LoginRequest>,
                    response: Response<LoginRequest>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "[*] Login Successful!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@MainActivity, SuccessActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@MainActivity, "[!] Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginRequest>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "[-] Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.i("Certificate Error","Error: ${t.printStackTrace()}")
                }

            })

        }

    }
}

class SuccessActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
    }
}
