package com.kova700.zerotomvvm.view.detail.presenter

import kotlinx.coroutines.CoroutineScope

interface DetailContract {
    interface View {
        val lifecycleScope: CoroutineScope
    }

    interface Presenter {
        suspend fun updatePokemonHeart(targetPokemonNum: Int, heartValue: Boolean)
    }
}