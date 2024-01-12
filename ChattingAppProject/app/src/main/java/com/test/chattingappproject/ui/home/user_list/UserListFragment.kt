package com.test.chattingappproject.ui.home.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.R
import com.test.chattingappproject.adapter.UserAdapter
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.databinding.FragmentUserListBinding
import com.test.chattingappproject.viewModel.UserListViewModel

class UserListFragment : Fragment() {
    lateinit var fragmentUserListBinding: FragmentUserListBinding
    lateinit var userListViewModel:UserListViewModel
    lateinit var userAdapter:UserAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserListBinding = FragmentUserListBinding.inflate(layoutInflater)
        userListViewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        userListViewModel.userList.observe(viewLifecycleOwner){
            userAdapter.submitList(it)
        }

        userAdapter = UserAdapter()
        val linearLayoutManager = LinearLayoutManager(this@UserListFragment.context)
        fragmentUserListBinding.userRecyclerView.run {
            adapter = userAdapter
            layoutManager = linearLayoutManager
        }

        //유저 정보 끌어오기
        val myId = Firebase.auth.uid
        userListViewModel.fetchUsers(myId!!)



        return fragmentUserListBinding.root
    }
}