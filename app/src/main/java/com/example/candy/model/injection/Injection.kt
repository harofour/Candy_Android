package com.example.candy.model.injection

import android.content.Context
import com.example.candy.model.api.ApiProvider
import com.example.candy.model.repository.ChallengeListRepository
import com.example.candy.model.repository.ChallengeListRepositoryImpl

object Injection{
    fun provideRepoRepository(context: Context): ChallengeListRepository {
        return ChallengeListRepositoryImpl(
            ApiProvider.provideChallengeApi(),
            context
        )
    }
}