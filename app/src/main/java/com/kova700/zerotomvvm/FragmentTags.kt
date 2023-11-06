package com.kova700.zerotomvvm

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

enum class FragmentTags {
    HOME_FRAGMENT_TAG, WISH_FRAGMENT_TAG
}

private fun getFragmentNewInstanceByTag(tag: FragmentTags): Fragment {
    return when (tag) {
        FragmentTags.HOME_FRAGMENT_TAG -> HomeFragment.newInstance()
        FragmentTags.WISH_FRAGMENT_TAG -> WishFragment.newInstance()
    }
}

fun AppCompatActivity.getFragmentInstanceByTag(tag: FragmentTags) =
    supportFragmentManager.findFragmentByTag(tag.name) ?: getFragmentNewInstanceByTag(tag)