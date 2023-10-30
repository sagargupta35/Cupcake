package com.example.cupcake.ui.theme

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cupcake.data.LoginUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.yield

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

     private var firstName by mutableStateOf("")
     private var lastName by mutableStateOf("")
     private var email by mutableStateOf("")
     private var password by mutableStateOf("")
    private var checkBox: Boolean by mutableStateOf(false)
    var loggedIn: Boolean by mutableStateOf(false)
        private set

    var failure: String by mutableStateOf("")

    fun updateFirstName(name: String) {this.firstName = name
        Log.d("TAG", "updated first name")
    }
    fun updateLastName(name:String)  {
        lastName = name
        Log.d("TAG", "updated last name")
    }
    fun updateEmail(email:String)  {
        this.email = email
        Log.d("TAG", "updated email")
    }
    fun updatePassword(password:String)  {
        this.password = password
        Log.d("TAG", "updated  password")
    }

    fun updateCheckBox(){
        checkBox = !checkBox
        Log.d("TAG", "updated check box")
    }

    fun canRegister(): Boolean{
        return setOf(firstName.isNotEmpty(), lastName.isNotEmpty(), email.isNotEmpty(), checkBox).all { it }
    }

    fun canLogin(): Boolean{
        return setOf(email.isNotEmpty(), password.isNotEmpty()).all { it }
    }

    fun createUserInFireBase(navigateToStart: () -> Unit){
        if(email.isNotEmpty() && password.isNotEmpty()) {

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    Log.d("TAG", "flag changed")
                    if(it.isSuccessful) {
                        loggedIn = true
                        if(loggedIn) Log.d("TAG", "logged in successfully")
                        failure = ""
                        navigateToStart()
                    }
                }
                .addOnFailureListener {
                    try {
                        Log.d("TAG", "failed to register")
                        it.localizedMessage?.let { it1 -> Log.d("TAG", it1) }
                        Log.d("TAG", "failed to register")
                        failure = it.localizedMessage?.toString() ?: ""

                    } catch (e: Exception) {
                       Log.d("TAG", "${e.printStackTrace()}")
                       Log.d("TAG", "${e.printStackTrace()}")
                    }
                }
        } else{
            Log.d("TAG", "failed to register  email empty")
        }
    }


    fun logOut(){
        loggedIn = false
        failure = ""

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val authStateListener = AuthStateListener{
            if(it.currentUser == null){
                Log.d("TAG", "Inside signout successfull")
            } else{
                Log.d("TAG", "signout failed")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun logIn(navigateToStart: () -> Unit){
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d("TAG", "login successfull")
                if(it.isSuccessful) {
                    loggedIn = true
                    navigateToStart()
                }
            }
            .addOnFailureListener{
                Log.d("TAG", "failed: ${it.message}")
                Log.d("TAG", "failed: ${it.localizedMessage}")
                failure = it.localizedMessage?.toString() ?: ""
                loggedIn = false
            }
    }

}