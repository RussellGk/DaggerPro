package com.hardtm.daggerpro.bash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardtm.daggerpro.DaggerProApp
import com.hardtm.daggerpro.R
import com.hardtm.daggerpro.rest.BashAPI
import kotlinx.android.synthetic.main.fragment_bash.*
import javax.inject.Inject

class FragmentBash : Fragment() {

    private lateinit var star: ImageView
    private lateinit var bashViewModel: BashViewModel

    @Inject
    lateinit var bashApi: BashAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerProApp.appComponent.inject(this) //Call it before onCreate
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        star = requireActivity().findViewById(R.id.star)
        val swipeColor = ContextCompat.getColor(requireContext(), R.color.colorGreen)
        swipeOne.setColorSchemeColors(swipeColor, swipeColor, swipeColor)
        swipeOne.setOnRefreshListener {
            bashViewModel.getBashData()
        }

        bashViewModel = ViewModelProvider(this, BashVmFactory(application = requireActivity().application , injectedBashApi = bashApi)).get(BashViewModel::class.java)
        bashViewModel.bashList.observe(viewLifecycleOwner, Observer { bashEntityList ->
            recyclerOne.adapter = BashRecyclerAdapter(bashEntityList,
                { bashItem: String -> itemClicked(bashItem.toInt()) })
        })
        bashViewModel.defaultProgressLiveData.observe(viewLifecycleOwner, Observer { isProgress ->
            swipeOne.isRefreshing  = isProgress
        })

        recyclerOne.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        if(bashViewModel.bashList.value.isNullOrEmpty()){
            bashViewModel.getBashData()
        }
    }

    private fun itemClicked(bashItem: Int) {
        for (i in 1..bashItem){
            starShower()
        }
    }

    private fun starShower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(requireContext())
        newStar.setImageResource(R.drawable.ic_star_24dp)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        newStar.scaleX = Math.random().toFloat() * 1.8f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        newStar.translationX = Math.random().toFloat() * containerW - starW / 2

        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(0.5f)

        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

    companion object {
        fun newInstance(): FragmentBash {
            return FragmentBash()
        }
    }
}