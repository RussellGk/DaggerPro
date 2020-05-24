package com.hardtm.daggerpro.mvvm

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
import com.hardtm.daggerpro.db.DaggerProDatabase
import com.hardtm.daggerpro.db.JokeEntity
import com.hardtm.daggerpro.rest.JokeService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_two.*

class FragmentTwo : Fragment() {

    private lateinit var snowflake: ImageView
    private lateinit var jokeViewModel: JokeViewModel
    private lateinit var database: DaggerProDatabase
    private var disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snowflake = activity!!.findViewById(R.id.snowflake)
        val swipeColor = ContextCompat.getColor(context!!, R.color.colorGreen)
        swipeTwo.setColorSchemeColors(swipeColor, swipeColor, swipeColor)
        swipeTwo.setOnRefreshListener {
            getJokeData()
        }

        database = DaggerProDatabase.getDatabase(activity?.applicationContext!!)
        jokeViewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
        jokeViewModel.jokeList.observe(viewLifecycleOwner, Observer { jokeEntityList ->
            recyclerTwo.adapter = JokesRecyclerAdapter(jokeEntityList,
                { jokeItem: String -> itemClicked(jokeItem.toInt()) })
        })

        recyclerTwo.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val jokeDBList = database.jokeDao().getJokeList().value
        if (jokeDBList.isNullOrEmpty()) {
            swipeTwo.isRefreshing = true
            getJokeData()
        }
    }

    private fun getJokeData() {
        val disposable = JokeService().api.getJokes("anekdot.ru", "new anekdot", "15")
            .subscribeOn(Schedulers.io())
            .map { jokeResponse ->
                if (jokeResponse.isSuccessful) {
                    val jokeList = jokeResponse.body()
                    if (!jokeList.isNullOrEmpty()) {
                        val jokes = mutableListOf<JokeEntity>()
                        jokeList.forEachIndexed { index, jokeStory ->
                            if (index != 0) {
                                jokes.add(
                                    JokeEntity(
                                        index, HtmlCompat.fromHtml(
                                            jokeStory.elementPureHtml.toString(),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY
                                        ).toString()
                                    )
                                )
                            }
                        }
                        database.jokeDao().saveJokeList(jokes)
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                swipeTwo.isRefreshing = false
            }, { throwableError ->
                swipeTwo.isRefreshing = false
                Toast.makeText(context, throwableError.message, Toast.LENGTH_LONG).show()
            })
        disposables.add(disposable)
    }

    private fun itemClicked(jokeItem: Int) {
        for (i in 1..jokeItem){
            snowShower()
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun snowShower() {
        val container = snowflake.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var snowflakeW: Float = snowflake.width.toFloat()
        var snowflakeH: Float = snowflake.height.toFloat()

        val newSnowflake = AppCompatImageView(context!!)
        newSnowflake.setImageResource(R.drawable.ic_snow_24dp)
        newSnowflake.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newSnowflake)

        newSnowflake.scaleX = Math.random().toFloat() * 1.8f + .1f
        newSnowflake.scaleY = newSnowflake.scaleX
        snowflakeW *= newSnowflake.scaleX
        snowflakeH *= newSnowflake.scaleY

        newSnowflake.translationX = Math.random().toFloat() * containerW - snowflakeW / 2

        val mover = ObjectAnimator.ofFloat(newSnowflake, View.TRANSLATION_Y, -snowflakeH, containerH + snowflakeH)
        mover.interpolator = AccelerateInterpolator(0.5f)

        val rotator = ObjectAnimator.ofFloat(newSnowflake, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newSnowflake)
            }
        })
        set.start()
    }


    companion object {
        fun newInstance(): FragmentTwo {
            return FragmentTwo()
        }
    }
}