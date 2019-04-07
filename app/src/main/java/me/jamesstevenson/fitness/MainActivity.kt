package me.jamesstevenson.fitness

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.*
import java.io.Serializable
import kotlinx.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.*

// This hash map (Dictionary) related key value pair from the entities in the list view to a list of each activity
public val mapOfActivities: HashMap<String, ArrayList<String>> = hashMapOf(
        "7 Minute Workout" to arrayListOf("Jumping Jacks", "Wall Sits", "Push Ups","Abdominal Crunches","Step-Ups Onto A Chair", "Rest", "Squats","Triceps Dips On A Chair","Planks","High Knees Running In Place", "Lunges", "Push-Ups And Rotations", "Side Planks", "Rest"),
        "Core Fundamentals" to arrayListOf("Abdominal Crunches", "Abdominal Circles","Bend Knees And Extend", "Planks", "Left Side Planks","Right Side Planks"),
        "Mindfulness Fundamentals" to arrayListOf("Deep Breaths and Close Eyes", "Survey Body", "Listen To Surroundings", "Deep Breaths and Open Eyes"))

class MainActivity : AppCompatActivity() {

    //This list is displayed in the listview to the user
    var array = arrayOf("7 Minute Workout", "Core Fundamentals","Mindfulness Fundamentals")
    // This variable defines if there is already an activity being played
    var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // The below is used to set the above list to the list view in the UI
        val adapter = ArrayAdapter(this,
                R.layout.listview_item, array)
        val listView: ListView = findViewById(R.id.listview)
        listView.setAdapter(adapter)

        // Runs if a user selects one of the list items
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {

                if (!isRunning) {
                    isRunning = true

                    // value of item that is clicked
                    val itemValue = listView.getItemAtPosition(position) as String
                    val textToDisplay = "Starting: $itemValue"

                    val activityList = mapOfActivities[itemValue]

                    showSnack(view,textToDisplay)

                    GlobalScope.async {
                        activityList?.forEach {
                            setActivity(it, 30)
                        }
                        isRunning = false
                    }
                }else{
                    showSnack(view,"Finish current activity first...")
                }

            }
        }

    }

    fun setActivity(activityName: String, duration: Int) {
        speak("Next Up $activityName")

        runOnUiThread {
            activityNameView.setText(activityName)
        }

        for (i in duration downTo 0) {
            runOnUiThread {
                countdown.setText(i.toString())
            }
            Thread.sleep(1_000)
        }
    }

    fun speak(words: String) {
        TTS(this@MainActivity, words)
    }

    fun showSnack(view: View, text:String){
        var snackbar = Snackbar.make(view, "$text",
                Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}


