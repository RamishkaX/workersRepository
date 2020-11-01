package com.example.workers.handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.workers.models.SpecialityModel
import com.example.workers.models.UserModel
import com.example.workers.models.WorkersSpeciality

val DB_NAME = "Workers"
val TABLE_USERS = "Users"
val TABLE_SPECIALITY = "Speciality"
val TABLE_WORKERSSPECIALITY = "WorkersSpeciality"
val USER_ID = "id"
val USER_NAME = "name"
val USER_LASTNAME = "lastname"
val USER_BIRTHDAY = "birthday"
val USER_AVATAR = "avatar"
val SPECIALITY_ID = "id"
val SPECIALITY_NAME = "name"
val WORKERSSPECIALITY_ID = "id"
val WORKERSSPECIALITY_USERID = "userId"
val WORKERSSPECIALITY_SPECIALITYID = "specialityId"

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = "CREATE TABLE $TABLE_USERS (" +
                "$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$USER_NAME VARCHAR(256)," +
                "$USER_LASTNAME VARCHAR(256)," +
                "$USER_BIRTHDAY VARCHAR(256) NULL," +
                "$USER_AVATAR VARCHAR(256) NULL)"
        val createSpecialityTable = "CREATE TABLE $TABLE_SPECIALITY (" +
                "$SPECIALITY_ID INTEGER PRIMARY KEY," +
                "$SPECIALITY_NAME VARCHAR(256))"
        val createWorkersSpecialityTable = "CREATE TABLE $TABLE_WORKERSSPECIALITY (" +
                "$WORKERSSPECIALITY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$WORKERSSPECIALITY_USERID INTEGER," +
                "$WORKERSSPECIALITY_SPECIALITYID INTEGER," +
                "FOREIGN KEY($WORKERSSPECIALITY_USERID) REFERENCES $TABLE_USERS($USER_ID)," +
                "FOREIGN KEY($WORKERSSPECIALITY_SPECIALITYID) REFERENCES $TABLE_SPECIALITY($SPECIALITY_ID))"

        db?.execSQL(createUsersTable)
        db?.execSQL(createSpecialityTable)
        db?.execSQL(createWorkersSpecialityTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SPECIALITY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WORKERSSPECIALITY")

        onCreate(db)
    }

    fun insertUser(db: SQLiteDatabase?, users: ArrayList<UserModel>) {
        for (element in users) {
            val contentValues = ContentValues()

            contentValues.put(USER_NAME, element.name)
            contentValues.put(USER_LASTNAME, element.lastName)
            element.birthday.let { contentValues.put(USER_BIRTHDAY, element.birthday) }
            element.avatarUrl.let { contentValues.put(USER_AVATAR, element.avatarUrl) }

            db?.insert(TABLE_USERS, null, contentValues)
        }
    }

    fun insertSpeciality(db: SQLiteDatabase?, speciality: ArrayList<SpecialityModel>) {
        for (element in speciality) {
            val contentValues = ContentValues()

            contentValues.put(SPECIALITY_ID, element.id)
            contentValues.put(SPECIALITY_NAME, element.name)

            db?.insert(TABLE_SPECIALITY, null, contentValues)
        }
    }

    fun insertWorkersSpeciality(db: SQLiteDatabase?, workersSpeciality: ArrayList<WorkersSpeciality>) {
        for (element in workersSpeciality) {
            val contentValues = ContentValues()

            contentValues.put(WORKERSSPECIALITY_USERID, element.userId)
            contentValues.put(WORKERSSPECIALITY_SPECIALITYID, element.specialityId)

            db?.insert(TABLE_WORKERSSPECIALITY, null, contentValues)
        }
    }
}