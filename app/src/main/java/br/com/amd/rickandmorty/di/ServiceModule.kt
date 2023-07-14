package br.com.amd.rickandmorty.di

import br.com.amd.rickandmorty.BuildConfig
import br.com.amd.rickandmorty.data.local.database.RickAndMortyDatabase
import br.com.amd.rickandmorty.data.remote.api.RickAndMortyApi
import br.com.amd.rickandmorty.data.repository.RickAndMortyRepository
import br.com.amd.rickandmorty.data.repository.RickAndMortyRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            clientBuilder.addInterceptor(interceptor)
        }

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRickAndMortyApi(
        okHttpClient: OkHttpClient
    ): RickAndMortyApi {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(RickAndMortyApi.BASE_API)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(RickAndMortyApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRickAndMortyRepository(
        repositoryImpl: RickAndMortyRepositoryImpl
    ): RickAndMortyRepository
}