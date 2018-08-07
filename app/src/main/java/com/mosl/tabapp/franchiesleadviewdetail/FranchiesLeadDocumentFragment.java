package com.mosl.tabapp.franchiesleadviewdetail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.model.FranchiesLeadViewDocumentModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.mosl.tabapp.common.CRMMOSLCommon.setPermisson;

/**
 * Created by Vijay on 20/03/2017.
 */

public class FranchiesLeadDocumentFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View franchiesLeadDocumentView;
    private TextView tvDocuments, tvAddDoc, tvCancel, tvTakePhoto, tvAddFromDevice;
    private LinearLayout llCancel, llOptions;
    private Dialog dialog;
    private RelativeLayout rlAddDoc;
    private Uri imageToUploadUri;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private String currentSelectedDocForUpload;
    private RecyclerView rvLeadDocument;
    private ArrayList<FranchiesLeadViewDocumentModel> franchiesLeadDocumentList;
    private FranchiesLeadDocumentAdapter franchiesLeadDocumentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        franchiesLeadDocumentView = super.onCreateView(inflater, container, savedInstanceState);
        setPermisson(getActivity(), getContext());
        attachLayout(R.layout.frag_franchies_lead_document);
        setAppTitle(getClass().toString(), "Documents");
        setUpView(franchiesLeadDocumentView);
        return franchiesLeadDocumentView;
    }

    private void setUpView(View franchiesLeadDocumentView) {
        rvLeadDocument = (RecyclerView) franchiesLeadDocumentView.findViewById(R.id.rvLeadDocument);
        tvAddDoc = (TextView) franchiesLeadDocumentView.findViewById(R.id.tvAddDoc);
        tvDocuments = (TextView) franchiesLeadDocumentView.findViewById(R.id.tvDocuments);
        rlAddDoc = (RelativeLayout) franchiesLeadDocumentView.findViewById(R.id.rlAddDoc);
        tvAddDoc.setText("Add a Document");
        tvDocuments.setText("You don't have any documents");
        rlAddDoc.setOnClickListener(this);
        franchiesLeadDocumentList = new ArrayList<>();
    }

    private void dialogAddDocument() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_document);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(true);

        tvTakePhoto = (TextView) dialog.findViewById(R.id.tvTakePhoto);
        tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        tvAddFromDevice = (TextView) dialog.findViewById(R.id.tvAddFromDevice);

        tvCancel.setText("Cancel");
        tvTakePhoto.setText("Take photo");
        tvAddFromDevice.setText("Add From Device");

        llCancel = (LinearLayout) dialog.findViewById(R.id.llCancel);
        llOptions = (LinearLayout) dialog.findViewById(R.id.llOptions);

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cameraIntent();
            }
        });

        tvAddFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                galleryIntent();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cameraIntent() {

        imageToUploadUri = null;
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getMainActivity().getContentResolver(), data.getData());
                uploadFile(bm, currentSelectedDocForUpload, SELECT_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void onCaptureImageResult(Bitmap bitmap) {
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        uploadFile(bitmap, currentSelectedDocForUpload, REQUEST_CAMERA);
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getMainActivity().getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getMainActivity().getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    /////////////////// UPLOAD DOCUMENT \\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void uploadFile(Bitmap bmp, String docName, int imageSource) {
        String data = null;
        try {
            //Bitmap bmp = BitmapFactory.decodeFile(sourceFileUri);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            InputStream fileInputStream = new ByteArrayInputStream(bos.toByteArray());

            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }

            output64.close();

            data = output.toString();

            fileInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        if (franchiesLeadDocumentList.isEmpty()) {
            franchiesLeadDocumentAdapter = new FranchiesLeadDocumentAdapter(franchiesLeadDocumentList);
        }
        franchiesLeadDocumentList.add(new FranchiesLeadViewDocumentModel(imageToUploadUri));
        rvLeadDocument.setLayoutManager(new GridLayoutManager(getMainActivity(), 2));
        rvLeadDocument.setAdapter(franchiesLeadDocumentAdapter);
        franchiesLeadDocumentAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                if (imageToUploadUri != null) {
                    Uri selectedImage = imageToUploadUri;
                    getMainActivity().getContentResolver().notifyChange(selectedImage, null);
                    Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                    onCaptureImageResult(reducedSizeBitmap);
                }

            } else if (requestCode == SELECT_FILE) {
                imageToUploadUri = data.getData();
                onSelectFromGalleryResult(data);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlAddDoc:
                dialogAddDocument();
                break;
        }
    }

    public class FranchiesLeadDocumentAdapter extends RecyclerView.Adapter<FranchiesLeadDocumentAdapter.MyViewHolder> {
        private List<FranchiesLeadViewDocumentModel> franchiesLeadDocumentList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private ImageView ivDocument;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivDocument = (ImageView) itemView.findViewById(R.id.ivDocument);
            }
        }

        public FranchiesLeadDocumentAdapter(List<FranchiesLeadViewDocumentModel> franchiesLeadDocumentList) {
            this.franchiesLeadDocumentList = franchiesLeadDocumentList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recycler_lead_document, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final FranchiesLeadViewDocumentModel leadViewDocumentModel = franchiesLeadDocumentList.get(position);
            holder.ivDocument.setImageURI(leadViewDocumentModel.getFileUploadedPath());
        }

        @Override
        public int getItemCount() {
            return franchiesLeadDocumentList.size();
        }
    }
}
