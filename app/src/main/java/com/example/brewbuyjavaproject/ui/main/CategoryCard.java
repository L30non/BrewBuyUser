package com.example.brewbuyjavaproject.ui.main;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.brewbuyjavaproject.R;

public class CategoryCard extends ConstraintLayout {
    private AppCompatImageView icon;
    private AppCompatTextView title;
    private AppCompatTextView description;

    public CategoryCard(@NonNull Context context) {
        super(context);
        init();
    }

    public CategoryCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.category_card, this);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
    }

    public void setIcon(int iconResId) {
        icon.setImageResource(iconResId);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setDescription(String descriptionText) {
        description.setText(descriptionText);
    }
}
