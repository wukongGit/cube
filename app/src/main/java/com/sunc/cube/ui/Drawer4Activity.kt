package com.sunc.cube.ui

import android.graphics.Bitmap
import com.sunc.cube.R
import com.sunc.cube.utils.ImageSplitter
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.layout_main4.*

class Drawer4Activity : DrawerActivity() {

    override fun getContentView(): Int = R.layout.layout_main4

    override fun placeBitmap(resource: Bitmap) {
        iv_origin.setImageBitmap(resource)
        val bmps = ImageSplitter.split(resource, 4, 4)
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
        setBitmap(iv_img10, bmps, indexs[10])
        setBitmap(iv_img11, bmps, indexs[11])
        setBitmap(iv_img12, bmps, indexs[12])
        setBitmap(iv_img13, bmps, indexs[13])
        setBitmap(iv_img14, bmps, indexs[14])
        setBitmap(iv_img15, bmps, indexs[15])
        setBitmap(iv_img16, bmps, indexs[16])
    }
}
