package com.hardtm.daggerpro

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

@SuppressLint("AppCompatCustomView")
class NetworkStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    private var lastShowInvocation = 0L
    private var lastHideInvocation = 0L
    private var hideRunnable = Runnable { animateHide() }
    private val mHandler = Handler(Looper.getMainLooper())

    init {
        visibility = GONE
        alpha = 0f
        gravity = Gravity.CENTER
        setPadding(
            context.dpToPx(LEFT_PADDING),
            context.dpToPx(TOP_PADDING),
            context.dpToPx(LEFT_PADDING),
            context.dpToPx(TOP_PADDING)
        )
        minHeight = context.dpToPx(MIN_HEIGHT)
        layoutParams = FrameLayout.LayoutParams(context.dpToPx(WIDTH), WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            bottomMargin = context.dpToPx(BOT_MARGIN)
        }
        setText(context.getText(R.string.network_offline))
        setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        textSize = TEXT_SIZE
        setBackgroundResource(R.drawable.background_network_rounded)
        elevation = 4f
    }

    fun show() {
        mHandler.removeCallbacks(hideRunnable)
        post { animateShow() }
    }

    private fun animateShow() {
        val invokeTime = System.currentTimeMillis()
        if (invokeTime - lastHideInvocation < ANIM_COOLDOWN) return
        if (visibility == GONE) {
            lastShowInvocation = invokeTime
            this.animate().alpha(1f).setDuration(ANIM_DURATION)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        visibility = View.VISIBLE
                        alpha = 1f
                        hideDelayed()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        visibility = GONE
                        alpha = 0f
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        visibility = View.VISIBLE
                        alpha = 0f
                    }
                }).start()
        } else {
            hideDelayed()
        }
    }

    private fun animateHide() {
        lastHideInvocation = System.currentTimeMillis()
        this.animate().alpha(0f)
            .setDuration(ANIM_DURATION)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    alpha = 0f
                    visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    visibility = GONE
                    alpha = 0f
                }

                override fun onAnimationStart(animation: Animator?) {
                    visibility = View.VISIBLE
                    alpha = 1f
                }
            }).start()
    }

    private fun hideDelayed() {
        if (visibility != View.VISIBLE) return
        val hideDelay = (lastShowInvocation - System.currentTimeMillis() + MIN_HIDE_DELAY)
        if (hideDelay > 0) {
            mHandler.postDelayed(hideRunnable, hideDelay)
        } else {
            post { animateHide() }
        }
    }

    //Extention
    fun Context.dpToPx(dp: Float) : Int {
        return (dp * this.resources.displayMetrics.density).toInt()
    }

    companion object {
        const val MIN_HEIGHT = 36f
        const val WIDTH = 250f
        const val LEFT_PADDING = 8f
        const val TOP_PADDING = 4f
        const val BOT_MARGIN = 18f
        const val TEXT_SIZE = 14f

        const val ANIM_DURATION = 150L
        const val ANIM_COOLDOWN = 15000L
        const val MIN_HIDE_DELAY = 2000L
    }
}