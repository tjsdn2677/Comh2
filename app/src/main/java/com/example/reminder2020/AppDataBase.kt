package com.example.reminder2020

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Reminder::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}