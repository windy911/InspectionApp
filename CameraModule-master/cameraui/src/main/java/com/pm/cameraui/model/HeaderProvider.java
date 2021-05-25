package com.pm.cameraui.model;

import com.pm.cameraui.Constants;
import com.pm.cameraui.api.HttpHeaderProvider;

public class HeaderProvider implements HttpHeaderProvider {
    @Override
    public String getToken() {
        return Constants.userInfo == null ? "" : Constants.userInfo.getToken();
    }
}
