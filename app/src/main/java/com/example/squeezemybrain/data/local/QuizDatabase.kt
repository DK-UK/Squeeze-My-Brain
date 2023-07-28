package com.example.squeezemybrain.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.squeezemybrain.data.local.dao.QuizDao
import com.example.squeezemybrain.data.local.dao.TestDao
import com.example.squeezemybrain.data.local.entity.QuestionEntity
import com.example.squeezemybrain.data.local.entity.TestEntity

@Database(entities = [QuestionEntity::class, TestEntity::class], version = 2, exportSchema = false)
abstract class QuizDatabase() : RoomDatabase() {
    abstract fun quizDao() : QuizDao
    abstract fun testDao() : TestDao

    companion object {
        var INSTANCE : QuizDatabase? = null
        public fun getDatabase(context : Context) : QuizDatabase{
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        QuizDatabase::class.java,
                        name = "Quiz"
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}