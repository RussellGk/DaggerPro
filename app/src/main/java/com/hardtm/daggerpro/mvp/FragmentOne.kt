package com.hardtm.daggerpro.mvp

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
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardtm.daggerpro.R
import com.hardtm.daggerpro.db.BashEntity
import com.hardtm.daggerpro.db.DaggerProDatabase
import com.hardtm.daggerpro.rest.BashService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_one.*

class FragmentOne : Fragment() {

    private lateinit var star: ImageView
    private lateinit var bashViewModel: BashViewModel
    private lateinit var database: DaggerProDatabase
    private var disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        star = activity!!.findViewById(R.id.star)
        val swipeColor = ContextCompat.getColor(context!!, R.color.colorGreen)
        swipeOne.setColorSchemeColors(swipeColor, swipeColor, swipeColor)
        swipeOne.setOnRefreshListener {
            getBashData()
        }

        database = DaggerProDatabase.getDatabase(activity?.applicationContext!!)
        bashViewModel = ViewModelProvider(this).get(BashViewModel::class.java)
        bashViewModel.bashList.observe(viewLifecycleOwner, Observer { bashEntityList ->
            recyclerOne.adapter = BashRecyclerAdapter(bashEntityList,
                { bashItem: String -> itemClicked(bashItem.toInt()) })
        })

        recyclerOne.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val bashDBList = database.bashDao().getBashList().value
        if (bashDBList.isNullOrEmpty()) {
            swipeOne.isRefreshing = true
            getBashData()
        }
    }

    private fun getBashData() {
        val disposable = BashService().api.getBash("bash.im", "bash", "16")
            .subscribeOn(Schedulers.io())
            .map { bashResponse ->
                if (bashResponse.isSuccessful) {
                    val bashList = bashResponse.body()
                    if (!bashList.isNullOrEmpty()) {
                        val bash = mutableListOf<BashEntity>()
                        bashList.forEachIndexed { index, bashStory ->
                            if (index != 0) {
                                bash.add(
                                    BashEntity(
                                        index, HtmlCompat.fromHtml(
                                            bashStory.elementPureHtml.toString(),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY
                                        ).toString()
                                    )
                                )
                            }
                        }
                        database.bashDao().saveBashList(bash)
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                swipeOne.isRefreshing = false
            }, { throwableError ->
                swipeOne.isRefreshing = false
                Toast.makeText(context, throwableError.message, Toast.LENGTH_LONG).show()
            })
        disposables.add(disposable)
    }

    private fun itemClicked(bashItem: Int) {
        for (i in 1..bashItem){
            starShower()
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun starShower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(context!!)
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
        fun newInstance(): FragmentOne {
            return FragmentOne()
        }
    }
}