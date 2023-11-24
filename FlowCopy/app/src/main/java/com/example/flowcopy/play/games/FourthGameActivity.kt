package com.example.flowcopy.play.games

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.flowcopy.R

class FourthGameActivity : AppCompatActivity() {

    private lateinit var resetImg: ImageView
    private lateinit var closeBtn: ImageView
    private lateinit var gridLayout: GridLayout
    private lateinit var pointsInitial: Array<Array<Int>>
    private lateinit var pointsCurrent: Array<Array<Int>>
    private lateinit var pointsFinal: Array<Array<Int>>
    private val pointColors = intArrayOf(Color.GRAY, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE)

    private val viewIds = arrayOf(
        R.id.view1, R.id.view2, R.id.view3, R.id.view4, R.id.view5,
        R.id.view6, R.id.view7, R.id.view8, R.id.view9, R.id.view10,
        R.id.view11, R.id.view12, R.id.view13, R.id.view14, R.id.view15,
        R.id.view16, R.id.view17, R.id.view18, R.id.view19, R.id.view20,
        R.id.view21, R.id.view22, R.id.view23, R.id.view24, R.id.view25
    )

    private var startPoint: View? = null
    private var endPoint: View? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_game)

        resetImg = findViewById(R.id.resetView)

        resetImg.setOnClickListener(){
            pointsCurrent = pointsInitial
            initializeGrid()
        }

        closeBtn = findViewById(R.id.backView)

        closeBtn.setOnClickListener(){
            finish()
        }

        gridLayout = findViewById(R.id.gridLayout)
        pointsInitial = arrayOf(
            arrayOf(3, 0, 0, 1, 5),
            arrayOf(0, 0, 0, 4, 0),
            arrayOf(0, 0, 4, 0, 0),
            arrayOf(0, 1, 5, 0, 2),
            arrayOf(0, 3, 2, 0, 0)
        )
        pointsCurrent = pointsInitial.copyOf()
        pointsFinal = arrayOf(
            arrayOf(3, 1, 1, 1, 5),
            arrayOf(3, 1, 4, 4, 5),
            arrayOf(3, 1, 4, 5, 5),
            arrayOf(3, 1, 5, 5, 2),
            arrayOf(3, 3, 2, 2, 2)
        )

        initializeGrid()

        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i) as View

            view.setOnTouchListener { _, event ->
                val rawX = event.rawX
                val rawY = event.rawY

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startPoint = view
                        endPoint = null
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (startPoint != null) {
                            for (j in 0 until gridLayout.childCount) {
                                val otherView = gridLayout.getChildAt(j)

                                val location = IntArray(2)
                                otherView.getLocationOnScreen(location)
                                val viewX = location[0]
                                val viewY = location[1]

                                val relativeX = rawX - viewX
                                val relativeY = rawY - viewY

                                if (relativeX >= 0 && relativeX <= otherView.width &&
                                    relativeY >= 0 && relativeY <= otherView.height
                                ) {
                                    endPoint = otherView
                                    drawPath()
                                    break
                                }
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        startPoint = null
                        endPoint = null

                        if (checkPuzzleCompletion()) {
                            // Verifica se o quebra-cabeça foi concluído
                            Toast.makeText(this, "Parabéns! Você concluiu o quebra-cabeça.", Toast.LENGTH_SHORT).show()
                            Thread.sleep(2000)
                            val intent = Intent(this, FifthGameActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                true
            }
        }
    }

    private fun initializeGrid() {
        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i) as View
            val value = pointsInitial[i / gridLayout.columnCount][i % gridLayout.columnCount]

            // Configura o background inicial e cor do ponto baseado no valor
            view.setBackgroundColor(if (value == 0) Color.GRAY else pointColors[value])
            pointsCurrent[i / gridLayout.columnCount][i % gridLayout.columnCount] = value
        }
    }

    private fun drawPath() {
        if (startPoint != null && endPoint != null) {
            val startColor = (startPoint!!.background as? ColorDrawable)?.color ?: Color.GRAY
            startPoint!!.setBackgroundColor(startColor)

            if (startColor != Color.GRAY) {
                val endRow = viewIds.indexOf(endPoint!!.id) / gridLayout.columnCount
                val endCol = viewIds.indexOf(endPoint!!.id) % gridLayout.columnCount

                val startRow = viewIds.indexOf(startPoint!!.id) / gridLayout.columnCount
                val startCol = viewIds.indexOf(startPoint!!.id) % gridLayout.columnCount

                pointsInitial = arrayOf(
                    arrayOf(3, 0, 0, 1, 5),
                    arrayOf(0, 0, 0, 4, 0),
                    arrayOf(0, 0, 4, 0, 0),
                    arrayOf(0, 1, 5, 0, 2),
                    arrayOf(0, 3, 2, 0, 0)
                )
                // Altera o valor na matriz atual apenas se o valor na matriz inicial for 0
                if (pointsInitial[endRow][endCol] == 0) {
                    pointsCurrent[endRow][endCol] = pointsCurrent[startRow][startCol]
                    endPoint!!.setBackgroundColor(pointColors[pointsCurrent[endRow][endCol]])
                }

            }

        }
    }

    private fun checkPuzzleCompletion(): Boolean {
        for (i in pointsCurrent.indices) {
            for (j in pointsCurrent[i].indices) {
                if (pointsCurrent[i][j] != pointsFinal[i][j]) {
                    return false
                }
            }
        }
        return true
    }

}