package com.sfu362group2.musicistrivial.activities

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.view_models.GameResultViewModel


class GameResultActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var gameResult: TextView
    private lateinit var song1: TextView
    private lateinit var song2: TextView
    private lateinit var song3: TextView
    private lateinit var song4: TextView
    private lateinit var song5: TextView
    private lateinit var homeButton: Button
    private lateinit var gameResultView: View
    private lateinit var shareToFbButton: Button
    private lateinit var shareDialog: ShareDialog
    private lateinit var callbackManager: CallbackManager
    private lateinit var inBundle: Bundle
    private lateinit var viewModel: GameResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)
        initTextViews()
        initViewModel()

        homeButton = findViewById(R.id.button_to_home)
        homeButton.setOnClickListener {
            finish()
        }

        // Facebook ShareDialog
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)

        shareToFbButton = findViewById(R.id.button_share_to_Facebook)
        shareToFbButton.setOnClickListener { shareToFacebook() }

        if (!isPackageInstalled("com.facebook.katana", this)) {
            shareToFbButton.visibility = View.GONE
        }

    }

    fun isPackageInstalled(packageName: String, context: Context): Boolean {
        return try {
            val packageManager = context.packageManager
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    private fun initTextViews(){
        artistName = findViewById(R.id.artist_name)
        gameResult = findViewById(R.id.game_result_message)
        song1 = findViewById(R.id.rank_1_song)
        song2 = findViewById(R.id.rank_2_song)
        song3 = findViewById(R.id.rank_3_song)
        song4 = findViewById(R.id.rank_4_song)
        song5 = findViewById(R.id.rank_5_song)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(GameResultViewModel::class.java)

        if (intent.extras != null){
            inBundle = intent.extras!!
            viewModel.artistName.value = inBundle.getString(
                getString(R.string.bund_key_artist_name), "")
            viewModel.detailedScore.value = inBundle.getFloatArray(
                getString(R.string.bund_key_detailed_score))!!
            viewModel.correctSongs.value = inBundle.getStringArrayList(
                getString(R.string.bund_key_correct_songs))!!
        }

        viewModel.artistName.observe(this) {
            this.artistName.text = it
        }
        viewModel.correctSongs.observe(this) {
            setText(it)
        }
        viewModel.detailedScore.observe(this) {
            setTextColors(it)
            gameResult.text = getString(R.string.result_message, it.sum())
        }

    }

    private fun setText(correctSongs: ArrayList<String>){
        song1.text = "1 - ${correctSongs[0]}"
        song2.text = "2 - ${correctSongs[1]}"
        song3.text = "3 - ${correctSongs[2]}"
        song4.text = "4 - ${correctSongs[3]}"
        song5.text = "5 - ${correctSongs[4]}"
    }

    private fun setTextColors(detailedScore: FloatArray){
        setScoreColor(song1, detailedScore[0])
        setScoreColor(song2, detailedScore[1])
        setScoreColor(song3, detailedScore[2])
        setScoreColor(song4, detailedScore[3])
        setScoreColor(song5, detailedScore[4])
    }

    private fun setScoreColor(textView: TextView, score: Float){
        when (score) {
            0.0f -> textView.setTextColor(getColor(R.color.red_result))
            0.5f -> textView.setTextColor(getColor(R.color.yellow_result))
            1.0f -> textView.setTextColor(getColor(R.color.green_result))
        }
    }

    private fun shareToFacebook() {
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            gameResultView = findViewById(R.id.game_result_view)
            val screenBitmap = getBitmapFromView(gameResultView)
            val sharePhoto = SharePhoto.Builder().setBitmap(screenBitmap).build()
            val shareContent = SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .setShareHashtag( ShareHashtag.Builder()
                    .setHashtag("#MusicIsTrivialApp")
                    .build()
                )
                .build()
            shareDialog.show(shareContent)
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val drawable = view.background
        if (drawable != null) {
            drawable.draw(canvas)
        } else {
            canvas.drawColor(getColor(R.color.white))
        }
        view.draw(canvas)
        return bitmap
    }
}