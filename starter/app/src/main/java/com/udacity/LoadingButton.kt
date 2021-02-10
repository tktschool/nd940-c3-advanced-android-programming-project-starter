package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var text: String = resources.getString(R.string.button_name)
    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private val textRect = Rect()
    private var loadLevel = 0f

//    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
//    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
//    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
//    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

//    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    private val circleRadius = 20.0f

    private val valueAnimator = ValueAnimator.ofFloat(0F, 1f)

    private val circleDiameter = circleRadius * 2
    private val circletSize = measuredHeight.toFloat() - paddingBottom.toFloat() - circleDiameter


    private val rectF = RectF(
    circleDiameter,
    circleDiameter,
    circletSize ,
    circletSize
    )

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {

            }
            ButtonState.Loading -> {
                text = resources.getString(R.string.button_loading)
                valueAnimator.repeatMode = ValueAnimator.REVERSE
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.duration =400L
                //valueAnimator.disableViewDuringAnimation()
                valueAnimator.start()
                invalidate()
            }
            ButtonState.Completed -> {
                text = resources.getString(R.string.button_name)
                loadLevel = 0f
                valueAnimator.cancel()
                invalidate()
            }

        }
    }

    fun setState(_buttonState: ButtonState){
        buttonState = _buttonState
    }

    private var bgColor = 0
    private var textColor = 0
    private var loadingColor = 0


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            bgColor = getColor(R.styleable.LoadingButton_bgColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
        }
        valueAnimator.addUpdateListener {
            loadLevel = it.animatedValue as Float
            invalidate()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private fun PointF.computeXYForText(textRect: Rect) {
        x = widthSize.toFloat() / 2
        y = heightSize.toFloat() / 2 - textRect.centerY()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(bgColor)

        paint.getTextBounds(text, 0, text.length, textRect)
        pointPosition.computeXYForText(textRect)
        paint.color = Color.BLACK
        canvas?.drawText(text, pointPosition.x, pointPosition.y, paint)
        if(buttonState == ButtonState.Loading) {
            drawArcLoading(canvas)
        }

    }

    private fun drawArcLoading(canvas: Canvas?) {
       // canvas?.save()
        canvas?.translate(
            widthSize.toFloat() / 2 + textRect.width() / 2 + smallRectOffset,
            measuredHeight.toFloat() / 2
        )
        paint.color = getColor(context, R.color.colorAccent)
        canvas?.drawArc(rectF, 0F, 360F*loadLevel, true, paint)
      //  canvas?.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun ValueAnimator.disableViewDuringAnimation(){
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isEnabled = true
            }
        })
    }
}