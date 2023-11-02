package com.example.realtimeapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.WindowManager.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimeapp.data.FirebaseInstance
import com.example.realtimeapp.databinding.ActivityMainBinding
import com.example.realtimeapp.databinding.DialogAskTaskBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var FireInstance: FirebaseInstance
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FireInstance = FirebaseInstance(this)
        initUI()
        setUpListener()

    }

    private fun setUpListener() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //val data= snapshot.getValue<String>()
                //if (data!=null){
                //binding.tvResult.text=data
                //               }
                val data = getCleanSnapshot(snapshot)
                todoAdapter.setNewList(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Laura Navarro", error.details)
            }
        }
        FireInstance.setUpDataBaseListener(postListener)
    }

    private fun getCleanSnapshot(snapshot: DataSnapshot): List<Pair<String, Todo>> {
        val list =
            snapshot.children.map { item ->
                Pair(item.key!!, item.getValue(Todo::class.java)!!) }
        return list

    }

    private fun initUI() {

        todoAdapter= TodoAdapter{ action,reference->
            when(action){
                Actions.DELETED ->  FireInstance.removeFromDataBase(reference)
                Actions.DONE -> FireInstance.uptadeFromDataBase(reference)
            }
        }
        binding.rvTasks.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=todoAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btnAskTask -> { showDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showDialog(){
        val binding =DialogAskTaskBinding.inflate(layoutInflater)
        val dialog= Dialog(this)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        binding.btnAddTask.setOnClickListener{

            val tittle= binding.etTittle.text.toString()
            val descripcion=binding.etDescription.text.toString()

            if( tittle.isEmpty() or descripcion.isEmpty()){
                Toast.makeText(this,"Ingrese un texto",Toast.LENGTH_LONG).show()
            }
            else{
                FireInstance.writeOnFirebase(tittle,descripcion)
                dialog.dismiss()

            }


        }
        dialog.show()
    }



}