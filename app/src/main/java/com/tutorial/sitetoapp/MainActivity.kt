package com.tutorial.sitetoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
/**
 * Created by KHALID EL MRABTI&Khalid Coding on 16/06/20.
 */
class MainActivity : AppCompatActivity() {

    val url = "https://www.khalidcoding.com/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this lines responsible for fullScreen mode
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // this line for hide app title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        web_view.settings.javaScriptEnabled = true

        // add this line for display images and other resources
        web_view.settings.domStorageEnabled = true

        if (intent.action.equals("new_post")) {
            val urlPost = intent.getStringExtra("url")
            web_view.loadUrl(urlPost)
        } else {
            web_view.loadUrl(url)
        }

        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
    }


    // inflate menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // get menu  click event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.face_page -> gotToFacebookPage()
            R.id.about_us -> goToActivityAbout()
            R.id.share -> shareApp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Site To App")
            var shareMessage = "\nThis Application is amazing \n\n"
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {
            e.toString();
        }
    }

    private fun goToActivityAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun gotToFacebookPage() {
        try {
            packageManager.getPackageInfo("com.facebook.katana", 0)
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    // put your page Id here
                    Uri.parse("fb://page/108333134034924")
                )
            )
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    // put your page url here
                    Uri.parse("https://www.facebook.com/khalidCoding")
                )
            )
        }
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }

}
