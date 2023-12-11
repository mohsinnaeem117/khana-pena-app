package com.example.khanapena

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.khanapena.Listeners.InstructionsListener
import com.example.khanapena.Listeners.RecipeClickListener
import com.example.khanapena.Listeners.RecipeDetailsListener
import com.example.khanapena.Listeners.SimilarRecipeListener
import com.example.khanapena.adapters.IngredientsAdapter
import com.example.khanapena.adapters.InstructionsAdapter
import com.example.khanapena.adapters.SimilarRecipeAdapter
import com.example.khanapena.models.InstructionsResponse
import com.example.khanapena.models.RecipeDetailsResponse
import com.example.khanapena.models.SimilarRecipeResponse
import com.squareup.picasso.Picasso

class RecipeDetailsActivity : AppCompatActivity() {

    var id: Int = 0
    private lateinit var textView_meal_name: TextView
    private lateinit var textView_meal_source: TextView
    private lateinit var imageView_meal_image: ImageView
    private lateinit var textView_meal_summary: TextView
    private lateinit var recycler_meal_ingredients: RecyclerView
    private lateinit var recycler_meal_similar: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var similarRecipeAdapter: SimilarRecipeAdapter
    private lateinit var recycler_meal_instructions: RecyclerView
    private lateinit var instructionsAdpter : InstructionsAdapter

    private lateinit var manager: RequestManager
    private lateinit var dialog: ProgressDialog

    private fun findViews(){
        textView_meal_name = findViewById(R.id.textView_meal_name)
        textView_meal_source = findViewById(R.id.textView_meal_source)
        imageView_meal_image = findViewById(R.id.imageView_meal_image)
        textView_meal_summary = findViewById(R.id.textView_meal_summary)
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients)
        recycler_meal_similar = findViewById(R.id.recycler_meal_similar)
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions)
    }


    private val recipeDetailsListener = object : RecipeDetailsListener{
        override fun didFetch(response: RecipeDetailsResponse?, message: String?) {
            dialog.dismiss()
            if (response != null){
                textView_meal_name.text = response.title
                textView_meal_source.text = response.sourceName
                textView_meal_summary.text = response.summary
                Picasso.get().load(response.image).into(imageView_meal_image)

                recycler_meal_ingredients.setHasFixedSize(true)
                recycler_meal_ingredients.layoutManager = LinearLayoutManager(
                    this@RecipeDetailsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                ingredientsAdapter = IngredientsAdapter(this@RecipeDetailsActivity, response?.extendedIngredients ?: emptyList())
                recycler_meal_ingredients.adapter = ingredientsAdapter

            }
        }

        override fun didError(message: String?) {
            Toast.makeText(this@RecipeDetailsActivity,message,Toast.LENGTH_SHORT).show()
        }
    }

    private val similarRecipeListener = object : SimilarRecipeListener{
        override fun didFetch(response: MutableList<SimilarRecipeResponse>?, message: String?) {
            recycler_meal_similar.setHasFixedSize(true)
            recycler_meal_similar.layoutManager = LinearLayoutManager(
                this@RecipeDetailsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            similarRecipeAdapter = SimilarRecipeAdapter(this@RecipeDetailsActivity,response, recipeClickListener)
            recycler_meal_similar.adapter=similarRecipeAdapter
        }


        override fun didError(message: String?) {
            Toast.makeText(this@RecipeDetailsActivity,message,Toast.LENGTH_SHORT).show()
        }

    }
    private val recipeClickListener = object : RecipeClickListener{
        override fun onRecipeClicked(id: String?) {
            val intent = Intent(this@RecipeDetailsActivity,RecipeDetailsActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }

//    private val instructionsListener = object : InstructionsListener{
//        override fun didFetch(response: MutableList<InstructionsResponse>?, message: String?) {
//            recycler_meal_instructions.setHasFixedSize(true)
//            recycler_meal_instructions.layoutManager = LinearLayoutManager(
//                this@RecipeDetailsActivity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//            instructionsAdpter = InstructionsAdapter(this@RecipeDetailsActivity, response)
//            recycler_meal_instructions.adapter = instructionsAdpter
//        }
//
//        override fun didError(message: String?) {
//
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        findViews()

        id= intent.getStringExtra("id")?.toIntOrNull()?:0

        dialog = ProgressDialog(this)
        dialog.setTitle("Loading....")
        dialog.show()

        manager = RequestManager(this)
        manager.getRecipesDetails(recipeDetailsListener,id)
        manager.getSimilarRecipes(similarRecipeListener,id)
//        manager.getInstructions(instructionsListener, id)


    }
}