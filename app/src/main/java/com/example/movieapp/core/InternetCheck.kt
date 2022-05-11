package com.example.movieapp.core

import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Validador de conexión a internet
 */
object InternetCheck {

    // coroutineScope es un Scope que permite ejecutar co-rutinas u operaciones asincronas
    suspend fun isNetworkAvailable() = coroutineScope {
        return@coroutineScope try {
            onActiveSecurityConnectionDefault()

            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(socketAddress, 2000)// es como realizar un ping de conexión a google
            sock.close()
            Log.d("OkNetwork", "Internet connection OK")
            true
        } catch (e: IOException) {
            Log.d("NotNetwork", "No internet connection")
            false
        }
    }

    /**
     * Permite hacer comunicaciones a Sockets en segundo plano, ya que en versiones recientes están restringidas
     */
    private fun onActiveSecurityConnectionDefault() {
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}