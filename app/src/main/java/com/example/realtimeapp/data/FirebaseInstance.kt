package com.example.realtimeapp.data

import android.content.Context
import android.icu.text.CaseMap.Title
import com.example.realtimeapp.Todo
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class FirebaseInstance (context: Context){

    private val database= Firebase.database
    private val myRef=database.reference
    init {
        FirebaseApp.initializeApp(context)
    }
    fun writeOnFirebase(title: String, descripcion:String){
        val myRef= database.reference
        val random= Random.nextInt(0,100).toString()
        val newRef=myRef.push()
        //myRef.setValue("hola $random")
        newRef.setValue(Todo(title,descripcion))

    }

    fun setUpDataBaseListener(postListener: ValueEventListener) {
        database.reference.addValueEventListener(postListener)

    }
    //private fun getGenericTodoTaskItem(randomValue:String)=
        //Todo(title = "tarea $randomValue",description ="descripcion")

    fun removeFromDataBase(reference: String) {
        myRef.child(reference).removeValue()

    }

    fun uptadeFromDataBase(reference: String) {
        myRef.child(reference).child("done").setValue(true)

    }


}