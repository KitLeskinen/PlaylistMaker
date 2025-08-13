package com.practicum.playlistmaker.medialibrary.ui


import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


open class NewPlaylistFragment : Fragment() {

    private var isPlaylistCoverChanged = false

    private var coverUri: String? = null

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }

    protected open val viewModel by viewModel<NewPlaylistViewModel>()

    protected var _binding: FragmentNewPlaylistBinding? = null
    protected val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    open fun load(){
        enableButton(false)
    }

    private fun checkIsCoverOrContentFilled(): Boolean {
        return isPlaylistCoverChanged
                || binding.playlistNameEditText.text.isNotEmpty()
                || binding.playlistDescriptionEditText.text.isNotEmpty()
    }

    protected open fun backAction() {
        if (checkIsCoverOrContentFilled()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setPositiveButton("Завершить") { _, _ ->
                    findNavController().popBackStack()
                }
                .setNegativeButton("Отмена") { _, _ ->
                }.show()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                NewPlayListState.EmptyFields -> enableButton(false)
                NewPlayListState.FilledFields -> enableButton(true)
                is NewPlayListState.Loading -> load()
                is NewPlayListState.CoverFilled -> fillCover(state.uriString)
                is NewPlayListState.FillingViews -> fillViews(state.coverUri, state.name, state.description)
            }

        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.textChanged(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        binding.playlistNameEditText.addTextChangedListener(textWatcher)


        val playlistArt =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addPlayListCoverButton.setImageURI(uri)
                    binding.addPlayListCoverButton.scaleType = ImageView.ScaleType.CENTER_CROP
                    isPlaylistCoverChanged = true
                    viewModel.saveCoverImage(uri.toString())
                }
            }


        binding.addPlayListCoverButton.setOnClickListener {
            playlistArt.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }




        binding.backImageView.setNavigationOnClickListener() {
            backAction()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backAction()
        }

        binding.createPlaylistButton.setOnClickListener {

            viewModel.savePlaylist(createPlaylist())
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.playlistNameEditText.text} создан",
                Toast.LENGTH_LONG
            ).show()
            findNavController().popBackStack()
        }

    }

    open fun createPlaylist() : Playlist{
        val playlist = Playlist(
            id = 0,
            name = binding.playlistNameEditText.text.toString(),
            description = binding.playlistDescriptionEditText.text.toString(),
            coverUri = coverUri,
            trackList = null
        )
        return playlist
    }

    open fun fillViews(coverUri: String?, name: String, description: String) {

    }

    private fun fillCover(uriString: String) {
        coverUri = uriString
        binding.addPlayListCoverButton.setImageURI(Uri.parse(uriString))
    }


    private fun enableButton(isEnabled: Boolean) {
        binding.createPlaylistButton.isEnabled = isEnabled
    }


}