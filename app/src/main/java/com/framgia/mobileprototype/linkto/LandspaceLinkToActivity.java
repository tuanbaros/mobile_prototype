package com.framgia.mobileprototype.linkto;

import android.content.Context;
import android.content.Intent;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Project;

public class LandspaceLinkToActivity extends LinkToActivity {
    public static Intent getLinkToIntent(Context context, Project project, Element element) {
        Intent intent = new Intent(context, LandspaceLinkToActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        intent.putExtra(EXTRA_ELEMENT, element);
        return intent;
    }
}
