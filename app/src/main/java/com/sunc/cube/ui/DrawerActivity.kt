package com.sunc.cube.ui

import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.CountListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.sunc.cube.App
import com.sunc.cube.R
import com.sunc.cube.bean.Achievement
import com.sunc.cube.bean.ImagePiece
import com.sunc.cube.bmob.Picture
import com.sunc.cube.bmob.Record
import com.sunc.cube.dialog.SuccessDialog
import com.sunc.cube.eventbus.NotifyType
import com.sunc.cube.toast
import com.sunc.cube.utils.*
import com.sunc.cube.view.PanelLayout
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.layout_main.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*

open class DrawerActivity : BaseActivity() {
    override fun onNotify(type: NotifyType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mTrack: ArrayList<String> = ArrayList()
    lateinit var mAdapter : AnswerAdapter
    var mLevel: Int = LEVEL_1
    var mPicture: Picture?= null
    var mAchievement: Achievement?= null

    var lastTime :Int = 0
    var lastRange: Int = 0
    var lastWorld: Int = 0

    var mSoundPool: SoundPool? = null
    var mSoundId : HashMap<Int, Int> = HashMap()
    var mSoundOn = false

    companion object {
        val LEVEL = "LEVEL"
        val LEVEL_1 = 10
        val LEVEL_2 = 20
        val LEVEL_3 = 30
        val LEVEL_4 = 50
        val LEVEL_5 = 100
        val PICTURE = "PICTURE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TranslucentStatusCompat.requestTranslucentStatus(this)
        setContentView(R.layout.activity_drawer)
        mLevel = intent.getIntExtra(LEVEL, LEVEL_1)
        mPicture = intent.getSerializableExtra(PICTURE) as Picture?
        init()
    }

    open fun getContentView():Int = R.layout.layout_main

    private fun init() {
        layoutInflater.inflate(getContentView(), rl_container)
        level.text = "${getString(R.string.level)}：$mLevel"
        initSound()
        getAchievement()
        queryRecord()
        progress_bar.visibility = View.VISIBLE
        Glide.with(this).load(mPicture!!.url).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                progress_bar.visibility = View.GONE
                if (resource == null) {
                    return
                }
                placeBitmap(resource)
            }
        })
        rv_answer.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        mAdapter = AnswerAdapter(this, mTrack)
        rv_answer.adapter = mAdapter
        layout_panel.setOnMoveListener(object : PanelLayout.OnMoveListener {
            override fun onInit(list: ArrayList<String>) {
                mTrack = list
            }

            override fun onStep() {
                if (mSoundOn) {
                    mSoundPool?.play(mSoundId[1]!!,1f,1f,0,0,3f)
                }
            }

            override fun onSuccess() {
                chronometer.stop()
                if (mSoundOn) {
                    mSoundPool?.play(mSoundId[2]!!,1f,1f,0,0,1f)
                }
                saveAchievement(chronometer.text.toString())
            }
        })

        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerOpened(drawerView: View?) {
                mAdapter.set(mTrack)
                mAdapter.notifyDataSetChanged()
            }

            override fun onDrawerClosed(drawerView: View?) {
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        iv_idea.setOnClickListener { view ->
            drawer_layout.openDrawer(Gravity.RIGHT)
        }
        iv_sound.setOnClickListener { view ->
            if (mSoundOn) {
                mSoundOn = false
                iv_sound.setImageResource(R.mipmap.sound_off)
            } else {
                mSoundOn = true
                iv_sound.setImageResource(R.mipmap.sound_on)
            }
        }
        chronometer.start()
    }

    private fun initSound() {
        mSoundPool = if (Build.VERSION.SDK_INT >= 21) {
            var builder: SoundPool.Builder = SoundPool.Builder()
            builder.setMaxStreams(2)
            var attrBuilder: AudioAttributes.Builder = AudioAttributes.Builder()
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
            builder.setAudioAttributes(attrBuilder.build())
            builder.build()
        } else {
            SoundPool(2, AudioManager.STREAM_MUSIC, 1)
        }
        mSoundId.put(1, mSoundPool!!.load(this, R.raw.sound_command, 1))
        mSoundId.put(2, mSoundPool!!.load(this, R.raw.sound_win, 1))
        mSoundOn = DBUtils.read(DBKeys.SOUND, true)
        if (mSoundOn) {
            iv_sound.setImageResource(R.mipmap.sound_on)
        } else {
            iv_sound.setImageResource(R.mipmap.sound_off)
        }
    }

    private fun success(title: String?, message: String?) {
        SuccessDialog.create(this).content(title).description(message).show()
    }

    open fun placeBitmap(resource: Bitmap) {
        iv_origin.setImageBitmap(resource)
        val bmps = ImageSplitter.split(resource, 3, 3)
        val indexs = layout_panel.disorder(mLevel)
        setBitmap(iv_img0, bmps, indexs[0])
        setBitmap(iv_img1, bmps, indexs[1])
        setBitmap(iv_img2, bmps, indexs[2])
        setBitmap(iv_img3, bmps, indexs[3])
        setBitmap(iv_img4, bmps, indexs[4])
        setBitmap(iv_img5, bmps, indexs[5])
        setBitmap(iv_img6, bmps, indexs[6])
        setBitmap(iv_img7, bmps, indexs[7])
        setBitmap(iv_img8, bmps, indexs[8])
        setBitmap(iv_img9, bmps, indexs[9])
    }

    fun getAchievement() {
        mAchievement = App.getAchievement()
        if(mAchievement == null) {
            return
        }
        when(mLevel) {
            LEVEL_1 -> {
                lastTime = mAchievement!!.time_1
                lastRange = mAchievement!!.range1
                lastWorld = mAchievement!!.world1

            }
            LEVEL_2 -> {
                lastTime = mAchievement!!.time_2
                lastRange = mAchievement!!.range2
                lastWorld = mAchievement!!.world2
            }
            LEVEL_3 -> {
                lastTime = mAchievement!!.time_3
                lastRange = mAchievement!!.range3
                lastWorld = mAchievement!!.world3
            }
            LEVEL_4 -> {
                lastTime = mAchievement!!.time_4
                lastRange = mAchievement!!.range4
                lastWorld = mAchievement!!.world4
            }
            LEVEL_5 -> {
                lastTime = mAchievement!!.time_5
                lastRange = mAchievement!!.range5
                lastWorld = mAchievement!!.world5
            }
        }
        if(lastRange > 0) {
            var rangeDes = String.format(getString(R.string.range_description), lastRange.toString())
            range.text = "${getString(R.string.best_range)}：$rangeDes， $lastTime${getString(R.string.second)} "
        }
        if (lastWorld > 0) {
            record.text = "${getString(R.string.world_record)}：$lastWorld${getString(R.string.second)} "
        }
    }

    fun saveAchievement(str: String) {
        var thisTime = TimeUtil.getTimeSecond((this@DrawerActivity),str)
        val r = if (thisTime > lastTime) lastRange + 1 else lastRange - 1
        var rangeDes = String.format(getString(R.string.success_description), thisTime, mLevel, r)
        success(rangeDes, mPicture?.cai)

        if (mAchievement == null) {
            mAchievement = Achievement()
        }
        when(mLevel) {
            LEVEL_1 -> {
                val lastTime :Int= mAchievement!!.time_1
                if (thisTime < lastTime || lastTime == 0) {
                    mAchievement!!.time_1 = thisTime
                }
            }
            LEVEL_2 -> {
                val lastTime :Int= mAchievement!!.time_2
                if (thisTime < lastTime || lastTime == 0) {
                    mAchievement!!.time_2 = thisTime
                }
            }
            LEVEL_3 -> {
                val lastTime :Int= mAchievement!!.time_3
                if (thisTime < lastTime || lastTime == 0) {
                    mAchievement!!.time_3 = thisTime
                }
            }
            LEVEL_4 -> {
                val lastTime :Int= mAchievement!!.time_4
                if (thisTime < lastTime || lastTime == 0) {
                    mAchievement!!.time_4 = thisTime
                }
            }
            LEVEL_5 -> {
                val lastTime :Int= mAchievement!!.time_5
                if (thisTime < lastTime || lastTime == 0) {
                    mAchievement!!.time_5 = thisTime
                }
            }
        }
        App.setAchievement(mAchievement)
        var record = Record()
        record.level = mLevel
        record.time = thisTime
        record.save(object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
            }
        })
    }

    fun saveWorld(world: Int) {
        if (mAchievement == null) {
            mAchievement = Achievement()
        }
        when(mLevel) {
            LEVEL_1 -> mAchievement!!.world1 = world
            LEVEL_2 -> mAchievement!!.world2 = world
            LEVEL_3 -> mAchievement!!.world3 = world
            LEVEL_4 -> mAchievement!!.world4 = world
            LEVEL_5 -> mAchievement!!.world5 = world
        }
        App.setAchievement(mAchievement)
    }

    fun saveRange(range: Int) {
        if (mAchievement == null) {
            mAchievement = Achievement()
        }
        when(mLevel) {
            LEVEL_1 -> mAchievement!!.range1 = range
            LEVEL_2 -> mAchievement!!.range2 = range
            LEVEL_3 -> mAchievement!!.range3 = range
            LEVEL_4 -> mAchievement!!.range4 = range
            LEVEL_5 -> mAchievement!!.range5 = range
        }
        App.setAchievement(mAchievement)
    }

    fun queryRecord() {
        if (!AndroidUtils.isNetworkConnected(this)) {
            toast(getString(R.string.network_error))
            return
        }
        var query = BmobQuery<Record>()
        query.addWhereEqualTo("level", mLevel)
        query.min(arrayOf("time"))
        query.findStatistics(Record::class.java, object: QueryListener<JSONArray>() {
            override fun done(p0: JSONArray?, p1: BmobException?) {
                if (p1 == null) {
                    if (p0 != null) {
                        try {
                            val obj = p0.getJSONObject(0)
                            val min = obj.getInt("_minTime")
                            if (min > 0) {
                                record.text = "${getString(R.string.world_record)}：$min${getString(R.string.second)} "
                                saveWorld(min)
                            }
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }

                    }
                }
            }
        })

        val query_1 = BmobQuery<Record>()
        query_1.addWhereEqualTo("level", mLevel)
        query_1.addWhereLessThan("time", lastTime)
        query_1.count(Record::class.java, object : CountListener() {
            override fun done(count: Int?, e: BmobException?) {
                if (e == null) {
                    range.text = "${getString(R.string.best_range)}：${(count!! + 2)}， $lastTime${getString(R.string.second)} "
                    saveRange(count+2)
                }
            }
        })
    }

    fun setBitmap(v : ImageView, bmps: List<ImagePiece>, i :Int) {
        if(i == 0) return
        v.setImageBitmap(bmps[i - 1].bitmap)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
            drawer_layout.closeDrawer(Gravity.RIGHT)
        } else {
            mSoundPool?.release()
            super.onBackPressed()
        }
    }
}
