package com.sirekanyan.knigopis.feature.login

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.sirekanyan.knigopis.common.extensions.app
import com.sirekanyan.knigopis.databinding.LoginActivityBinding
import com.sirekanyan.knigopis.dependency.providePresenter
import com.sirekanyan.knigopis.feature.startMainActivity

private const val MARKET_URI = "market://details?id="
private const val GOOGLE_PLAY_URI = "https://play.google.com/store/apps/details?id="

fun Context.startLoginActivity() {
    startActivity(Intent(this, LoginActivity::class.java))
}

class LoginActivity : AppCompatActivity(), LoginPresenter.Router {

    val binding by lazy { LoginActivityBinding.inflate(layoutInflater) }
    private val presenter by lazy { providePresenter() }
    private val auth by lazy { app.authRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        presenter.init()
    }

    override fun onResume() {
        super.onResume()
        intent?.data?.findParameter("token")?.let { token ->
            intent = null
            auth.saveToken(token)
            startMainActivity()
        }
    }

    override fun openBrowser(website: Website): Boolean {
        val toolbarColor = ContextCompat.getColor(this, website.color)
        val colorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(toolbarColor)
            .build()
        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colorSchemeParams)
            .build()
        return try {
            customTabsIntent.launchUrl(this, website.uri)
            true
        } catch (ex: ActivityNotFoundException) {
            false
        }
    }

    override fun openMarket(packageName: String) {
        try {
            startActivity(Intent(ACTION_VIEW, Uri.parse(MARKET_URI + packageName)))
        } catch (ex: ActivityNotFoundException) {
            startActivity(Intent(ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URI + packageName)))
        }
    }

    override fun close() {
        finish()
    }

    private fun Uri.findParameter(key: String): String? =
        if (
            scheme == LOGIN_CALLBACK_URI.scheme
            && host == LOGIN_CALLBACK_URI.host
            && path == LOGIN_CALLBACK_URI.path
        ) {
            getQueryParameter(key)
        } else {
            null
        }

}