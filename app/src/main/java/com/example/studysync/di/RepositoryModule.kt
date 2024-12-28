// RepositoryModule.kt
package com.example.studysync.di

import com.example.studysync.data.repository.AuthRepositoryImpl
import com.example.studysync.data.repository.CourseRepositoryImpl
import com.example.studysync.data.repository.UserRepositoryImpl
import com.example.studysync.domain.reposiroty.AuthRepository
import com.example.studysync.domain.reposiroty.CourseRepository
import com.example.studysync.domain.reposiroty.UserRepository

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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        courseRepositoryImpl: CourseRepositoryImpl
    ): CourseRepository
}