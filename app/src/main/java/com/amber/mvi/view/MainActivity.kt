package com.amber.mvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.amber.mvi.databinding.ActivityMainBinding
import com.amber.mvi.databinding.ItemBinding
import com.amber.mvi.intent.MainState
import com.amber.mvi.model.User
import com.amber.mvi.view.MainViewModel
import com.amber.mvi.view.MainViewModelFactory
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(UserRepositoryImpl(NetworkUtil.apiService))
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val adapter = UserAdapter()
        binding.rv.adapter = adapter

        viewModel.state.observe(this, Observer {
            when (it) {
                MainState.Idle -> {
                    binding.pb.visibility = View.GONE
                }
                MainState.Loading -> {
                    binding.pb.visibility = View.VISIBLE
                }
                is MainState.Users -> {
                    binding.pb.visibility = View.GONE
                    adapter.renderList(it.user[0])
                }
                is MainState.Error -> {
                    binding.pb.visibility = View.GONE
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}

class UserAdapter(val userList: MutableList<User> = mutableListOf()) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User) {
            binding.user = user
        }
    }

    fun renderList(user: User) {
        this.userList.add(user)
        notifyItemInserted(userList.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(userList[position])
    }

    override fun getItemCount() = userList.size
}

@BindingAdapter("img")
fun setImage(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(it)
            .into(view)
    }
}