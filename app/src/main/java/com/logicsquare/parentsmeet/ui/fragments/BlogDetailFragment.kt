package com.logicsquare.parentsmeet.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.logicsquare.parentsmeet.databinding.FragmentBlogDetailBinding
import com.logicsquare.parentsmeet.model.BlogDetailsResponse
import com.logicsquare.parentsmeet.model.BlogsItem
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.BlogsAdapter
import com.logicsquare.parentsmeet.utils.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogDetailFragment(blogsId: String) : Fragment(), BlogsAdapter.OnItemClickListener {

    var blogId = blogsId
    lateinit var binding: FragmentBlogDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }



    }

    private fun getBlogDetails() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<BlogDetailsResponse?> =
            APIClient.client.create(APIInterface::class.java).getBlogDetails(token, blogId)
        showProgressBar()
        call.enqueue(object : Callback<BlogDetailsResponse?> {
            override fun onResponse(
                call: Call<BlogDetailsResponse?>,
                response: Response<BlogDetailsResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        handleResponse(response.body()!!)
                    }
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<BlogDetailsResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })

    }

    private fun handleResponse(response: BlogDetailsResponse) {

        var spacing = 30;

        binding.rvRelatedContent.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })

        if(!response.blog?.coverImage.isNullOrEmpty()) {
            val transformation: Transformation = RoundedCornersTransformation(20, 0)
            Picasso.with(activity).load(response.blog?.coverImage).transform(transformation)
                .into(binding.ivBlog)
        }
        binding.tvTitle.text = response.blog?.title
        binding.tvDescription.text = Html.fromHtml(response.blog?.description)

        binding.tvLabel.visibility = if (response.relatedBlogs?.isNotEmpty()!!) {
            View.VISIBLE
        } else {
            View.GONE
        }
        var adapter =
            BlogsAdapter((response.relatedBlogs as ArrayList<BlogsItem>), requireContext())
        adapter.setListener(this)
        binding.rvRelatedContent.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBlogDetailBinding.inflate(inflater)
        getBlogDetails()
        return binding.root
    }

    override fun onClick(blog: BlogsItem) {

    }
}