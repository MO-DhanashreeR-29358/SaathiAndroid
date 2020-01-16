package com.mosl.tabapp.birthdayanniversary;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mosl.app.core.data.MOSLResponseWrapper;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryData;
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryItem;
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryRequest;
import com.mosl.saathi.api.model.birthdayanniversary.Filter;
import com.mosl.saathi.api.model.birthdayanniversary.FiltersItem;
import com.mosl.saathi.api.model.birthdayanniversary.Request;
import com.mosl.saathi.api.model.birthdayanniversary.SendEmailRequest;
import com.mosl.saathi.api.model.birthdayanniversary.SendGiftRequest;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.BirthdayAnniversaryModel;
import com.mosl.tabapp.ui.MSFDialog;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.constants.MOSLConstants.EMAIL_ALL_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.EMAIL_MESSAGE_CLIENT;
import static com.mosl.tabapp.constants.MOSLConstants.GIFT_SEND;
import static com.mosl.tabapp.constants.MOSLConstants.LEADPROFILE;
import static com.mosl.tabapp.constants.MOSLConstants.REPORT_PAGE_SIZE;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vijay on 08/03/2017.
 */

public class BirthdayAnniversaryFragment extends CommonFragment
    implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    public static String BELATED_FLAG = "belated";
    public static String TODAY_FLAG = "today";
    public static String UPCOMING_FLAG = "upcoming";
    //    public static String UPCOMING_FLAG = "1";
    //    public static String UPCOMING_FLAG = "2";
    //    public static String UPCOMING_FLAG = "3";

    private View birthdayAnniversaryView;
    private RecyclerView rvBirthdayAnniversary;
    private Spinner spProfile, spEventType;
    private ArrayList<BirthdayAnniversaryModel> birthdayAnniversaryList;
    private BirthdayAnniversaryAdapter birthdayAnniversaryAdapter;
    private ArrayList<RowData> arrProfile;
    private ArrayList<String> arrEventType;
    private ArrayAdapter<RowData> arrAdapProfile;
    private ArrayAdapter<String> arrAdapEventType;
    private String profileCodeStr, profileNameStr = "";
    private TextView tvNoData;
    private ImageView ivEmailAll;
    boolean dataFoundFlag;
    private SegmentedGroup segmentedGroup;
    private String filterType = "2", dayType = TODAY_FLAG;

    private AdapterView.OnItemSelectedListener profileInListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    profileCodeStr = ((RowData) spProfile.getSelectedItem()).getKey();
                    profileNameStr = ((RowData) spProfile.getSelectedItem()).getValue();
                    getData();
                /* if (profileCodeStr.equalsIgnoreCase("-1")) {
                    getData("", spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A");
                } else {
                    getData(profileNameStr, spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A");
                }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener eventTypeInListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (profileCodeStr != null) {
                        getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        birthdayAnniversaryView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(birthdayAnniversaryView);
		container.post(() -> setAppTitle("Birthday & Anniversary"));
        setUserVisibleHint(true);
        return birthdayAnniversaryView;
    }

    @Override protected int getLayout() {
        return R.layout.frag_birthday_anniversary;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && birthdayAnniversaryView != null) {
                getProfile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    private void setUpView(View birthdayAnniversaryView) {
        spProfile = (Spinner) birthdayAnniversaryView.findViewById(R.id.spProfile);
        spEventType = (Spinner) birthdayAnniversaryView.findViewById(R.id.spEventType);
        segmentedGroup = (SegmentedGroup) birthdayAnniversaryView.findViewById(R.id.segmentedGroup);
        spEventType.setOnItemSelectedListener(eventTypeInListener);
        rvBirthdayAnniversary =
            (RecyclerView) birthdayAnniversaryView.findViewById(R.id.rvBirthdayAnniversary);
        tvNoData = (TextView) birthdayAnniversaryView.findViewById(R.id.tvNoData);
        ivEmailAll = (ImageView) birthdayAnniversaryView.findViewById(R.id.ivEmailAll);
        String[] eventTypeArray =
            getActivity().getResources().getStringArray(R.array.BirthdayAniversary);
        arrEventType = new ArrayList<String>(Arrays.asList(eventTypeArray));
        arrAdapEventType =
            new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrEventType);
        arrAdapEventType.setDropDownViewResource(R.layout.mdropdown);
        spEventType.setAdapter(arrAdapEventType);

        ivEmailAll.setOnClickListener(this);
        segmentedGroup.setOnCheckedChangeListener(this);
        //        OverScrollDecoratorHelper.setUpOverScroll(rvBirthdayAnniversary, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    private void renderAllLookUp(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrProfile = new ArrayList<>();
            arrProfile.add(new RowData(0, "-1", "All"));

            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrProfile.add(new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapProfile =
                new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrProfile);
            arrAdapProfile.setDropDownViewResource(R.layout.mdropdown);
            spProfile.setAdapter(arrAdapProfile);
            spProfile.setOnItemSelectedListener(profileInListener);
        }
    }

    private void getProfile() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), LEADPROFILE,
					null, null);
            Disposable subscribe = getSaathiApi().getAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderAllLookUp);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderBirthdayAnniversary(UiModel<BirthdayAnniversaryItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;

        if (error != null) {
            birthdayAnniversaryAdapter =
                new BirthdayAnniversaryAdapter(birthdayAnniversaryList);
            rvBirthdayAnniversary.setLayoutManager(
                new GridLayoutManager(requireContext(), 3));
            rvBirthdayAnniversary.setAdapter(
                birthdayAnniversaryAdapter);
            birthdayAnniversaryAdapter.notifyDataSetChanged();
            dataFoundFlag = false;
            tvNoData.setVisibility(View.VISIBLE);
            final String eventType =
                spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A";
            setNoDataText(eventType);
            showOnFailurePopup(error);
        }

        BirthdayAnniversaryItem item = uiModel.data;
        if (item != null) {
            List<BirthdayAnniversaryData> birthdayAnniversaryDataList = item.data;
            final String eventType =
                spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A";
            if (birthdayAnniversaryDataList.size() != 0) {
                for (int i = 0; i < birthdayAnniversaryDataList.size(); i++) {
                    BirthdayAnniversaryData data = birthdayAnniversaryDataList.get(i);
                    String clientCode = stringOrZero(data.clientCode);
                    String clientType = stringOrEmpty(data.clientType);
                    String ucid = stringOrZero(data.ucid);
                    String clientName = stringOrEmpty(data.clientName);
                    String dob = stringOrEmpty(data.dob);
                    String aniversaryDate = stringOrEmpty(data.anniversaryDate);
                    String noOfYearBirthday = stringOrEmpty(data.noOfYearBirthday);
                    String mobile = stringOrEmpty(data.backOfficeMobileNumber);
                    String email = stringOrEmpty(data.email);
                    String birthdayCount = stringOrEmpty(data.birthdayCount);
                    String aniversaryCount = stringOrEmpty(data.anniversaryCount);
                    String clientPotential = stringOrEmpty(data.clientPotential);
                    boolean birthdayFlag = data.birthdayFlag;
                    boolean anniversoryFlag = data.anniversaryFlag;

                    birthdayAnniversaryList.add(
                        new BirthdayAnniversaryModel(clientName,
                            clientCode, ucid,
                            eventType.equals("B") ? dob
                                : aniversaryDate, email, mobile,
                            eventType.equals("B") ? noOfYearBirthday
                                : aniversaryCount,
                            eventType.equals("B") ? "Birthday"
                                : "Anniversary",
                            eventType.equals("B") ? birthdayFlag
                                : anniversoryFlag, eventType));
                }
                tvNoData.setVisibility(View.GONE);
                birthdayAnniversaryAdapter =
                    new BirthdayAnniversaryAdapter(
                        birthdayAnniversaryList);
                rvBirthdayAnniversary.setLayoutManager(
                    new GridLayoutManager(requireContext(), 3));
                rvBirthdayAnniversary.setAdapter(
                    birthdayAnniversaryAdapter);
                birthdayAnniversaryAdapter.notifyDataSetChanged();
                dataFoundFlag = true;
            } else {
                birthdayAnniversaryAdapter =
                    new BirthdayAnniversaryAdapter(
                        birthdayAnniversaryList);
                rvBirthdayAnniversary.setLayoutManager(
                    new GridLayoutManager(requireContext(), 3));
                rvBirthdayAnniversary.setAdapter(
                    birthdayAnniversaryAdapter);
                birthdayAnniversaryAdapter.notifyDataSetChanged();
                dataFoundFlag = false;
                tvNoData.setVisibility(View.VISIBLE);
                setNoDataText(eventType);
            }
        }
    }

    private void setNoDataText(String eventType) {
        if (eventType.equalsIgnoreCase("B")) {
            tvNoData.setText(
                "No clients having Birthday " + dayType);
        } else {
            tvNoData.setText(
                "No clients having Anniversary " + dayType);
        }
    }

    private void getData() {
        String profileId;
        birthdayAnniversaryList = new ArrayList<>();
        birthdayAnniversaryAdapter = new BirthdayAnniversaryAdapter(birthdayAnniversaryList);
        rvBirthdayAnniversary.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvBirthdayAnniversary.setAdapter(birthdayAnniversaryAdapter);
        birthdayAnniversaryAdapter.notifyDataSetChanged();
        if (profileCodeStr.equalsIgnoreCase("-1")) {
            profileId = "";
        } else {
            profileId = profileNameStr;
        }
        final String eventType =
            spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A";

        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            FiltersItem filtersItem = new FiltersItem();
            ArrayList<String> valueList = new ArrayList<>();
            valueList.add(profileId);
            filtersItem.value = valueList;
            List<FiltersItem> filters = new ArrayList<FiltersItem>();

            filters.add(filtersItem);
            Filter filter = new Filter(filters, "and");
            String dayType = filterType;
            String page = "1";
            String calculatedPage = "0";
            String totalCount = "0";
            Request birthdayRequest =
                new Request(dayType, totalCount, REPORT_PAGE_SIZE, eventType, calculatedPage, page);

            BirthdayAnniversaryRequest request =
                new BirthdayAnniversaryRequest(filter, birthdayRequest, true);
            Disposable subscribe = getSaathiApi().postBirthdayAnniversary(request)
                .compose(applyUiModel())
                .subscribe(this::renderBirthdayAnniversary);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderSendGift(UiModel<String> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        String data = uiModel.data;
        if (data != null) {
            alertDialog(requireContext(), 0,
                getResources().getString(R.string.motilal_oswal), GIFT_SEND, "Ok", true, null);
        }
    }

    private String getErrorMessage(SaathiCommonResponse<?> response) {
        if (response.errors.isEmpty()) {
            return MOSLResponseWrapper.SERVER_ERROR_OCCURRED;
        } else {
            return response.errors.get(0).message;
        }
    }

    private void sendGift(String ucid, String eventType) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            SendGiftRequest request = new SendGiftRequest(ucid, eventType);
            Disposable subscribe = getSaathiApi().postSendGift(request)
                .compose(applyUiModel())
                .subscribe(this::renderSendGift);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderSendEmail(UiModel<String> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        String data = uiModel.data;
        if (data != null) {
            alertDialog(requireContext(), 0,
                getResources().getString(R.string.motilal_oswal), MOSLConstants.EMAIL_SEND, "Ok",
                true, null);
        }
    }

    private void sendEmail(String ucid, String eventType) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            SendEmailRequest request =
                new SendEmailRequest(ucid, false, eventType + "-T", "E", null, null);
            Disposable subscribe = getSaathiApi().postSendEmail(request)
                .compose(applyUiModel())
                .subscribe(this::renderSendEmail);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void sendEmailAll() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            String eventType =
                (spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A")
                    + (dayType.equalsIgnoreCase(BELATED_FLAG) ? "-L" : "-T");
            SendEmailRequest request =
				new SendEmailRequest(null, true, eventType, "E", Objects.requireNonNull(
					userManager.getUser()).employeeCode,
                    filterType);
            Disposable subscribe = getSaathiApi().postSendEmail(request)
                .compose(applyUiModel())
                .subscribe(this::renderSendEmail);
        } else {
            noInternetConnectionDialog();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivEmailAll:
                if (dataFoundFlag) {
                    if (!filterType.equalsIgnoreCase("3")) {
                        alertDialog(requireContext(), 0,
                            getResources().getString(R.string.motilal_oswal), EMAIL_ALL_MESSAGE,
                            "Yes", "No", true, new MSFDialog.DialogListener() {
                                @Override
                                public void alertDialogAction(MSFDialog.Action action,
                                    Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        sendEmailAll();
                                    }
                                }
                            });
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btnBelated:
                filterType = "1";
                dayType = "belated";
                getData();
                // ivEmailAll.setVisibility(View.GONE);
                CRMAppUtil.collapse(ivEmailAll);
                break;
            case R.id.btnToday:
                filterType = "2";
                dayType = "today";
                getData();
                //ivEmailAll.setVisibility(View.VISIBLE);
                CRMAppUtil.expand(ivEmailAll);
                break;
            case R.id.btnUpcomming:
                filterType = "3";
                dayType = "upcoming";
                getData();
                //ivEmailAll.setVisibility(View.GONE);
                CRMAppUtil.collapse(ivEmailAll);
                break;
            default:
                // Nothing to do
        }
    }

    public class BirthdayAnniversaryAdapter
        extends RecyclerView.Adapter<BirthdayAnniversaryAdapter.MyViewHolder> {

        private List<BirthdayAnniversaryModel> birthdayAnniversaryList;

        public BirthdayAnniversaryAdapter(List<BirthdayAnniversaryModel> birthdayAnniversaryList) {
            this.birthdayAnniversaryList = birthdayAnniversaryList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_birthday_anniversary, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final BirthdayAnniversaryModel birthdayAnniversaryModel =
                birthdayAnniversaryList.get(position);
            String[] birthDate = birthdayAnniversaryModel.getBirthday().split("T");
            Date dateformat = CRMAppUtil.getDate(birthDate[0], "yyyy-MM-dd");
            String date = CRMAppUtil.getDateStr(dateformat, "dd MMM yyyy");
            holder.tvClientName.setText(birthdayAnniversaryModel.getClientName());
            if (birthdayAnniversaryModel.getClientCode().equalsIgnoreCase("0") || isBlank(
                birthdayAnniversaryModel.getClientCode())) {
                holder.tvClientCode.setText(birthdayAnniversaryModel.getUcid());
            } else {
                holder.tvClientCode.setText(birthdayAnniversaryModel.getClientCode());
            }
            holder.tvDate.setText(date);
            //            holder.tvDate.setText(birthdayAnniversaryModel.getBirthday());
            holder.tvEvent.setText(
                birthdayAnniversaryModel.getTotalYears()
                    + CRMAppUtil.getDayOfMonthSuffix(
                    Integer.parseInt(birthdayAnniversaryModel.getTotalYears()))
                    + birthdayAnniversaryModel.getTitle());
            if (birthdayAnniversaryModel.getTitle().equals("Birthday")) {
                holder.ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_bday);
            } else {
                holder.ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_anniversary);
            }

            //dont open it for belated
            if (!filterType.equalsIgnoreCase("1")) {
                holder.ivPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(requireContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow()
                            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.dialog_birthday_anniversary);
                        dialog.getWindow()
                            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCanceledOnTouchOutside(false);

                        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
                        ImageView ivCall = (ImageView) dialog.findViewById(R.id.ivCall);
                        ImageView ivSms = (ImageView) dialog.findViewById(R.id.ivSms);
                        ImageView ivEmail = (ImageView) dialog.findViewById(R.id.ivEmail);
                        ImageView ivSendGift = (ImageView) dialog.findViewById(R.id.ivSendGift);

                        ivCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!filterType.equalsIgnoreCase("3")) {
                                    alertDialog(requireContext(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        "Do you want to call?", "Yes", "No", true,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    if (birthdayAnniversaryModel.getContactNo()
                                                        .equalsIgnoreCase("")) {
                                                        Toast.makeText(requireContext(),
                                                            "Contact number not available",
                                                            Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Intent callIntent =
                                                            new Intent(Intent.ACTION_DIAL);
                                                        callIntent.setData(Uri.parse("tel:"
                                                            + birthdayAnniversaryModel.getContactNo()));
                                                        callIntent.setFlags(
                                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        requireContext().startActivity(callIntent);
                                                    }
                                                }
                                            }
                                        });
                                    dialog.dismiss();
                                }
                            }
                        });
                        ivSms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!filterType.equalsIgnoreCase("3")) {
                                    if (!(birthdayAnniversaryModel.getContactNo()
                                        .equals("android.intent.action.VIEW"))) {
                                        Intent smsIntent = new Intent("");
                                        smsIntent.setData(Uri.parse("smsto:" + Uri.encode(
                                            birthdayAnniversaryModel.getContactNo())));
                                        smsIntent.putExtra("sms_body",
                                            birthdayAnniversaryModel.getTitle()
                                                .equalsIgnoreCase("birthday")
                                                ? MOSLConstants.BIRTHDAY_MESSAGE
                                                : MOSLConstants.ANNIVERSARY_MESSAGE);
                                        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        requireContext().startActivity(smsIntent);
                                    } else {
                                        Toast.makeText(requireContext(),
                                            "Contact number not available", Toast.LENGTH_LONG)
                                            .show();
                                    }
                                    dialog.dismiss();
                                }
                            }
                        });
                        ivEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!filterType.equalsIgnoreCase("3")) {
                                    alertDialog(requireContext(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        EMAIL_MESSAGE_CLIENT, "Yes", "No", true,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    sendEmail(birthdayAnniversaryModel.getUcid(),
                                                        birthdayAnniversaryModel.getEventType());
                                                }
                                            }
                                        });
                                    dialog.dismiss();
                                }
                            }
                        });
                        ivSendGift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!birthdayAnniversaryModel.isGiftFlag()) {
                                    sendGift(birthdayAnniversaryModel.getUcid(),
                                        birthdayAnniversaryModel.getEventType());
                                }
                                dialog.dismiss();
                            }
                        });
                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            }//iff end
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            //            private MSFPopupWindow menuPopupWindow;
            private TextView tvClientName, tvClientCode, tvDate, tvEvent;
            private ImageView ivPopup, ivBirthdayAnniversary;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvClientName = (TextView) itemView.findViewById(R.id.tvClientName);
                tvClientCode = (TextView) itemView.findViewById(R.id.tvClientCode);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);
                tvEvent = (TextView) itemView.findViewById(R.id.tvEvent);
                ivBirthdayAnniversary =
                    (ImageView) itemView.findViewById(R.id.ivBirthdayAnniversary);
                ivPopup = (ImageView) itemView.findViewById(R.id.ivPopup);
                //                final BirthdayAnniversaryModel model = data.get(position);

            }
        }

        @Override
        public int getItemCount() {
            return birthdayAnniversaryList.size();
        }
    }
}
