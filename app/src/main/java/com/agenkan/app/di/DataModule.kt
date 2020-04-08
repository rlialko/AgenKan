package com.agenkan.app.di

import android.content.Context
import androidx.room.Room
import com.agenkan.app.BuildConfig
import com.agenkan.app.BuildConfig.DEBUG
import com.agenkan.app.data.api.DataApiService
import com.agenkan.app.data.preferences.AppPreferences
import com.agenkan.app.data.repository.DataRepository
import com.agenkan.app.data.repository.DataRepositoryImpl
import com.agenkan.app.data.source.local.DataDao
import com.agenkan.app.data.source.local.DataDatabase
import com.agenkan.app.data.source.local.LocalDataSource
import com.agenkan.app.data.source.local.LocalDataSourceImpl
import com.agenkan.app.data.source.remote.RemoteDataSource
import com.agenkan.app.data.source.remote.RemoteDataSourceImpl
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val dataModule = module {
    factory { FirebaseInstanceId.getInstance() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideApi(get()) }
    single { provideRoom(androidApplication()) }
    single { get<DataDatabase>().getDao() }
    single { provideLocalSource(get()) }
    single { provideRemoteSource(get()) }
    single { provideRepository(get(), get(), get()) }
    single { AppPreferences(get()) }
}

fun provideRoom(context: Context): DataDatabase {
    return Room.databaseBuilder(context, DataDatabase::class.java, "data.db").build()
}

fun provideOkHttpClient(appPreferences: AppPreferences): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    if (DEBUG) {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    }

    return OkHttpClient().newBuilder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().addHeader(
                    "content-type",
                    "application/json; charset=utf-8"
                ).build()
            )
        }
        .addInterceptor(ServiceInterceptor(appPreferences))
        .build()
}

class ServiceInterceptor(private val appPreferences: AppPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.header("No-Authentication") == null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer ${appPreferences.getToken()}")
                .build()
        }
        return chain.proceed(request)
    }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideApi(retrofit: Retrofit): DataApiService = retrofit.create(DataApiService::class.java)


fun provideLocalSource(dao: DataDao): LocalDataSource =
    LocalDataSourceImpl(dao)

fun provideRemoteSource(service: DataApiService): RemoteDataSource =
    RemoteDataSourceImpl(service)

fun provideRepository(
    appPreferences: AppPreferences,
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
): DataRepository {
    return DataRepositoryImpl(appPreferences, remoteDataSource, localDataSource)
}