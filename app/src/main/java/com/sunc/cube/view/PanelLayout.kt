package com.sunc.cube.view

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.layout_main.view.*
import java.util.*

/**
 * Created by Administrator on 2017/10/24.
 */
class PanelLayout : RelativeLayout {
    private var mDragger: ViewDragHelper? = null
    private var mCallback: ViewDragHelper.Callback? = null
    private var mSpacePosition = IntArray(2)
    private var mSpaceTag: Int? = 0
    private var mMoveDirection = 0
    private val MOVE_UP = 1
    private val MOVE_DOWN = 2
    private val MOVE_LEFT = 3
    private val MOVE_RIGHT = 4
    private var mSqure = 3
    private var mViewIndex = intArrayOf(0,1,2,3,4,5,6,7,8,9)
    private var mTrack: ArrayList<String> = ArrayList()
    private var mListener: OnMoveListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mCallback = DraggerCallBack()
        mDragger = ViewDragHelper.create(this, 1.0f, mCallback!!)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragger!!.cancel()
            return false
        }
        return mDragger!!.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragger!!.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mSpacePosition[0] = iv_img0.left
        mSpacePosition[1] = iv_img0.top
        when(childCount) {
            10 -> {
                mSqure = 3
                mViewIndex = intArrayOf(0,1,2,3,4,5,6,7,8,9)
            }
            17 -> {
                mSqure = 4
                mViewIndex = intArrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16)
            }
        }

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!changed) {
            // 防止界面重绘的时候，引起重新layout，导致移动的位置发生复原
            return
        }
        super.onLayout(changed, l, t, r, b)
    }

    override fun computeScroll() {
        if (mDragger!!.continueSettling(true)) {
            invalidate()
        }
    }

    private fun getCanMove(): IntArray? {
        when(mSqure) {
            3 -> {
                when(mSpaceTag) {
                    0 -> return intArrayOf(1)
                    1 -> return intArrayOf(0, 2, 4)
                    2 -> return intArrayOf(1, 3, 5)
                    3 -> return intArrayOf(2, 6)
                    4 -> return intArrayOf(1, 5, 7)
                    5 -> return intArrayOf(2, 4, 6, 8)
                    6 -> return intArrayOf(3, 5, 9)
                    7 -> return intArrayOf(4, 8)
                    8 -> return intArrayOf(5, 7, 9)
                    9 -> return intArrayOf(6, 8)
                }
            }
            4 -> {
                when(mSpaceTag) {
                    0 -> return intArrayOf(1)
                    1 -> return intArrayOf(0, 2, 5)
                    2 -> return intArrayOf(1, 3, 6)
                    3 -> return intArrayOf(2, 4, 7)
                    4 -> return intArrayOf(3, 8)
                    5 -> return intArrayOf(1, 6, 9)
                    6 -> return intArrayOf(2, 5, 7, 10)
                    7 -> return intArrayOf(3, 6, 8, 11)
                    8 -> return intArrayOf(4, 7, 12)
                    9 -> return intArrayOf(5, 10, 13)
                    10 -> return intArrayOf(6, 9, 11, 14)
                    11 -> return intArrayOf(7, 10, 12, 15)
                    12 -> return intArrayOf(8, 11, 16)
                    13 -> return intArrayOf(9, 14)
                    14 -> return intArrayOf(10, 13, 15)
                    15 -> return intArrayOf(11, 14, 16)
                    16 -> return intArrayOf(12, 15)
                }
            }
        }
        return null
    }

    private fun getMoveDirection(tag : Int) : Int? {
        val yu = mSpaceTag!! % mSqure
        val cha = tag - mSpaceTag!!
        when(yu) {
            0 -> {
                when(cha) {
                    1 -> if (mSpaceTag == 0) return MOVE_UP else return 0
                    -1 -> return MOVE_RIGHT
                    mSqure -> if (mSpaceTag == 0) return 0 else return MOVE_UP
                    -mSqure -> return MOVE_DOWN
                }
            }
            1 -> {
                when(cha) {
                    1 -> return MOVE_LEFT
                    mSqure -> return MOVE_UP
                    -mSqure -> return MOVE_DOWN
                    -1 -> if (mSpaceTag == 1) return MOVE_DOWN else return 0
                }
            }
            else -> {
                when(cha) {
                    1 -> return MOVE_LEFT
                    -1 -> return MOVE_RIGHT
                    mSqure -> return MOVE_UP
                    -mSqure -> return MOVE_DOWN
                }
            }
        }
        return 0
    }

    fun disorder(step: Int) : IntArray {
        val random = Random()
        while (isInit()) {
            var num = step
            while (num > 0) {
                val array = getCanMove()
                val next = array!![random.nextInt(array.size)]
                move(next)
                num--
            }
            while (mSpaceTag != 0) {
                val array = getCanMove()
                val next = array!![0]
                move(next)
            }
        }
        mListener?.onInit(mTrack)
        return mViewIndex
    }

    fun isInit(): Boolean {
        return (0 until mSqure * mSqure).none { mViewIndex[it] != it }
    }

    fun move(v: Int) {
        var track = "($v, $mSpaceTag)"
        if(mTrack.isEmpty()) {
            mTrack.add(track)
        } else {
            val last = mTrack.last()
            when(last) {
                "($mSpaceTag, $v)" -> {
                    mTrack.removeAt(mTrack.size - 1)
                }
                else -> {
                    mTrack.add(track)
                }
            }
        }

        val space = mSpaceTag
        val temp = mViewIndex[space!!]
        mViewIndex[space] = mViewIndex[v]
        mViewIndex[v] = temp
        mSpaceTag = v
    }

    inner class DraggerCallBack : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
            val tag = child!!.tag as String
            mMoveDirection = getMoveDirection(tag.toInt())!!
            return mMoveDirection > 0
        }

        override fun clampViewPositionHorizontal(child: View?, left: Int, dx: Int): Int {
            var leftBound = child!!.left
            var rightBound = child.left
            when(mMoveDirection) {
                MOVE_LEFT -> {
                    leftBound = child.left - child.width
                }
                MOVE_RIGHT -> {
                    rightBound = child.left + child.width
                }
            }
            return Math.min(Math.max(left, leftBound), rightBound)
        }

        override fun clampViewPositionVertical(child: View?, top: Int, dy: Int): Int {
            var topBound = child!!.top
            var bottomBound = child.top
            when(mMoveDirection) {
                MOVE_UP -> {
                    topBound = child.top - child.height
                }
                MOVE_DOWN -> {
                    bottomBound = child.top + child.height
                }
            }
            return Math.min(Math.max(top, topBound), bottomBound)
        }

        override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
            var x : Int = mSpacePosition[0]
            var y : Int = mSpacePosition[1]

            val viewTag = releasedChild!!.tag as String
            releasedChild.tag=mSpaceTag.toString()
            move(viewTag.toInt())

            when(mMoveDirection) {
                MOVE_UP -> {
                    y += releasedChild.height
                }
                MOVE_DOWN -> {
                    y -= releasedChild.height
                }
                MOVE_LEFT -> {
                    x += releasedChild.width
                }
                MOVE_RIGHT -> {
                    x -= releasedChild.width
                }
            }
            mDragger!!.settleCapturedViewAt(mSpacePosition[0], mSpacePosition[1])
            mSpacePosition[0] = x
            mSpacePosition[1] = y
            invalidate()

            if(isInit()) {
                mListener?.onSuccess()
            } else {
                mListener?.onStep()
            }
        }
    }

    fun setOnMoveListener(listener: OnMoveListener) {
        mListener = listener
    }

    interface OnMoveListener {
        fun onInit(list: ArrayList<String>)
        fun onStep()
        fun onSuccess()
    }
}
