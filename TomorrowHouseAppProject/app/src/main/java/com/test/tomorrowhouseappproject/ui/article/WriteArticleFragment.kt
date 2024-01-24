package com.test.tomorrowhouseappproject.ui.article

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.test.tomorrowhouseappproject.R
import com.test.tomorrowhouseappproject.databinding.FragmentWriteArticleBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleModel
import java.util.UUID

class WriteArticleFragment : Fragment() {
    private lateinit var fragmentWriteArticleBinding: FragmentWriteArticleBinding
    private var selectedUri: Uri? = null

    //사진이 선택되었을 때의 콜백 변수
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                selectedUri = uri
                fragmentWriteArticleBinding.photoImageView.setImageURI(uri)
                fragmentWriteArticleBinding.addPhotoButton.visibility = View.GONE
                fragmentWriteArticleBinding.clearPhotoButton.visibility = View.VISIBLE
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentWriteArticleBinding = FragmentWriteArticleBinding.inflate(layoutInflater)

        fragmentWriteArticleBinding.topAppBar.inflateMenu(R.menu.toolbar_menu)

        //툴바 메뉴 색상 변경
        fragmentWriteArticleBinding.topAppBar.menu.getItem(0).icon?.setTint(
            resources.getColor(
                R.color.teal_200,
                resources.newTheme()
            )
        )

        //툴바의 뒤로가기를 클릭했을 경우
        setUpBackButton()

        //툴바의 업로드 버튼을 클릭했읋 경우
        setUpUploadButton()

        //이미지를 클릭했을 경우
        setUpPhotoImageView()

        //이미지 삭제를 클릭했을 경우
        setUpClearImageView()

        return fragmentWriteArticleBinding.root

    }

    private fun setUpBackButton() {
        fragmentWriteArticleBinding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(WriteArticleFragmentDirections.actionBack())
        }
    }

    private fun setUpUploadButton() {
        fragmentWriteArticleBinding.topAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.uploadButton && selectedUri != null) {
                showProgressBar()
                val uri: Uri = selectedUri ?: return@setOnMenuItemClickListener true
                uploadImage(
                    uri = uri,
                    successHandler = {
                        val stringUri = it
                        val description = fragmentWriteArticleBinding.descriptionEditText.text.toString()
                        //파이어스토어에 업로드하는 작업
                        uploadArticle(stringUri, description)
                        fragmentWriteArticleBinding.addPhotoButton.visibility = View.GONE
                    },
                    errorHandler = {
                        hideProgressBar()
                        Snackbar.make(
                            fragmentWriteArticleBinding.root,
                            "uri 저장 및 불러오기 실패!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    })
                true // 클릭 이벤트를 소비함
            } else {
                Snackbar.make(
                    fragmentWriteArticleBinding.root,
                    "이미지를 업로드 해야합니다!!",
                    Snackbar.LENGTH_SHORT
                ).show()
                hideProgressBar()
                false // 클릭 이벤트를 계속 전파함
            }
        }
    }

    private fun showProgressBar(){
        fragmentWriteArticleBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        fragmentWriteArticleBinding.progressBarLayout.visibility = View.GONE
    }


    private fun setUpClearImageView() {
        fragmentWriteArticleBinding.clearPhotoButton.setOnClickListener {
            fragmentWriteArticleBinding.photoImageView.setImageURI(null)
            selectedUri = null
            fragmentWriteArticleBinding.clearPhotoButton.visibility = View.GONE
            fragmentWriteArticleBinding.addPhotoButton.visibility = View.VISIBLE
        }
    }

    private fun setUpPhotoImageView() {
        fragmentWriteArticleBinding.photoImageView.setOnClickListener {
            if (selectedUri == null) {
                startPicker()
            }
        }
    }

    private fun startPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadImage(
        uri: Uri,
        successHandler: (String) -> Unit,
        errorHandler: (Throwable?) -> Unit,
    ) {
        //TODO 사진과 내용을 업로드해야 함
        val fileName = "${UUID.randomUUID()}.png"
        Firebase.storage.reference.child("articles/photo").child(fileName).putFile(uri)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Firebase.storage.reference.child("articles/photo/$fileName").downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler(it)
                        }
                } else {
                    // error Handler
                    errorHandler(task.exception)
                }
            }
    }

    private fun uploadArticle(photoUrl: String, description:String) {
        val articleId = UUID.randomUUID().toString()
        val articleModel = ArticleModel(
            articleId = articleId,
            createAt = System.currentTimeMillis(),
            description = description,
            imageUrl = photoUrl
            )

        Firebase.firestore.collection("articles").document(articleId)
            .set(articleModel)
            .addOnSuccessListener {
                findNavController().navigate(WriteArticleFragmentDirections.actionWriteArticleFragmentToHomeFragment())
                hideProgressBar()
            }.addOnFailureListener{
                hideProgressBar()
                Snackbar.make(fragmentWriteArticleBinding.root,"글 업로드 실패!",Snackbar.LENGTH_SHORT).show()
            }
    }
}
