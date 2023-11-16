package com.kova700.zerotomvvm.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kova700.zerotomvvm.R
import com.kova700.zerotomvvm.databinding.ActivityMainBinding
import com.kova700.zerotomvvm.view.main.home.HomeFragment
import com.kova700.zerotomvvm.view.main.wish.WishFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding

    private val homeFragment by lazy { HomeFragment() }
    private val wishFragment by lazy { WishFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragmentContainer(savedInstanceState, homeFragment)
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_home -> showFragment(homeFragment)
                R.id.bottom_menu_wish -> showFragment(wishFragment)
            }
            true
        }
    }

    private fun initFragmentContainer(savedInstanceState: Bundle?, firstFragment: Fragment) {
        if (savedInstanceState != null) return
        showFragment(firstFragment)
    }

    private fun showFragment(targetFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container_main, targetFragment)
            .commitNow()
    }

    companion object {
        const val TO_DETAIL_SELECTED_ITEM_EXTRA = "TO_DETAIL_SELECTED_ITEM_EXTRA"
    }
}