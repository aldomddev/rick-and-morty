package br.com.amd.rickandmorty.di

import android.content.Context
import androidx.room.Room
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRickAndMortyDatabase(@ApplicationContext context: Context): RickAndMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickAndMortyDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideCharactersDao(database: RickAndMortyDatabase) = database.getCharactersDao()
}