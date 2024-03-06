package com.mosl.tabapp.attendancedialog.verifyotp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.mosl.tabapp.attendancedialog.verifyotp.VerifyContract.OTP_RESENT;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.mosl.app.core.error.ErrorUtils;
import com.mosl.app.core.events.DataAction;
import com.mosl.commons.data.UiModel;
import com.mosl.core.annotations.DepartmentIds;
import com.mosl.tabapp.R;
import com.mosl.tabapp.attendancedialog.AttendanceHelper;
import com.mosl.tabapp.core.CommonActivity;
import com.mosl.tabapp.dashboard.base.DashboardFragment;
import com.mosl.tabapp.databinding.FragmentVerifyOtpBinding;
import com.mosl.user.domain.model.GenerateOTP;
import com.mosl.user.domain.model.User;
import com.mosl.user.login.cache.UserCacheImpl;
import com.mosl.user.prefs.StringPreference;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import kotlin.Unit;
import timber.log.Timber;

public class VerifyDialogFragment extends DialogFragment implements VerifyContract.View {
    public static String ARG_OTP_DATA = "OtpData";
    private final SMSReceiver smsReceiver = new SMSReceiver();
    @Inject
    VerifyPresenter presenter;
    @Inject @DepartmentIds
    StringPreference deptIds;
    private FragmentVerifyOtpBinding binding;
    @Inject Gson gson;
    @Inject UserCacheImpl userCache;
    private Boolean hasDeptId = false;

    private PublishSubject<DataAction> dataActionSubject = PublishSubject.create();

    @Override public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify_otp, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentVerifyOtpBinding.bind(view);
        setupViews();
        presenter.start();
        dataActionSubject.onNext(DataAction.Fetch.INSTANCE);
    }

    private void setupViews() {
        //retrieveFromRemoteConfig();
        requireDialog().setCanceledOnTouchOutside(false);
        requireContext().registerReceiver(smsReceiver,
            new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        startSMSRetriever();
        setWaitingForOtpView();
    }

    private void retrieveFromRemoteConfig() {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.fetch(3600).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                mFirebaseRemoteConfig.activateFetched();
            }
            String strResponse =
                mFirebaseRemoteConfig.getString(getString(R.string.department_ids_key));
            if (!strResponse.isEmpty()) {
                deptIds.set(strResponse);
            }
        });
    }

    private void startSMSRetriever() {
        SmsRetrieverClient client = SmsRetriever.getClient(requireContext());
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(result -> {
            Timber.d("SmsRetriever Started");
        });
        task.addOnFailureListener(result -> {
            Timber.d("SMSREtriever Failed %s", result.getMessage());
        });
    }

    private void setWaitingForOtpView() {
        binding.llProgressBar.setVisibility(VISIBLE);
        binding.txtNotReceivedOtp.setVisibility(GONE);
        toggleSubmitButton(false);
    }

    public void setOtpReceivedView() {
        binding.llProgressBar.setVisibility(GONE);
        binding.txtNotReceivedOtp.setVisibility(GONE);
    }

    public void setOtpNotReceivedView() {
        binding.llProgressBar.setVisibility(GONE);
        binding.txtNotReceivedOtp.setVisibility(VISIBLE);
    }

    @Override public void renderTicker(@NonNull TimerState timerState) {
        if (timerState instanceof TimerState.InProgress) {
            long count = ((TimerState.InProgress) timerState).getCount();
            binding.txtCounter.setText(String.valueOf(count));
        } else {
            setOtpNotReceivedView();
        }
    }


    @NotNull @Override public String getOtp() {
        return binding.edtOtp.getText().toString().trim();
    }

    @Override public void setOtp(@NotNull String otp) {
        binding.edtOtp.setText(otp);
    }

    @Override public void renderOtpEntered() {
        setOtpReceivedView();
        toggleSubmitButton(true);
    }

    @Override public void renderSubmit(@NotNull UiModel<User> uiModel) {
        toggleProgressBar(uiModel.inProgress);
        toggleSubmitButton(!uiModel.inProgress);

        if (uiModel.isError()) {
            Throwable error = uiModel.error;
            showErrorMessage(error);
        }

        if (uiModel.data != null) {
            User data = uiModel.data;
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> deptIdData = gson.fromJson(deptIds.get(), type);
            if (deptIdData == null) {
                deptIdData = Collections.emptyList();
            }
            if (deptIdData.size() > 0) {
                for (String deptId : deptIdData) {
                    if (deptId.equals(data.departmentCode)) {
                        hasDeptId = true;
                        break;
                    }
                }
            }
            if (hasDeptId || deptIdData.size() == 0) {
                //showToast("Login Successful.");
                ((DashboardFragment) getParentFragment()).showAttendancePopupIfNotMarked();
                dismissAllowingStateLoss();
            } else {
                showPopup();
                userCache.clearUser();
            }
        }
    }

    @Override public void renderResendOtp(@NotNull UiModel<GenerateOTP> uiModel) {
        toggleProgressBar(uiModel.inProgress);
        binding.txtNotReceivedOtp.setEnabled(!uiModel.inProgress);

        if (uiModel.isError()) {
            showErrorMessage(uiModel.error);
        }

        if (uiModel.isSuccess()) {
            binding.edtOtp.setText("");
            dataActionSubject.onNext(new DataAction.RetrySection(OTP_RESENT));
            setWaitingForOtpView();
            startSMSRetriever();
        }
    }

    private void toggleSubmitButton(boolean enabled) {
        binding.btnConfirm.setEnabled(enabled);
    }


    private void toggleProgressBar(boolean inProgress) {
        binding.apiProgressBar.setVisibility(inProgress ? VISIBLE : GONE);
    }

    @Override public boolean isDataEmpty() {
        return false;
    }

    private void showToast(@NonNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void showErrorMessage(Throwable error) {
        showErrorMessage(ErrorUtils.getErrorOnFailure(error)
            .getResolvedMessage(getContext()));
    }

    private void showErrorMessage(String message) {
        binding.txtErrorMessage.setVisibility(VISIBLE);
        binding.txtErrorMessage.setText(message);
    }

    @Override public void onDestroyView() {
      //  presenter.stop();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(smsReceiver);
        }
        super.onDestroyView();
    }

    @Override public void showPopup() {
        showErrorMessage(
            "We are in process of migrating you to new HRMS Portal. Apologies but till then you need to continue with existing older Myzone app");
    }


    @NonNull
    @Override
    public Observable<DataAction> onDataAction() {
        return dataActionSubject;
    }

    @NonNull
    @Override
    public Observable<Unit> onConfirmClicked() {
        return RxView.clicks(binding.btnConfirm);
    }

    @NonNull
    @Override
    public Observable<Integer> onDoneClicked() {
        return  RxTextView.editorActions(binding.edtOtp,
                event -> event == EditorInfo.IME_ACTION_DONE);
    }

    @NonNull
    @Override
    public Observable<Integer> onOtpReceived() {
        return smsReceiver.read();
    }

    @NonNull
    @Override
    public Observable<CharSequence> onOtpTextChanged() {
        return RxTextView.textChanges(binding.edtOtp);
    }

    @NonNull
    @Override
    public Observable<Unit> onResendOtpClicked() {
        return RxView.clicks(binding.txtNotReceivedOtp);
    }
}
