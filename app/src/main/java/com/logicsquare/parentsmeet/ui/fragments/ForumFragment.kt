package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.adapter.MommyForumAdapter
import com.logicsquare.parentsmeet.databinding.FragmentForumBinding
import com.logicsquare.parentsmeet.interfaces.OnItemClickId
import com.logicsquare.parentsmeet.interfaces.OnItemClickPosition
import com.logicsquare.parentsmeet.model.ForumResponse
import com.logicsquare.parentsmeet.model.GetAllForum
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumFragment : Fragment(),OnItemClickId {
    private lateinit var binding: FragmentForumBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentForumBinding.inflate(inflater,container,false)
        getData()
        binding.searchText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getData()
                return@OnEditorActionListener true
            }
            false
        })
        binding.searchIcon.setOnClickListener{
            getData()
        }


        return binding.root
    }

    private fun getData() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var getForum = GetAllForum()
        getForum.search = binding.searchText.text.toString()

        val call: Call<ForumResponse?> =
            APIClient.client.create(APIInterface::class.java).getForums(token,getForum)
        showProgressBar()
        call.enqueue(object : Callback<ForumResponse?> {
            override fun onResponse(
                call: Call<ForumResponse?>,
                response: Response<ForumResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null)
                        handleResponse(response.body()!!)
                } else{
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<ForumResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun handleResponse(body: ForumResponse) {
        if(body.forums!=null){
        binding.replyList.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.replyList.adapter=MommyForumAdapter(this, body.forums)
        }
    }

    override fun OnClick(Id: String) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.addToBackStack("FORUM_DETAIL")
        val bundle = Bundle()
        bundle.putString(ID,Id)
        val fragment = ForumDetailFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}