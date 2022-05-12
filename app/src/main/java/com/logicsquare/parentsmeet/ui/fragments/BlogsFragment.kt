package com.logicsquare.parentsmeet.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentBlogsBinding
import com.logicsquare.parentsmeet.model.BlogsItem
import com.logicsquare.parentsmeet.model.BlogsResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.BlogsAdapter
import com.logicsquare.parentsmeet.utils.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BlogsFragment : Fragment(), BlogsAdapter.OnItemClickListener {

    lateinit var binding: FragmentBlogsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAllBlogs()
    }

    private fun getAllBlogs() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        Log.e("getAllBlogs", "token= $token")
        val call: Call<BlogsResponse?> =
            APIClient.client.create(APIInterface::class.java).getAllBlogs(token)
        showProgressBar()
        call.enqueue(object : Callback<BlogsResponse?> {
            override fun onResponse(
                call: Call<BlogsResponse?>,
                response: Response<BlogsResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null)
                        handleResponse(response.body()!!)
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<BlogsResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })

    }


    private fun handleResponse(response: BlogsResponse) {

        var spacing = 30;

        binding.rvBlogs.addItemDecoration(object : ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })

        val transformation: Transformation = RoundedCornersTransformation(20, 0)
        if (!response.blogs?.get(0)?.coverImage.isNullOrEmpty()) {
            Picasso.with(activity).load(response.blogs?.get(0)?.coverImage)
                .transform(transformation)
                .into(binding.ivBlog)
        }

        binding.tvTitle.text = response.blogs?.get(0)?.title
        var adapter = BlogsAdapter((response.blogs as ArrayList<BlogsItem>), requireContext())
        adapter.setListener(this)
        binding.rvBlogs.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBlogsBinding.inflate(inflater)
        return binding.root
    }

    override fun onClick(blog: BlogsItem) {
        if (blog.id.isNullOrEmpty()) {
            return
        }
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.container, BlogDetailFragment(blog.id!!))
        transaction?.addToBackStack("Blog details")
        transaction?.commit()
    }
}