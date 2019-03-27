package com.example.bmi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.bmi.logic.state.AppState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {
    companion object {
        const val RESULT_KEY = "result"
        const val CATEGORY_KEY = "category"
        const val DESCRIPTION_KEY = "description"
        const val COLOR_KEY = "color"
        const val DEFAULT_COLOR_KEY = "default color"
        const val STATE_BUNDLE_KEY = "state"
        const val NULL_STATE_BUNDLE_MSG = "Null state bundle!"
    }

    private val state = AppState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        state.setResources(resources)

        countBmiButton.setOnClickListener {
            val mass = validateInput(massInput, getString(R.string.mass_input_error), Pair({result -> result != 0}, getString(R.string.mass_neq_zero)))
            val height = validateInput(heightInput, getString(R.string.height_input_error), Pair({result -> result != 0}, getString(R.string.height_neq_zero)))
            state.setMassAndHeight(mass, height) // update state
            updateUI() // update interface
        }

        bmiInfoButton.setOnClickListener {
            val intent = Intent(this, BmiInfo::class.java)
            intent.putExtra(RESULT_KEY, state.getBmi().toString())
            intent.putExtra(CATEGORY_KEY, state.getShortDescription())
            intent.putExtra(DESCRIPTION_KEY, state.getLongDescription())
            intent.putExtra(COLOR_KEY, state.getColor())
            intent.putExtra(DEFAULT_COLOR_KEY, ContextCompat.getColor(this, R.color.colorPrimary))
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putBundle(STATE_BUNDLE_KEY, state.toBundle())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val restoredBundle = savedInstanceState?.getBundle(STATE_BUNDLE_KEY)
        if (restoredBundle != null) {
            state.fromBundle(restoredBundle)
        }
        else {
            throw IllegalStateException(NULL_STATE_BUNDLE_MSG)
        }

        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        menu?.findItem(R.id.aboutMeUnits)?.isChecked = state.getImperialUnits()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.aboutMeItem -> {
                val intent = Intent(this, AboutMe::class.java)
                startActivity(intent)
                true
            }
            R.id.aboutMeUnits -> {
                state.changeUnits()
                item.isChecked = state.getImperialUnits()

                updateUI()

                return true
            }
            else -> true
        }
    }

    /**
     * Method for re-calculating all texts and colors viewed on screen.
     * Uses information from application state.
     */
    private fun updateUI() {
        massDescription.text = state.getMassDescription()
        heightDescription.text = state.getHeightDescription()

        bmiResult.text = state.getBmi()?.toString() ?: getString(R.string.empty_text)
        bmiCategory.text = state.getShortDescription()

        bmiResult.setTextColor(state.getColor())
        bmiCategory.setTextColor(state.getColor())

        if(state.isValid()) {
            bmiInfoButton.visibility = VISIBLE
            bmiInfoButton.isEnabled = true
            bmiInfoButton.background.setTint(state.getColor())
        }
        else {
            bmiInfoButton.visibility = INVISIBLE
            bmiInfoButton.isEnabled = false
            bmiInfoButton.background.setTint(state.getColor())
        }
    }

    /**
     * Method for validating input from a field with given conditions.
     * @param field TextField from which a value is read
     * @param errorMessage general error message displayed when reading input fails
     * @param validationRules pairs comprised of functions validating the result and corresponding error messages
     * if validation fails
     * @return number conforming to given conditions or null when input doesn't conform to constraints
     */
    private fun validateInput(field: EditText, errorMessage: String, vararg validationRules: Pair<(Int) -> (Boolean), String>): Int? {
        var result: Int? = null
        var ruleBroken = false

        try {
            result = field.text.toString().toInt()


            for(rule in validationRules) {
                if(!rule.first(result)) {
                    field.error = rule.second
                    ruleBroken = true
                    break
                }
            }
        } catch (e: NumberFormatException) {
            field.error = errorMessage
        }

        return if(ruleBroken) null else result
    }
}
