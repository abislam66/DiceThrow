package edu.temple.dicethrow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.random.Random
import androidx.lifecycle.ViewModelProvider

private const val DIESIDE = "sidenumber"
private const val ROLL_FLIPS = 15

class DieFragment : Fragment() {

    // Scoped to this fragment so each die keeps its own roll (and survives rotation)
    val dieViewModel: DieViewModel by lazy{
        ViewModelProvider(this)[DieViewModel::class.java]
    }
    lateinit var dieTextView: TextView

    var dieSides:Int=6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dieSides = it.getInt(DIESIDE, 6)
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
        dieViewModel.getRoll().observe(viewLifecycleOwner){
            dieTextView.text=it.toString()
        }
        // Tapping this die asks the activity to roll every die
        dieTextView.setOnClickListener {
            (requireActivity() as DieRoll).rollDice()
        }
        // Roll for the first time; after rotation the ViewModel still has the previous roll
        if (dieViewModel.getRoll().value == null)
            throwDie()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dieTextView.removeCallbacks(rollAnimation)
    }

    private var flipsLeft = 0

    // Shows a random face, waits a bit longer each time, then lands on the ViewModel's roll
    private val rollAnimation: Runnable = object : Runnable {
        override fun run() {
            if (flipsLeft > 0) {
                dieTextView.text = (Random.nextInt(dieSides) + 1).toString()
                flipsLeft--
                dieTextView.postDelayed(this, 50L + (ROLL_FLIPS - flipsLeft) * 15L)
            } else {
                dieTextView.text = dieViewModel.getRoll().value.toString()
            }
        }
    }

    fun throwDie() {
        dieViewModel.setRoll(Random.nextInt(dieSides) + 1)
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

    // The activity implements this so a tap on either die can roll both
    interface DieRoll {
        fun rollDice()
    }
}
