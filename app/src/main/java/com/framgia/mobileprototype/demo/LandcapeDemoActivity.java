package com.framgia.mobileprototype.demo;

import android.content.Context;
import android.content.Intent;

public class LandcapeDemoActivity extends DemoActivity {

    public static Intent getDemoIntent(Context context, String mockEntryId) {
        Intent intent = new Intent(context, LandcapeDemoActivity.class);
        intent.putExtra(EXTRA_MOCK_ENTRY_ID, mockEntryId);
        return intent;
    }

    public static Intent getDemoIntent(Context context, String mockEntryId, String anim) {
        Intent intent = new Intent(context, LandcapeDemoActivity.class);
        intent.putExtra(EXTRA_MOCK_ENTRY_ID, mockEntryId);
        intent.putExtra(EXTRA_TRANSITION, anim);
        return intent;
    }

    @Override
    public void showNextScreen(String linkTo, String anim) {
        clearAllElement();
        startActivity(LandcapeDemoActivity.getDemoIntent(this, linkTo, anim));
        finish();
    }
}
