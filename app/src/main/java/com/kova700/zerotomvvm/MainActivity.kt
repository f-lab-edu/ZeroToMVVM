package com.kova700.zerotomvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val homeFragment = HomeFragment()
        val wishFragment = WishFragment()

        with(supportFragmentManager) {
            beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.container_main, wishFragment, WISH_FRAGMENT_TAG)
                .hide(wishFragment)
                .commitNow()

            beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.container_main, homeFragment, HOME_FRAGMENT_TAG)
                .commitNow()
        }
    }

    private fun showFragment(fragmentTag: String) {
        if (fragmentTag !in fragmentTagList) throw Exception("Unknown Fragment")

        val targetFragment = getFragmentByTag(fragmentTag)
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach { fragmentInTransaction ->
            if (fragmentInTransaction !== targetFragment) transaction.hide(fragmentInTransaction)
        }
        transaction.show(targetFragment)
        transaction.commitNow()
        renewItemList(targetFragment)
    }

    private fun renewItemList(targetFragment: Fragment) {
        when (targetFragment) {
            is HomeFragment -> {
                targetFragment.homeAdapter.submitList(homePokeymonList.toList())
            }

            is WishFragment -> {
                targetFragment.wishAdapter.submitList(wishPokeymonList.toList())
            }
        }
    }

    private fun getFragmentByTag(tag: String) =
        supportFragmentManager.findFragmentByTag(tag) ?: throw Exception("Unknown Fragment Tag")

    companion object {
        const val HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG"
        const val WISH_FRAGMENT_TAG = "WISH_FRAGMENT_TAG"
        val fragmentTagList = listOf(HOME_FRAGMENT_TAG, WISH_FRAGMENT_TAG)
        const val TO_DETAIL_EXTRA = "selectedPokeymon"
    }
}