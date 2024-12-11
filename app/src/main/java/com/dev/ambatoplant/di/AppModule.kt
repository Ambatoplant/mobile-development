package com.dev.ambatoplant.di

import androidx.room.Room
import com.dev.ambatoplant.BuildConfig
import com.dev.ambatoplant.data.AppRepositoryImpl
import com.dev.ambatoplant.data.source.local.room.CancerDatabase
import com.dev.ambatoplant.data.source.remote.network.ApiService
import com.dev.ambatoplant.domain.repository.AppRepository
import com.dev.ambatoplant.view.HistoryViewModel
import com.dev.ambatoplant.view.MainViewModel
import com.dev.ambatoplant.view.ResultViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val databaseModule = module {
    factory { get<CancerDatabase>().cancerDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            CancerDatabase::class.java,
            "cancer.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val repositoryModule = module {
    single<AppRepository> {
        AppRepositoryImpl(
            get(),
            get(),
            androidContext()
        )
    }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ResultViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}

val appModule = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    viewModelModule
)