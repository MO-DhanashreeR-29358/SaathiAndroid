package com.mosl.tabapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pramod on 02/03/17.
 */

public class SecondClickModel implements Serializable {
    SecondClickHeaderModel headerModel;
    List<KeyValuePairModel> bottomModel;

    public SecondClickModel(SecondClickHeaderModel headerModel, List<KeyValuePairModel> bottomModel) {
        this.headerModel = headerModel;
        this.bottomModel = bottomModel;
    }

    public SecondClickHeaderModel getHeaderModel() {
        return headerModel;
    }

    public void setHeaderModel(SecondClickHeaderModel headerModel) {
        this.headerModel = headerModel;
    }

    public List<KeyValuePairModel> getBottomModel() {
        return bottomModel;
    }

    public void setBottomModel(List<KeyValuePairModel> bottomModel) {
        this.bottomModel = bottomModel;
    }
}
