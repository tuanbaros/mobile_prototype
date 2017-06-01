package com.framgia.mobileprototype.demo;

import android.content.Context;
import android.content.Intent;
import com.framgia.mobileprototype.data.model.Project;

/**
 * Created by FRAMGIA\nguyen.thanh.tuan on 29/05/2017.
 */

public class LandscapeOnlineActivity extends OnlineDemoActivity {
    public static Intent getOnlineDemoIntent(Context context, Project project) {
        Intent intent = new Intent(context, LandscapeOnlineActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    public static Intent getOnlineDemoIntent(Context context, String mockEntryId, String anim) {
        Intent intent = new Intent(context, LandscapeOnlineActivity.class);
        intent.putExtra(EXTRA_MOCK_ENTRY_ID, mockEntryId);
        intent.putExtra(EXTRA_TRANSITION, anim);
        return intent;
    }

    @Override
    public void showNextScreen(String linkTo, String anim) {
        clearAllElement();
        startActivity(LandscapeOnlineActivity.getOnlineDemoIntent(this, linkTo, anim));
        finish();
    }
}
