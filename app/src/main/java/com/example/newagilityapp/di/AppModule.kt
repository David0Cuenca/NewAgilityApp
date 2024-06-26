package com.example.newagilityapp.di

import android.content.Context
import androidx.room.Room
import com.example.newagilityapp.data.local.AppDatabase
import com.example.newagilityapp.data.local.ProjectDao
import com.example.newagilityapp.data.local.SessionDao
import com.example.newagilityapp.data.local.TaskDao
import com.example.newagilityapp.data.repository.ProjectRepository
import com.example.newagilityapp.data.repository.SessionRepository
import com.example.newagilityapp.data.repository.TaskRepository
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
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            name = "New Agility Database"
        ).build()

    }

    @Provides
    @Singleton
    fun provideProjectDao(appDatabase: AppDatabase): ProjectDao {
        return appDatabase.ProjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.TaskDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(appDatabase: AppDatabase): SessionDao {
        return appDatabase.SessionDao()
    }

    @Provides
    @Singleton
    fun provideProjectRepository(projectDao: ProjectDao): ProjectRepository {
        return ProjectRepository(projectDao)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(sessionDao: SessionDao): SessionRepository {
        return SessionRepository(sessionDao)
    }
}