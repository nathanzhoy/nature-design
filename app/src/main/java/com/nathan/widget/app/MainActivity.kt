package com.nathan.widget.app

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nathan.widget.app.databinding.ActivityMainBinding
import com.nathan.widget.app.databinding.ItemFuncBinding
import com.nathan.widget.app.util.startActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory("${packageName}.category.UI_PAGE_SCOPE")
        }
        val data = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
        if (data.isNotEmpty()) {
            binding.funcList.adapter = FuncAdapter(data)
        }
    }

    override fun getViewBinding(): ViewBinding {
        return ActivityMainBinding.inflate(layoutInflater).apply {
            binding = this
        }
    }

    class FuncAdapter(private val data: List<ResolveInfo>) :
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
            val text = data[pos].activityInfo.loadLabel(holder.itemView.context.packageManager)
            holder.binding.funcTitle.text = text
            holder.binding.root.setOnClickListener {
                val info = data[pos].activityInfo
                it.context.startActivity(ComponentName(info.packageName, info.name))
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    class FuncViewHolder(val binding: ItemFuncBinding) : RecyclerView.ViewHolder(binding.root)
}