package com.test.chattingappproject.ui.home.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.chattingappproject.R
import com.test.chattingappproject.adapter.UserAdapter
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {
    lateinit var fragmentUserListBinding: FragmentUserListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserListBinding = FragmentUserListBinding.inflate(layoutInflater)

        val userAdapter = UserAdapter()
        val linearLayoutManager = LinearLayoutManager(this@UserListFragment.context)
        fragmentUserListBinding.userRecyclerView.run {
            adapter = userAdapter
            layoutManager = linearLayoutManager
        }

        userAdapter.submitList(
            mutableListOf(UserModel("12","12","12"))
        )


        return fragmentUserListBinding.root
    }
}