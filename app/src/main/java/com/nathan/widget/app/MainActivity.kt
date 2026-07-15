package com.nathan.widget.app

import android.R.attr.text
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityMainBinding
import com.nathan.widget.app.databinding.ItemFuncBinding
import com.nathan.widget.app.util.startActivity
import com.nathan.widget.util.ScreenUtils

class MainActivity : BaseActivity() {

    private var funcMap = mapOf<String, String>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFuncMap()
        setupRecyclerView()
    }

    private fun setupFuncMap() {
        funcMap = mapOf(
            getString(R.string.button) to "小按钮、大按钮",
            getString(R.string.dialog) to "小弹窗、大弹窗",
            getString(R.string.smooth_layout) to "各种平滑圆角"
        )
    }

    private fun setupRecyclerView() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory("${packageName}.category.UI_PAGE_SCOPE")
        }
        val data = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
        if (data.isNotEmpty()) {
            binding.funcList.addItemDecoration(object : RecyclerView.ItemDecoration() {

                private val space = ScreenUtils.dp2Px(this@MainActivity, 5F).toInt()

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
            binding.funcList.adapter = FuncAdapter(generateFuncList(data))
        }
    }

    private fun generateFuncList(data: MutableList<ResolveInfo>): List<FuncModel> {
        return mutableListOf<FuncModel>().apply {
            data.forEach {
                val title = it.activityInfo.loadLabel(packageManager).toString()
                add(FuncModel(title, funcMap[title] ?: "", it))
            }
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityMainBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }

    class FuncModel(val title: String, val summary: String, val info: ResolveInfo)

    class FuncAdapter(private val data: List<FuncModel>) :
        RecyclerView.Adapter<FuncViewHolder>() {
        override fun onCreateViewHolder(container: ViewGroup, viewType: Int): FuncViewHolder {
            return FuncViewHolder(
                ItemFuncBinding.inflate(
                    LayoutInflater.from(container.context),
                    container,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: FuncViewHolder, pos: Int) {
            holder.binding.funcItem.setTitle(data[pos].title)
            holder.binding.funcItem.setSummary(data[pos].summary)
            holder.binding.funcItem.setOnClickListener {
                val info = data[pos].info.activityInfo
                it.context.startActivity(ComponentName(info.packageName, info.name))
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    class FuncViewHolder(val binding: ItemFuncBinding) : RecyclerView.ViewHolder(binding.root)
}