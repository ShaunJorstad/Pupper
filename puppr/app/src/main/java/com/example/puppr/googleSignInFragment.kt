package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.puppr.databinding.FragmentGoogleSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass.
 */
class googleSignInFragment : Fragment() {
    private lateinit var binding: FragmentGoogleSignInBinding
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGoogleSignInBinding>(
            inflater,
            R.layout.fragment_user_account_types, container, false
        )

        return binding.root
    }

    private fun startSignInFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//        var userToken: String? = null
//
//        try {
//            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
//            if (account != null) userToken = account.idToken
//        } catch (exception: Exception) {
//            Log.d("LOGIN", exception.toString())
//        }

//        viewModel.handleEvent(
//            LoginEvent.OnGoogleSignInResult(
//                LoginResult(
//                    requestCode,
//                    userToken
//                )
//            )
//        )
//    }
}
// --- on create
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
// Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance()
//---

//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("GoogleAuthentication", "Google sign in failed", e)
//                // ...
//            }
//        }
//    }
//
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
//
//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Log.d("GoogleAuthentication", "firebaseAuthWithGoogle:" + acct.id!!)
//
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("GoogleAuthentication", "signInWithCredential:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("GoogleAuthentication", "signInWithCredential:failure", task.exception)
//                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
//                // ...
//            }
//    }

