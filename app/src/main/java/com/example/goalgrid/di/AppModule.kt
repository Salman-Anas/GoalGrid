package com.example.goalgrid.di

import android.content.Context
import androidx.room.Room
import com.example.goalgrid.data.GoalGridDao
import com.example.goalgrid.data.GoalGridDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GoalGridDatabase {
        return Room.databaseBuilder(
            context,
            GoalGridDatabase::class.java,
            "goalgrid_db"
        ).build()
    }

    @Provides
    fun provideDao(database: GoalGridDatabase): GoalGridDao {
        return database.dao()
    }
}
