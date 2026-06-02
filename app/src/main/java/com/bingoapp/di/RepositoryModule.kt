package com.bingoapp.di

import com.bingoapp.data.repository.AuthRepositoryImpl
import com.bingoapp.data.repository.GameRepositoryImpl
import com.bingoapp.data.repository.UserRepositoryImpl
import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
