package com.framgia.mobileprototype.draw;

import android.content.Context;
import android.content.Intent;
import com.framgia.mobileprototype.data.model.Project;

/**
 * Created by FRAMGIA\nguyen.thanh.tuan on 26/04/2017.
 */

public class LandscapeDrawActivity extends DrawActivity {
    public static Intent getDrawIntent(Context context, Project project) {
        Intent intent = new Intent(context, LandscapeDrawActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }
}
