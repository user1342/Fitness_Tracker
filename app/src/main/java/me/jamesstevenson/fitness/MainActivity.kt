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
                    // This is then displayed to

                    // This takes the item from the selected list and looks it up in the hashmap, returning it's related list
                    val activityList = mapOfActivities[itemValue]

                    showSnack(view,"Starting: $itemValue")

                    // This asynchronously loops through each item in the list
                    GlobalScope.async {
                        activityList?.forEach {
                            setActivity(it, 30)
                        }
                        // After all items in the list have run it will set the isRunning back to false.
                        isRunning = false
                    }
                }else{
                    // Shows an error message if an activity is already running
                    showSnack(view,"Finish current activity first...")
                }

            }
        }

    }

    fun setActivity(activityName: String, duration: Int) {
        speak("Next Up $activityName")

        //Runs any changes to the UI on the UI thread
        runOnUiThread {
            activityNameView.setText(activityName)
        }

        // Loops down from a range to 0 and waits 1 second sbetween each, this functions as a countdown.
        for (i in duration downTo 0) {
            runOnUiThread {
                countdown.setText(i.toString())
            }
            Thread.sleep(1_000)
        }
    }

    // Runs the text to speech code
    fun speak(words: String) {
        TTS(this@MainActivity, words)
    }

    //Runs Snackbar code
    fun showSnack(view: View, text:String){
        val snackbar = Snackbar.make(view, text,
                Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}


