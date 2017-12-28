package com.sunc.cube.utils

import android.graphics.Bitmap
import com.sunc.cube.bean.ImagePiece
import java.util.*

/**
 * Created by Administrator on 2017/10/25.
 */
object ImageSplitter {
    fun split(bitmap: Bitmap, xPiece: Int, yPiece: Int): List<ImagePiece> {
        val pieces = ArrayList<ImagePiece>(xPiece * yPiece)
        val width = bitmap.width
        val height = bitmap.height
        val pieceWidth = width / xPiece
        val pieceHeight = height / yPiece
        for (i in 0 until xPiece) {
            for (j in 0 until yPiece) {
                val piece = ImagePiece()
                piece.index = j + i * xPiece
                val xValue = j * pieceWidth
                val yValue = i * pieceHeight
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceHeight)
                pieces.add(piece)
            }
        }
        return pieces
    }
}