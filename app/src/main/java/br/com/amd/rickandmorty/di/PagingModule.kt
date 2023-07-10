package br.com.amd.rickandmorty.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.local.model.CharacterEntity
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.paging.CharactersRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideCharacterPaging(
        database: RickAndMortyDatabase,
        api: RickAndMortyApi
    ): Pager<Int, CharacterEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharactersRemoteMediator(
                rickAndMortyDb = database,
                rickAndMortyApi = api
            ),
            pagingSourceFactory = {
                database.getCharactersDao().pagingSource()
            }
        )
    }
}