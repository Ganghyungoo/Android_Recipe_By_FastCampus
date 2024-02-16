package com.test.defencekeypadproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.children
import com.test.defencekeypadproject.databinding.WidgetShuffleNumberKeyboardBinding
import kotlin.random.Random

class ShuffleNumberKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : GridLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var _binding: WidgetShuffleNumberKeyboardBinding? = null
    val binding get() = _binding!!
    private var listener: KeyPadListener? = null

    init {
        _binding = WidgetShuffleNumberKeyboardBinding.inflate(LayoutInflater.from(context), this, true)
        binding.view = this
        binding.clickListener = this
        //뷰가 띄워질때 초기화작업에서 셔플 수행
        shuffle()
    }

    //커스텀 뷰에서 뷰바인딩/데이터 바인딩은 수동으로 없애줘야한다.
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    //키보드를 섞는 작업
    fun shuffle() {
        val keyNumberArray = ArrayList<String>()
        for (i in 0..9) {
            keyNumberArray.add(i.toString())
        }
        //자식뷰중에 숫자 키패드만 골라서 섞음
        binding.gridLayout.children.forEach { view ->
            if (view is TextView && view.tag != null) {
                val randIndex = Random.nextInt(keyNumberArray.size)
                view.text = keyNumberArray[randIndex]
                keyNumberArray.removeAt(randIndex)
            }
        }
    }

    fun setKeyPadListener(keyPadListener: KeyPadListener){
        this.listener = keyPadListener
    }

    fun onClickDelete(){
        listener?.onClickDelete()
    }

    fun onClickDone(){
        listener?.onClickDone()
    }

    interface KeyPadListener {
        fun onClickNum(num: String)
        fun onClickDelete()
        fun onClickDone()
    }

    override fun onClick(v: View) {
        if (v is TextView && v.tag != null) {
            listener?.onClickNum(v.text.toString())
        }
    }
}