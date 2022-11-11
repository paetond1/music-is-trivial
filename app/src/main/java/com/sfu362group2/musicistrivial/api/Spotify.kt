package com.sfu362group2.musicistrivial.api

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.sfu362group2.musicistrivial.R
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

private const val TAG = "DEBUG: Spotify API"

class Spotify(context: Context) {
    private val context: Context
    private val clientId: String
    private val clientSecret: String

    init {
        System.loadLibrary("keys")
        this.context = context
        this.clientId = getSpotifyClientId()
        this.clientSecret = getSpotifyClientSecret()
    }

    fun tokenRequest(onSuccess: (jsonObj: JSONObject) -> Unit): JsonObjectRequest {
        val tokenRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            context.getString(R.string.spotify_token_url),
            null,
            Response.Listener { response ->
                Log.i(TAG, "tokenRequest succeeded!")
                onSuccess(response)
            },
            Response.ErrorListener { error ->
                Log.e(
                    TAG,
                    "tokenRequest failed with status code ${error.networkResponse.statusCode}"
                )
                Log.e(TAG, "tokenRequest failure message: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> {
                val idSecret =
                    "$clientId:$clientSecret"
                val authHeaderVal =
                    "Basic " + Base64.getEncoder().encodeToString(idSecret.toByteArray())
                val headers = HashMap<String, String>()
                headers["Authorization"] = authHeaderVal
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                return headers
            }

            override fun getBody(): ByteArray {
                val body = "grant_type=client_credentials"
                return body.toByteArray()
            }
        }
        return tokenRequest
    }

    fun artistRequest(
        token: String,
        artistId: String,
        onSuccess: (jsonObj: JSONObject) -> Unit
    ): JsonObjectRequest {
        val artistRequest = object : JsonObjectRequest(
            Method.GET,
            context.getString(R.string.spotify_artist_url) + artistId,
            null,
            { response ->
                Log.i(TAG, "artistRequest succeeded")
                onSuccess(response)
            }, { error ->
                Log.e(
                    TAG,
                    "artistRequest failed with status code ${error.networkResponse.statusCode}"
                )
                Log.e(TAG, "artistRequest failure message: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return getJSONHeaders(token)
            }
        }
        return artistRequest
    }

    fun topTracksRequest(
        token: String,
        artistId: String,
        onSuccess: (jsonObj: JSONObject) -> Unit
    ): JsonObjectRequest {
        val topSongsRequest = object : JsonObjectRequest(
            Method.GET,
            context.getString(R.string.spotify_artist_url) + artistId + context.getString(R.string.spotify_top_tracks_path),
            null,
            { response ->
                onSuccess(response)
            }, { error ->
                Log.e(
                    TAG,
                    "topTracksRequest failed with status code ${error.networkResponse.statusCode}"
                )
                Log.e(TAG, "topTracksRequest failure message: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return getJSONHeaders(token)
            }
        }
        return topSongsRequest
    }

    fun randomArtistRequest(
        token: String, char: Char,
        onSuccess: (jsonObj: JSONObject) -> Unit
    ): JsonObjectRequest {
        var num = Random(System.currentTimeMillis()).nextInt(0, 20)
        val url = "https://api.spotify.com/v1/search?q=$char%25&type=artist&market=CA&limit=20"
        val request = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            { response ->
                Log.i(TAG, "searchRequest success")
                onSuccess(response)
            }, { error ->
                Log.e(
                    TAG,
                    "searchRequest failed with status code ${error.networkResponse.statusCode}"
                )
                Log.e(TAG, "searchRequest failure message: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return getJSONHeaders(token)
            }
        }
        return request
    }

    private fun getJSONHeaders(token: String): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        headers["Content-Type"] = "application/json"
        return headers
    }


    private external fun getSpotifyClientSecret(): String

    private external fun getSpotifyClientId(): String
}