package com.logicsquare.parentsmeet.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.adapter.CommentsForumAdapter
import com.logicsquare.parentsmeet.databinding.ForumDetailBinding
import com.logicsquare.parentsmeet.model.*
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.DrawerActivity
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForumDetailFragment : Fragment() {
    val _liveDataOne = MutableLiveData<ForumDetailResponse>()

    val _liveDataTwo = MutableLiveData<GetAllCommentsResponse>()
    val pairMediatorLiveData = PairMediatorLiveData(_liveDataOne, _liveDataTwo)


    private lateinit var binding: ForumDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ForumDetailBinding.inflate(inflater, container, false)

        binding.txtCommentSend.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage()
                return@OnEditorActionListener true
            }
            false
        })
        binding.imgSend.setOnClickListener {
            sendMessage()
        }
        binding.forumLike.setOnClickListener { likeMessage() }

        showProgressBar()
        getData()
        getMessages()
        pairMediatorLiveData.observe(viewLifecycleOwner) {
            hideProgressBar()
            it.first?.let { it1 -> handleResponse(it1) }
            it.second?.let { it -> handleAllCommentsResponse(it) }
        }
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.menuOption.setOnClickListener { startActivity(Intent(requireActivity(), DrawerActivity::class.java)) }



        return binding.root
    }

    private fun getMessages() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var getComentRequest = GetComentRequest()
        getComentRequest .limit = "50"
        val call: Call<GetAllCommentsResponse?> =
            APIClient.client.create(APIInterface::class.java)
                .getAllComments(token, arguments?.getString(ID, "").toString(), getComentRequest)
        //showProgressBar()
        call.enqueue(object : Callback<GetAllCommentsResponse?> {
            override fun onResponse(
                call: Call<GetAllCommentsResponse?>,
                response: Response<GetAllCommentsResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null){
                        _liveDataTwo?.postValue(response.body()!!)
                    }

                } else {
                    hideProgressBar()
                    handleErrorResponse(response.errorBody(), requireContext())
                }

            }

            override fun onFailure(call: Call<GetAllCommentsResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun sendMessage() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val addComentRequest = AddComentRequest()
        addComentRequest.content = binding.txtCommentSend.text.toString()

        val call: Call<AddCommentResponse?> =
            APIClient.client.create(APIInterface::class.java)
                .addForumComment(token, arguments?.getString(ID, "").toString(), addComentRequest)
        showProgressBar()
        call.enqueue(object : Callback<AddCommentResponse?> {
            override fun onResponse(
                call: Call<AddCommentResponse?>,
                response: Response<AddCommentResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null)
                        handleCommentResponse(response.body()!!)
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<AddCommentResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun likeMessage() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<LikeForumResponse?> =
            APIClient.client.create(APIInterface::class.java)
                .toggleLike(token, arguments?.getString(ID, "").toString())
        showProgressBar()
        call.enqueue(object : Callback<LikeForumResponse?> {
            override fun onResponse(
                call: Call<LikeForumResponse?>,
                response: Response<LikeForumResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.isLiked == 1) {
                        binding.textLikes.setCompoundDrawablesWithIntrinsicBounds(requireContext().getDrawable(
                            R.drawable.ic_like_selected), null, null, null)
                    } else {
                        binding.textLikes.setCompoundDrawablesWithIntrinsicBounds(requireContext().getDrawable(
                            R.drawable.ic_like), null, null, null)
                    }
                    binding.textLikes.setText("${response.body()?.likes} Likes")
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<LikeForumResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun handleCommentResponse(commentResponse: AddCommentResponse) {
        binding.txtCommentSend.setText("")
        var comments: GetAllCommentsResponse? =  _liveDataTwo.value
       (comments?.comments as ArrayList).add(0, commentResponse.comment)
        comments?.count = commentResponse.forum?.comments
      //  comments?.let { _liveDataTwo.postValue(comments!!)}
        binding.replyList.adapter = CommentsForumAdapter(comments?.comments)
        binding.replyList.adapter?.notifyDataSetChanged()
        binding.textComments.setText("${commentResponse.forum?.comments} Comments")
        showToast(requireContext().getString(R.string.comment_sent))
    }

    private fun getData() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"

        val call: Call<ForumDetailResponse?> =
            APIClient.client.create(APIInterface::class.java)
                .getForumDetail(token, arguments?.getString(ID, "").toString())
       // showProgressBar()
        call.enqueue(object : Callback<ForumDetailResponse?> {
            override fun onResponse(
                call: Call<ForumDetailResponse?>,
                response: Response<ForumDetailResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _liveDataOne?.postValue(response.body()!!)
                    }
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                    hideProgressBar()
                }

            }

            override fun onFailure(call: Call<ForumDetailResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun handleResponse(body: ForumDetailResponse) {
        body.forum?.apply {
            binding.forumDescription.setText(description)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.forumDescription.setText(fromHtml(description, Html.FROM_HTML_MODE_COMPACT))
            } else {
                binding.forumDescription.setText(fromHtml(description))
            }
            binding.textLikes.setText("${likes} Likes")
            binding.textComments.setText("${comments} Comments")
            if (isLiked == 1) {
                binding.textLikes.setCompoundDrawables(requireContext().getDrawable(R.drawable.ic_like_selected),
                    null,
                    null,
                    null)
            } else {
                binding.textLikes.setCompoundDrawables(requireContext().getDrawable(R.drawable.ic_like),
                    null,
                    null,
                    null)
            }
            if (isCreatedByAdmin ?: true) {
                binding.userName.setText(requireContext().getString(R.string.appname))
                binding.imgUser.setImageResource(R.mipmap.ic_launcher_round)
            } else {
                binding.userName.setText(createdBy?.name?.full)
            }
        }
    }

    private fun handleAllCommentsResponse(body:GetAllCommentsResponse){
        binding.replyList.layoutManager =  LinearLayoutManager(requireContext())
        binding.replyList.adapter = CommentsForumAdapter(body.comments)
    }

}