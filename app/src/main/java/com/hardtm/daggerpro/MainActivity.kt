package com.hardtm.daggerpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hardtm.daggerpro.bash.FragmentBash
import com.hardtm.daggerpro.jokes.FragmentJokes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_one -> showFragmentBash()
                R.id.action_second -> showFragmentJokes()
            }
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        //DaggerProApp.appComponent.inject(this)//TODO Inject Dagger in Activity before onCreate
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorYellow)
        setContentView(R.layout.activity_main)

        setSupportActionBar(daggerProToolbar)
        val actionBar = supportActionBar
        actionBar?.run {
            title = getString(R.string.app_name)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        daggerProToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)


        bottomNav.setOnNavigationItemSelectedListener(navigationListener)
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.action_one)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun showFragmentBash() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerlayout, FragmentBash.newInstance(), "bash")
            .commit()
    }

    fun showFragmentJokes() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerlayout, FragmentJokes.newInstance(), "jokes")
            .commit()
    }
}
