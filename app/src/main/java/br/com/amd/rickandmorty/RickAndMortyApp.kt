package br.com.amd.rickandmorty

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.05)
                    .build()
            }
            .build()
    }
}
