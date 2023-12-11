package com.example.khanapena

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khanapena.Listeners.RandomRecipeResponseListener
import com.example.khanapena.Listeners.RecipeClickListener
import com.example.khanapena.adapters.RandomRecipeAdapter
import com.example.khanapena.models.RandomRecipeApiResponse

class MainActivity : AppCompatActivity() {
    private lateinit var dialog: ProgressDialog
    private lateinit var manager: RequestManager
    private lateinit var randomRecipeAdapter: RandomRecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView
    private val tags : MutableList<String> = ArrayList()

    private val recipeClickListener: RecipeClickListener = object  : RecipeClickListener{
        override fun onRecipeClicked(id: String?) {
            startActivity(Intent(this@MainActivity,RecipeDetailsActivity::class.java).apply {
             putExtra("id",id)
            })
        }
    }

    private val randomRecipeResponseListener = object : RandomRecipeResponseListener{
        override fun didFetch(response: RandomRecipeApiResponse?, message: String?) {
            dialog.dismiss()
            recyclerView = findViewById(R.id.recycler_random)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity,1)
            randomRecipeAdapter = RandomRecipeAdapter(this@MainActivity,response?.recipes?: emptyList(), recipeClickListener)
            recyclerView.adapter= randomRecipeAdapter
        }

        override fun didError(message: String?) {
            Toast.makeText(this@MainActivity ,message,Toast.LENGTH_SHORT).show()
        }

    }

    private val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            tags.clear()
            tags.add(parent?.selectedItem.toString())
            manager.getRandomRecipes(randomRecipeResponseListener,tags)
            dialog.show()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = ProgressDialog(this)
        dialog.setTitle("Loading....")

        spinner = findViewById(R.id.spinner_tags)
        val arrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.tags,
            R.layout.spinner_text
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = spinnerItemSelectedListener

        manager = RequestManager(this)
//        manager.getRandomRecipes(randomRecipeResponseListener)
//        dialog.show()

        val searchView = findViewById<SearchView>(R.id.searchView_home)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()){
                tags.clear()
                tags.add(query)
                manager.getRandomRecipes(randomRecipeResponseListener, tags)
                dialog.show()}
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }
}