package bonch.dev.kotlinnetwork.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import bonch.dev.kotlinnetwork.R

class PostCreateDialogFragment : DialogFragment() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextBody: EditText
    private lateinit var createBtn: Button

    var btnCreateListener: OnDialogButtonClick? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_created, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        btnCreateListener = context as OnDialogButtonClick
    }

    private fun setListeners() {
        createBtn.setOnClickListener {

            if (!editTextBody.text.toString().startsWith(" ")
                && !editTextBody.text.toString().endsWith(" ")
                && !editTextTitle.text.toString().startsWith(" ")
                && !editTextTitle.text.toString().endsWith(" ")
                && !editTextTitle.text.toString().equals("")
                && !editTextBody.text.toString().equals("")
            ) {
                btnCreateListener!!.onDialogClickListener(
                    editTextTitle.text.toString(),
                    editTextBody.text.toString()
                )
                dismiss()
            }

        }
    }

    private fun initViews(view: View) {
        editTextTitle = view.findViewById(R.id.edit_title_post)
        editTextBody = view.findViewById(R.id.edit_body_post)
        createBtn = view.findViewById(R.id.btn_post_create)
    }


}

interface OnDialogButtonClick {
    fun onDialogClickListener(postTitle: String, postBody: String)
}