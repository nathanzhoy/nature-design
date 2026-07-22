package com.nathan.widget.app

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityListBinding
import com.nathan.widget.app.databinding.ItemListButtonBinding
import com.nathan.widget.app.databinding.ItemListDotBinding
import com.nathan.widget.app.databinding.ItemListForwardingBinding
import com.nathan.widget.app.databinding.ItemListNumberBinding
import com.nathan.widget.app.databinding.ItemListSwitchBinding
import com.nathan.widget.ui.list.NatureListItemView
import com.nathan.widget.ui.view.NatureButton
import com.nathan.widget.util.ScreenUtils

class ListActivity : BaseActivity() {

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val models = getListModel()
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {

            private val space = ScreenUtils.dp2Px(this@ListActivity, 5F).toInt()

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                // 上下间隔5dp，即顶部和底部各添加5dp
                outRect.top = space
                outRect.bottom = space
            }
        })
        binding.recyclerView.adapter = object : RecyclerView.Adapter<ListItemViewHolder>() {

            override fun onCreateViewHolder(
                container: ViewGroup,
                viewType: Int
            ): ListItemViewHolder {
                return when (viewType) {
                    0 -> {
                        ListItemViewHolder(
                            ItemListForwardingBinding.inflate(
                                layoutInflater,
                                container,
                                false
                            )
                        )
                    }

                    1 -> {
                        ListItemViewHolder(
                            ItemListButtonBinding.inflate(
                                layoutInflater,
                                container,
                                false
                            )
                        )

                    }

                    2 -> {
                        ListItemViewHolder(
                            ItemListSwitchBinding.inflate(
                                layoutInflater,
                                container,
                                false
                            )
                        )

                    }

                    3 -> {
                        ListItemViewHolder(
                            ItemListDotBinding.inflate(
                                layoutInflater,
                                container,
                                false
                            )
                        )

                    }

                    else -> {
                        ListItemViewHolder(
                            ItemListNumberBinding.inflate(
                                layoutInflater,
                                container,
                                false
                            )
                        )
                    }
                }
            }

            override fun onBindViewHolder(holder: ListItemViewHolder, pos: Int) {
                val model = models[pos]
                val listView = holder.binding.root.findViewById<NatureListItemView>(R.id.list_view)
                listView.setAvatar(model.avatar)
                listView.setTitle(model.title)
                listView.setSummary(model.summary)
                listView.setExtraSummary(model.extra)
                if (model.type == Type.BUTTON) {
                    val binding = holder.binding as ItemListButtonBinding
                    val button = binding.listView.findViewById<NatureButton>(R.id.button)
                    button.setOnClickListener {
                        Toast.makeText(it.context, "点击按钮", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
                if (model.type == Type.SWITCH) {
                    val binding = holder.binding as ItemListSwitchBinding
                    binding.listView.setToggleChangeListener { it, isCheck ->
                        Toast.makeText(it.context, "点击开关：$isCheck", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
                if (model.type == Type.DOT) {
                    val binding = holder.binding as ItemListDotBinding
                    binding.listView.setOnClickListener {
                        val itemView = it as NatureListItemView
                        itemView.setDot(true)
                    }
                    return
                }
                if (model.type == Type.NUMBER) {
                    val binding = holder.binding as ItemListNumberBinding
                    binding.listView.setOnClickListener {
                        val itemView = it as NatureListItemView
                        itemView.setNumber(10.toString(), false)
                    }
                    return
                }
                if (model.type == Type.FORWARDING) {
                    val binding = holder.binding as ItemListForwardingBinding
                    binding.listView.setOnClickListener {
                        Toast.makeText(it.context, "点击进入下一页", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }

            override fun getItemCount(): Int {
                return models.size
            }

            override fun getItemViewType(position: Int): Int {
                return models[position].type.ordinal
            }
        }
    }

    private fun getListModel(): MutableList<ListItemModel> {
        return mutableListOf<ListItemModel>().apply {
            add(
                ListItemModel(
                    R.drawable.ic_demo,
                    "前进Forwarding",
                    "预设前进布局（默认）",
                    "警告",
                    Type.FORWARDING
                )
            )
            add(ListItemModel(R.drawable.ic_demo, "开关Toggle", "预设开关布局", "警告", Type.SWITCH))
            add(ListItemModel(R.drawable.ic_demo, "点Dot", "预设提示布局", "警告", Type.DOT))
            add(ListItemModel(R.drawable.ic_demo, "数字Number", "预设数字布局", "警告", Type.NUMBER))

            add(ListItemModel(R.drawable.ic_demo, "按钮Custom", "自定义按钮布局", "警告", Type.BUTTON))
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityListBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }

    class ListItemViewHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    enum class Type {
        FORWARDING,
        BUTTON,
        SWITCH,
        DOT,
        NUMBER
    }

    class ListItemModel(
        var avatar: Int,
        var title: String,
        var summary: String,
        val extra: String,
        var type: Type
    )
}