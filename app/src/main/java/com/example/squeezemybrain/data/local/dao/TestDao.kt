package com.example.squeezemybrain.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.squeezemybrain.data.local.entity.TestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories : List<TestEntity>)

    @Query("update test set test_taken=:test_taken,test_passed=:test_passed,difficulty=:difficulty where category_id=:category_id")
    suspend fun updateCategory(category_id : Int, difficulty : String, test_taken : Int, test_passed : Int)

    @Query("select count(id) from test")
    suspend fun getCategoryCount() : Int

    @Query("select * from test where category_id=:categoryId")
    suspend fun getCategoryViaId(categoryId : Int) : TestEntity

    @Query("select * from test")
    fun getAllCategories() : Flow<List<TestEntity>>
}