package com.example.roomdemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert
    suspend fun insert(entity: ProductEntity): Long

    // Удобная обертка для UI-кода (Room обработает только insert(entity)).
    suspend fun insert(name: String, category: String, quantity: Int): Long =
        insert(ProductEntity(name = name, category = category, quantity = quantity))

    @Query("SELECT * FROM products ORDER BY id ASC")
    suspend fun getAll(): List<ProductEntity>

    @Query("DELETE FROM products")
    suspend fun clear()
}

