package com.sfu362group2.musicistrivial.activities

import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.games.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PlayGamesAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sfu362group2.musicistrivial.R


class SignInActivity : AppCompatActivity() {


    private var achievementClient: AchievementsClient? = null
    private var leaderboardClient: LeaderboardsClient? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gamesSignInClient: GamesSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var signInButton: Button
    private lateinit var leaderboardButton: Button
    private lateinit var achievementButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        achievementClient = PlayGames.getAchievementsClient(this)
        leaderboardClient = PlayGames.getLeaderboardsClient(this)


        achievementButton = findViewById(R.id.achievement_button)
        achievementButton.setOnClickListener {
            showAchievements()
        }
        leaderboardButton = findViewById(R.id.leaderboard_button)
        leaderboardButton.setOnClickListener {
            showLeaderboard()
        }
//        achievementClient?.unlock("id")
//        leaderboardClient?.submitScore("id", 100)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
            initPlayGames()

        }

    }

    private fun initPlayGames() {
        gamesSignInClient = PlayGames.getGamesSignInClient(this)
        gamesSignInClient.isAuthenticated.addOnCompleteListener { isAuthenticatedTask: Task<AuthenticationResult> ->
            val isAuthenticated = isAuthenticatedTask.isSuccessful &&
                    isAuthenticatedTask.result.isAuthenticated
            if (isAuthenticated) {
                // Continue with Play Games Services
            } else {
                // Disable your integration with Play Games Services or show a
                // login button to ask  players to sign-in. Clicking it should
                // call GamesSignInClient.signIn().
                PlayGames.getGamesSignInClient(this).signIn();
            }
        }

    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        println("debug: currents user: ${currentUser?.displayName.toString()}")

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //case google sign in
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//            }
//        }
        //case play game sign in
//        const val PG_SIGN_IN = 1002    --- random number code
        if (requestCode == RC_SIGN_IN) {
            println("passed PG_SIGN_IN onActivityResult condition check")

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithPlayGames(account)
                println("debug:finish onActivityResult try scope")
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "play games sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {
        // Call this function both in the silent sign-in task's OnCompleteListener and in the
        // Activity's onActivityResult handler.
        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.id!!)

        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    println("debug: play games user: ${user?.displayName}")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //leaderboard show
    private val RC_LEADERBOARD_UI = 9004
     fun showLeaderboard() {
        PlayGames.getLeaderboardsClient(this)
            .getLeaderboardIntent(getString(R.string.leaderboard_id))
            .addOnSuccessListener { intent -> startActivityForResult(intent, RC_LEADERBOARD_UI) }
    }
    //achievement show
    private val RC_ACHIEVEMENT_UI = 9003
    fun showAchievements() {
        PlayGames.getAchievementsClient(this)
            .achievementsIntent
            .addOnSuccessListener { intent -> startActivityForResult(intent, RC_ACHIEVEMENT_UI) }
    }

    //sign out
//    fun signOut(){
//        Firebase.auth.signOut()
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        signOut()
//    }


    companion object {
        //code for google sign in
        const val RC_SIGN_IN = 1001

        //code for play games sign in
        const val PG_SIGN_IN = 1002
        const val EXTRA_NAME = "EXTRA NAME"
    }


}