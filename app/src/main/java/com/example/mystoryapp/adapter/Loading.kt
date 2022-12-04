package com.example.mystoryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mystoryapp.databinding.ItemLoadingBinding

class Loading(var retry: () -> Unit): LoadStateAdapter<Loading.LoadingViewHolder>() {
    inner class LoadingViewHolder(private val binding: ItemLoadingBinding, retry: (() -> Unit)): RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState){
            if (loadState is LoadState.Error){
                binding.tvError.text = loadState.error.localizedMessage
            }
            binding.pbLoading.isVisible = loadState is LoadState.Loading
            binding.tvError.isVisible = loadState is LoadState.Error
        }
        init {
            binding.btnUlangi.setOnClickListener { retry.invoke() }
        }

    }

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingViewHolder(binding, retry)
    }


}