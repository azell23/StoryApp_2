package com.example.mystoryapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(remoteKeys: List<RemoteKeys>)

    @Query("select * from remote_keys where id = :id")
    suspend fun getRemoteId(id: String): RemoteKeys?

    @Query("delete from remote_keys")
    suspend fun deleteRemote()
}