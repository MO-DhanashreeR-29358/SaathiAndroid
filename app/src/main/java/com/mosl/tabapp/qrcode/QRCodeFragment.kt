package com.mosl.tabapp.qrcode


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentQrCodeBinding
import com.mosl.tabapp.util.downloadImage
import com.squareup.picasso.Picasso
import javax.inject.Inject


class QRCodeFragment : CommonFragment(), QRCodeView {

    lateinit var binding : FragmentQrCodeBinding

    @Inject lateinit var presenter: QRCodePresenter

    lateinit var strUrl : String
    override fun getLayout(): Int {
        return R.layout.fragment_qr_code
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQrCodeBinding.bind(view)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        binding.txtMessage.text = getString(R.string.qr_code_message)

        binding.btnCreate.setOnClickListener(View.OnClickListener {
            val strMessage = binding.txtMessage.text.toString()
            if(strUrl.isNotEmpty() && !strMessage.contains("Error")
                && !strMessage.equals(getString(R.string.qr_code_message))) {
                requireContext().downloadImage(strUrl.replace("\"","").trim())
                Toast.makeText(
                    requireContext(),
                    "Downloading ${strUrl}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    override fun renderGetQRCode(uiModel: UiModel<String>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
             binding.txtMessage.isVisible = true
             binding.imgQRCode.isVisible = false
             binding.txtMessage.text = it.message
        }
        uiModel.onSuccess { imageUrl ->
            strUrl = imageUrl
            val glideCallback: RequestListener<String, GlideDrawable> =
                object : RequestListener<String, GlideDrawable> {
                    override fun onException(
                        e: java.lang.Exception,
                        model: String?,
                        target: Target<GlideDrawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.txtMessage.isVisible = true
                        binding.imgQRCode.isVisible = false
                        binding.txtMessage.text = "Error : " + e.message.toString()
                        e.printStackTrace()
                        return false
                    }

                    override fun onResourceReady(
                        resource: GlideDrawable?,
                        model: String?,
                        target: Target<GlideDrawable?>,
                        isFromMemoryCache: Boolean,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.txtMessage.isVisible = false
                        binding.imgQRCode.isVisible = true
                        binding.txtMessage.text = ""
                        return false
                    }
                }

            try {
                Glide.with(binding.imgQRCode.context)
                    .load(imageUrl.replace("\"","").trim())
                    .error(R.drawable.ic_closed)
                    .listener(glideCallback)
                    .into(binding.imgQRCode)
            }catch (e: Exception){
                Log.d("QRCode : Exception", e.message.toString())
            }

        }
    }
}