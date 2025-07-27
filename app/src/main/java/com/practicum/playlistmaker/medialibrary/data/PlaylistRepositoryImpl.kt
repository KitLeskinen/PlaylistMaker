package com.practicum.playlistmaker.medialibrary.data

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.medialibrary.data.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.medialibrary.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.medialibrary.data.db.AppDataBase
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.medialibrary.domain.PlaylistRepository
import java.io.File
import java.io.FileOutputStream


class PlaylistRepositoryImpl(private val appDataBase: AppDataBase, private val context: Context) :
    PlaylistRepository {

    override suspend fun getPlaylists(): List<Playlist> {
        return appDataBase.playlistDao().getPlaylists()
            .map { playlist ->
                PlaylistDbConvertor().map(playlist)
            }
    }

    override suspend fun getPlaylistWithTracks(playlistId: Long): Playlist {
        return TrackDbConvertor().map(appDataBase.playlistDao().getPlayListWithTracks(playlistId))
    }


    override suspend fun getAllPlaylistsWithTracks(): List<Playlist> {
        return appDataBase.playlistDao().getAllPlayListsWithTracks().map { playlistWithTracks ->
            TrackDbConvertor().map(playlistWithTracks)
        }
    }


    override suspend fun savePlaylist(playlist: Playlist) {
        appDataBase.playlistDao().savePlaylist(PlaylistDbConvertor().map(playlist))
    }

    override fun saveCoverImage(uri: Uri): Uri {
        val directoryPath =
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "covers"
            )
        if (!directoryPath.exists()) {
            directoryPath.mkdirs()
        }
        val contentResolver = context.contentResolver

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION

        contentResolver.takePersistableUriPermission(uri, takeFlags)
        val file = File(directoryPath, "cover_${System.currentTimeMillis()}.jpg")

        val inputStream = contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }

    override suspend fun addTrack(track: Track, playlist: Playlist) : Long {
        return appDataBase.playlistDao().addTrack(
            crossRef = PlaylistTrackCrossRef(playlist.id, track.trackId)
        )
    }

}