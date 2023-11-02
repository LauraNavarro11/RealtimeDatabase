package com.example.realtimeapp

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimeapp.Actions.*
import com.example.realtimeapp.Todo
import com.example.realtimeapp.databinding.ItemTodoBinding

class TodoViewHolder(view:View) :RecyclerView.ViewHolder(view) {
    private val binding=ItemTodoBinding.bind(view)

    fun bind(TodoTask: Pair<String, Todo>, onItemSelected: (Actions,String) -> Unit,) {
        binding.tvTittle.text= TodoTask.second.title
        binding.tvDescription.text=TodoTask.second.description
        binding.tvReference.text=TodoTask.first

        binding.ivDone.setOnClickListener { onItemSelected(DONE,TodoTask.first) }
        binding.ivDelete.setOnClickListener { onItemSelected(DELETED,TodoTask.first) }

        val color= if(TodoTask.second.done == true){
            R.color.gold
        }
        else{
            R.color.card
        }
        binding.cardViewItem.strokeColor =ContextCompat.getColor(binding.tvReference.context,color)


    }
}