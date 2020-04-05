package com.example.todolist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TASK_FILE_NAME = "lista_de_tareas.txt"
    private val tareas = ArrayList<String>()
    private lateinit var myAdapter : ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lateinit var reader : Scanner

        try {
            reader = Scanner(openFileInput(TASK_FILE_NAME))

        }catch (e : FileNotFoundException ){
            Log.d("wtf","!!!! ${e.toString()}")
            PrintStream(openFileOutput(TASK_FILE_NAME, Context.MODE_PRIVATE)).close()
            reader = Scanner(openFileInput(TASK_FILE_NAME))
        }finally {
            start(reader)
        }

    }

    private fun start(reader: Scanner){

        if (!reader.hasNextLine()){

            configurarLista()
            configurarListeners()

        }else{

            leerArchivoDeTareas(reader)
            configurarLista()
            configurarListeners()
        }
    }

   private fun createFile(lista : ArrayList<String>){
       val outStream = PrintStream(openFileOutput(TASK_FILE_NAME, Context.MODE_PRIVATE))
       for (linea in lista){
           outStream.println(linea)
       }
       outStream.close()
    }



    private fun agregarTarea(){
        val task = the_new_task.text.toString()
        tareas.add(task)
        createFile(tareas)
        configurarLista()
        the_new_task.setText("")
    }

    private fun leerArchivoDeTareas(reader: Scanner) {
        while (reader.hasNextLine()) {
            val line = reader.nextLine()
            tareas.add(line)
        }
    }

    private fun configurarLista() {
        myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tareas)
        the_task_list.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
    }

    private fun configurarListeners(){
        add_button.setOnClickListener { agregarTarea() }

        the_task_list.setOnItemLongClickListener { _, _, index, _ ->
            tareas.removeAt(index)
            createFile(tareas)
            myAdapter.notifyDataSetChanged()
            !tareas.isEmpty()
        }
    }
}
