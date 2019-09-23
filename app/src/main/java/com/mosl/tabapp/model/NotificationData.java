package com.mosl.tabapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;

/**
 * Created by Sanket Gurav on 7/31/2017.
 */

public class NotificationData implements Parcelable {//, Serializable
    private String page;
    private String section;
    private String subSection;
    private Map<String, String> paramMap;



    public NotificationData(String jobj) {
        try {
            JSONObject jobjTmp = new JSONObject(jobj);
            this.page = (jobjTmp.isNull("Page") || jobjTmp.getString("Page").equals("")) ? "" : jobjTmp.getString("Page");//Page
            this.section = (jobjTmp.isNull("Section") || jobjTmp.getString("Section").equals("")) ? "" : jobjTmp.getString("Section");//Page
            this.subSection = (jobjTmp.isNull("SubSection") || jobjTmp.getString("SubSection").equals("")) ? "" : jobjTmp.getString("SubSection");//Page
            this.paramMap = new HashMap<>();
            if (!jobjTmp.isNull("Params")) {
                JSONArray jsonArray = jobjTmp.getJSONArray("Params");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonTempChild = jsonArray.getJSONObject(i);
                    JSONArray jsonArrayNameList = jsonTempChild.names();
                    for (int j = 0; j < jsonArrayNameList.length(); j++) {
                        String name = isBlank(jsonArrayNameList.get(j).toString()) ? "0"
                            : jsonArrayNameList.get(j).toString();
                        this.paramMap.put(name, jsonTempChild.getString(name));
                    }
                }
            }

        } catch (JSONException je) {
            this.page = "";//Page
            this.section = "";//Page
            this.subSection = "";//Page
            this.paramMap = new HashMap<>();
        }
       /*
       this.page = page;
        this.section = section;
        this.subSection = subSection;
        this.paramMap = paramMap;
        */
    }

    //endregion

    //region Getter and Setter
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubSection() {
        return subSection;
    }

    public void setSubSection(String subSection) {
        this.subSection = subSection;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    //endregion

    //region Parcelable implementation
    protected NotificationData(Parcel in) {
        this.page = in.readString();
        this.section = in.readString();
        this.subSection = in.readString();
        //read map
        int size = in.readInt();
        this.paramMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            paramMap.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(page);
        out.writeString(section);
        out.writeString(subSection);
        //Writing map
        out.writeInt(paramMap.size());
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            out.writeString(entry.getKey());
            out.writeString(entry.getValue());
        }
        //Writing map end
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationData> CREATOR = new Creator<NotificationData>() {
        @Override
        public NotificationData createFromParcel(Parcel in) {
            return new NotificationData(in);
        }

        @Override
        public NotificationData[] newArray(int size) {
            return new NotificationData[size];
        }
    };
    //endregion
}
