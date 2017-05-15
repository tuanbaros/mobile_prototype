package com.framgia.mobileprototype.library;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ActivityIconBinding;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.ITypeface;
import java.util.ArrayList;

public class IconActivity extends BaseActivity implements IconItemHandler {
    private static final int NUMBER_COLUMN = 4;
    public static final String EXTRA_ICON = "EXTRA_ICON";
    private ArrayList<String> mIcons = new ArrayList<>();
    private IconAdapter mIconAdapter;
    private ActivityIconBinding mIconBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIconBinding = DataBindingUtil.setContentView(this, R.layout.activity_icon);
        setUpRecyclerView();
        getData();
    }

    private void setUpRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, NUMBER_COLUMN);
        mIconBinding.recyclerIcons.setLayoutManager(manager);
        mIconBinding.recyclerIcons.setHasFixedSize(true);
        mIconAdapter = new IconAdapter(this);
        mIconBinding.recyclerIcons.setAdapter(mIconAdapter);
    }

    private void getData() {
        for (ITypeface iTypeface : Iconics.getRegisteredFonts(this)) {
            if (iTypeface.getIcons() != null) {
                for (String icon : iTypeface.getIcons()) {
                    mIcons.add(icon);
                }
                mIconAdapter.setUpData(mIcons);
                break;
            }
        }
    }

    @Override
    public void itemClicked(String icon) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ICON, icon);
        setResult(RESULT_OK, intent);
        finish();
    }
}
