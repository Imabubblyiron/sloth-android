package com.depromeet.sloth.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.depromeet.sloth.R
import com.depromeet.sloth.data.PreferenceManager
import com.depromeet.sloth.databinding.ActivityHomeBinding
import com.depromeet.sloth.ui.list.ListFragment
import com.depromeet.sloth.ui.manage.ManageFragment
import com.depromeet.sloth.ui.list.TodayFragment

class HomeActivity : AppCompatActivity() {

    private val pm: PreferenceManager by lazy { PreferenceManager(this) }

    lateinit var binding: ActivityHomeBinding
    lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accessToken = pm.getAccessToken().toString()

        supportFragmentManager.fragmentFactory = SlothFragmentFactory()

        initNavigationEvent()
    }

    private fun initNavigationEvent() {
        binding.navigationView.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_today -> changeFragment(TodayFragment::class.java.name)
                    R.id.menu_class -> changeFragment(ListFragment::class.java.name)
                    R.id.menu_mypage -> changeFragment(ManageFragment::class.java.name)
                }

                true
            }
            this.selectedItemId = R.id.menu_today
        }
    }

    private fun changeFragment(className: String) {
        val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, className)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}