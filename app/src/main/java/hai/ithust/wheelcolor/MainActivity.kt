package hai.ithust.wheelcolor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import hai.ithust.wheelcolor.databinding.ActivityMainBinding
import hai.ithust.wheelcolor.wheelcolor.SegmentColorView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(LayoutInflater.from(this)) }
    private val segmentColorClicked = View.OnClickListener {
        binding.wheelColor.setSelectedColor((it as SegmentColorView).contentColor)
        binding.slider.value = binding.wheelColor.brightness.roundToInt().toFloat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.wheelColor.brightness = binding.slider.value
        registerEvents()
    }

    private fun registerEvents() {
        binding.sgOne.setOnClickListener(segmentColorClicked)
        binding.sgTwo.setOnClickListener(segmentColorClicked)
        binding.sgThree.setOnClickListener(segmentColorClicked)
        binding.slider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.wheelColor.brightness = value
            }
        }
    }
}