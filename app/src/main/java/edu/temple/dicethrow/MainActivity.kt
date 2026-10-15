package edu.temple.dicethrow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), DieFragment.DieRoll {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Only add the fragments the first time; after rotation the FragmentManager restores them
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainerView, DieFragment.newInstance(6))
                .add(R.id.fragmentContainerView2, DieFragment.newInstance(6))
                .commit()
    }

    // Called by either die when it's tapped; rolls both
    override fun rollDice() {
        listOf(R.id.fragmentContainerView, R.id.fragmentContainerView2).forEach {
            (supportFragmentManager.findFragmentById(it) as DieFragment).throwDie()
        }
    }
}
