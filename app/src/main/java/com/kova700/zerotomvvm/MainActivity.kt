package com.kova700.zerotomvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

    val homeFragment: HomeFragment by lazy { HomeFragment() }
    val wishFragment: WishFragment by lazy { WishFragment() }

    val homePokeymonList = getDummy()
    val wishPokeymonList: List<PokemonListItem>
        get() = homePokeymonList.filter { it.heart }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentContainer(savedInstanceState)
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_home -> showFragment(homeFragment)
                R.id.bottom_menu_wish -> showFragment(wishFragment)
            }
            true
        }
    }

    private fun initFragmentContainer(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        showFragment(homeFragment)
    }

    private fun showFragment(targetFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container_main, targetFragment)
            .commitNow()
    }

    companion object {
        const val TO_DETAIL_SELECTED_ITEM_EXTRA = "TO_DETAIL_SELECTED_ITEM_EXTRA"
        const val TO_DETAIL_ITEM_POSITION_EXTRA = "TO_DETAIL_ITEM_POSITION_EXTRA"
    }
}