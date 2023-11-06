package com.kova700.zerotomvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kova700.zerotomvvm.FragmentTags.HOME_FRAGMENT_TAG
import com.kova700.zerotomvvm.FragmentTags.WISH_FRAGMENT_TAG

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

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
                R.id.bottom_menu_home -> showFragment(HOME_FRAGMENT_TAG)
                R.id.bottom_menu_wish -> showFragment(WISH_FRAGMENT_TAG)
            }
            true
        }
    }

    private fun initFragmentContainer(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        showFragment(HOME_FRAGMENT_TAG)
    }

    private fun showFragment(tag: FragmentTags) {
        val targetFragment = getFragmentInstanceByTag(tag)

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container_main, targetFragment, tag.name)
            .commitNow()
    }

    companion object {
        const val TO_DETAIL_SELECTED_ITEM_EXTRA = "TO_DETAIL_SELECTED_ITEM_EXTRA"
        const val TO_DETAIL_ITEM_POSITION_EXTRA = "TO_DETAIL_ITEM_POSITION_EXTRA"
    }
}