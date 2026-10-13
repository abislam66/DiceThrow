package edu.temple.dicethrow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.random.Random

private const val DIESIDE = "sidenumber"
private const val CURRENTROLL = "currentroll"
private const val ROLL_FLIPS = 15

class DieFragment : Fragment() {

    lateinit var dieTextView: TextView


    var currentRoll: Int = 0
    var dieSides:Int=6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dieSides = it.getInt(DIESIDE, 6)
        }
        savedInstanceState?.let {
            currentRoll = it.getInt(CURRENTROLL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_die, container, false).apply {
            dieTextView = findViewById(R.id.dieTextView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Restore the previous roll after rotation, otherwise roll for the first time
        if (currentRoll == 0)
            throwDie()
        else
            dieTextView.text = currentRoll.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENTROLL, currentRoll)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dieTextView.removeCallbacks(rollAnimation)
    }

    private var flipsLeft = 0

    // Shows a random face, waits a bit longer each time, then lands on currentRoll
    private val rollAnimation: Runnable = object : Runnable {
        override fun run() {
            if (flipsLeft > 0) {
                dieTextView.text = (Random.nextInt(dieSides) + 1).toString()
                flipsLeft--
                dieTextView.postDelayed(this, 50L + (ROLL_FLIPS - flipsLeft) * 15L)
            } else {
                dieTextView.text = currentRoll.toString()
            }
        }
    }

    fun throwDie() {
        currentRoll = Random.nextInt(dieSides) + 1
        dieTextView.removeCallbacks(rollAnimation)
        flipsLeft = ROLL_FLIPS
        dieTextView.post(rollAnimation)
    }

    companion object {
        fun newInstance(sides: Int) = DieFragment().apply {
            arguments = Bundle().apply {
                putInt(DIESIDE, sides)
            }
        }
    }
}
