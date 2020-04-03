package com.mosl.tabapp.actionableinsight;

import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.navigation.ScreenNavigatorImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.io.Serializable;
import java.util.ArrayList;

import static com.mosl.tabapp.actionableinsight.ActionableInsightDialogFragment.ARG_DASHBOARD_FOR;
import static com.mosl.tabapp.actionableinsight.ActionableInsightDialogFragment.ARG_LIST_DATA;

@Module public abstract class ActionableInsightModule {

    @Provides
    static ActionableInsightData provideInsightList(ActionableInsightDialogFragment fragment) {
        Serializable data;
        if (fragment.getArguments() != null) {
            data = fragment.getArguments()
                .getSerializable(ARG_LIST_DATA);
            return new ActionableInsightData((ArrayList<WidgetModel>) data, fragment.getArguments()
                .getString(ARG_DASHBOARD_FOR));
        }
        return new ActionableInsightData(new ArrayList<>(), "");
    }

    @Binds
    abstract ActionableInsightView provideInsightView(ActionableInsightDialogFragment fragment);

    @Binds
    abstract ActionableInsightNavigator provideInsightNavigator(ScreenNavigatorImpl navigator);
}
