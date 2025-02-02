package com.depromeet.sloth.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.depromeet.sloth.ui.list.ListFragment
import com.depromeet.sloth.ui.manage.ManageFragment
import com.depromeet.sloth.ui.list.TodayFragment

class SlothFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        when (className) {
            TodayFragment::class.java.name -> {
                return TodayFragment()
            }
            ListFragment::class.java.name -> {
                return ListFragment()
            }
            ManageFragment::class.java.name -> {
                return ManageFragment()
            }
        }

        return super.instantiate(classLoader, className)
    }
}
