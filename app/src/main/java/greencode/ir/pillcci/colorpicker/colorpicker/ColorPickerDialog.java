/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package greencode.ir.pillcci.colorpicker.colorpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import greencode.ir.pillcci.R;


/**
 * A dialog which takes in as input an array of colors and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 */
public class ColorPickerDialog extends DialogFragment implements ColorPickerSwatch.OnColorSelectedListener {

    public static final int SIZE_LARGE = 1;
    public static final int SIZE_SMALL = 2;
    protected static final String KEY_TITLE_ID = "title_id";
    protected static final String KEY_COLORS = "colors";
    protected static final String KEY_COLOR_CONTENT_DESCRIPTIONS = "color_content_descriptions";
    protected static final String KEY_SELECTED_COLOR = "selected_color";
    protected static final String KEY_COLUMNS = "columns";
    protected static final String KEY_SIZE = "size";
    protected static final String KEY_BACKWARDS_ORDER = "backwards_order";
    protected static final String KEY_STROKE_WIDTH = "stroke_width";
    protected static final String KEY_STROKE_COLOR = "stroke_color";
    protected AlertDialog mAlertDialog;
    protected int mTitleResId = R.string.color_picker_default_title;
    protected int[] mColors = null;
    protected String[] mColorContentDescriptions = null;
    protected int mSelectedColor;
    protected int mColumns;
    protected int mSize;
    protected int mStrokeWidth;
    protected int mStrokeColor;
    protected boolean mBackwardsOrder;
    protected ColorPickerSwatch.OnColorSelectedListener mListener;
    private ColorPickerPalette mPalette;
    private ProgressBar mProgress;

    public ColorPickerDialog() {
        // Empty constructor required for dialog fragments.
    }

    public static ColorPickerDialog newInstance(int titleResId, int[] colors, int selectedColor,
            int columns, int size) {
        return newInstance(titleResId, colors, selectedColor, columns, size, true, 0, 0);
    }

    public static ColorPickerDialog newInstance(int titleResId, int[] colors, int selectedColor,
            int columns, int size, boolean backwardsOrder) {
        return newInstance(titleResId, colors, selectedColor, columns, size, backwardsOrder, 0, 0);
    }

    public static ColorPickerDialog newInstance(int titleResId, int[] colors, int selectedColor,
            int columns, int size, boolean backwardsOrder, int strokeWidth, int strokeColor) {
        ColorPickerDialog ret = new ColorPickerDialog();
        ret.initialize(titleResId, colors, selectedColor, columns, size, backwardsOrder,
                strokeWidth, strokeColor);
        return ret;
    }

    public void initialize(int titleResId, int[] colors, int selectedColor, int columns, int size,
            boolean backwardsDisable, int strokeWidth, int strokeColor) {
        setArguments(titleResId, columns, size, backwardsDisable, strokeWidth, strokeColor);
        setColors(colors, selectedColor);
    }

    public void setArguments(int titleResId, int columns, int size, boolean backwardsOrder,
            int strokeWidth, int strokeColor) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TITLE_ID, titleResId);
        bundle.putInt(KEY_COLUMNS, columns);
        bundle.putInt(KEY_SIZE, size);
        bundle.putBoolean(KEY_BACKWARDS_ORDER, backwardsOrder);
        bundle.putInt(KEY_STROKE_WIDTH, strokeWidth);
        bundle.putInt(KEY_STROKE_COLOR, strokeColor);
        setArguments(bundle);
    }

    public void setOnColorSelectedListener(ColorPickerSwatch.OnColorSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitleResId = getArguments().getInt(KEY_TITLE_ID);
            mColumns = getArguments().getInt(KEY_COLUMNS);
            mSize = getArguments().getInt(KEY_SIZE);
            mBackwardsOrder = getArguments().getBoolean(KEY_BACKWARDS_ORDER);
            mStrokeWidth = getArguments().getInt(KEY_STROKE_WIDTH);
            mStrokeColor = getArguments().getInt(KEY_STROKE_COLOR);
        }

        if (savedInstanceState != null) {
            mColors = savedInstanceState.getIntArray(KEY_COLORS);
            mSelectedColor = (int) savedInstanceState.getSerializable(KEY_SELECTED_COLOR);
            mColorContentDescriptions = savedInstanceState.getStringArray(
                    KEY_COLOR_CONTENT_DESCRIPTIONS);
            mBackwardsOrder = savedInstanceState.getBoolean(KEY_BACKWARDS_ORDER);
            mStrokeWidth = (int) savedInstanceState.getSerializable(KEY_STROKE_WIDTH);
            mStrokeColor = (int) savedInstanceState.getSerializable(KEY_STROKE_COLOR);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.color_picker_dialog, null);
        mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
        mPalette = (ColorPickerPalette) view.findViewById(R.id.color_picker);
        mPalette.init(mSize, mColumns, this, mBackwardsOrder);

        if (mColors != null) {
            showPaletteView();
        }

        mAlertDialog = new AlertDialog.Builder(activity)
                .setTitle(mTitleResId)
                .setView(view)
                .create();
        mAlertDialog.setCanceledOnTouchOutside(true);
        return mAlertDialog;
    }

    @Override
    public void onColorSelected(int color) {
        if (mListener != null) {
            mListener.onColorSelected(color);
        }

        if (getTargetFragment() instanceof ColorPickerSwatch.OnColorSelectedListener) {
            final ColorPickerSwatch.OnColorSelectedListener listener =
                    (ColorPickerSwatch.OnColorSelectedListener) getTargetFragment();
            listener.onColorSelected(color);
        }

        if (color != mSelectedColor) {
            mSelectedColor = color;
            // Redraw palette to show checkmark on newly selected color before dismissing.
            mPalette.drawPalette(mColors, mSelectedColor, mStrokeWidth, mStrokeColor);
        }

        dismiss();
    }

    public void showPaletteView() {
        if (mProgress != null && mPalette != null) {
            mProgress.setVisibility(View.GONE);
            refreshPalette();
            mPalette.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBarView() {
        if (mProgress != null && mPalette != null) {
            mProgress.setVisibility(View.VISIBLE);
            mPalette.setVisibility(View.GONE);
        }
    }

    public void setColors(int[] colors, int selectedColor) {
        if (mColors != colors || mSelectedColor != selectedColor) {
            mColors = colors;
            mSelectedColor = selectedColor;
            refreshPalette();
        }
    }

    public void setColorContentDescriptions(String[] colorContentDescriptions) {
        if (mColorContentDescriptions != colorContentDescriptions) {
            mColorContentDescriptions = colorContentDescriptions;
            refreshPalette();
        }
    }

    private void refreshPalette() {
        if (mPalette != null && mColors != null) {
            mPalette.drawPalette(mColors, mSelectedColor, mColorContentDescriptions, mStrokeWidth,
                    mStrokeColor);
        }
    }

    public int[] getColors() {
        return mColors;
    }

    public void setColors(int[] colors) {
        if (mColors != colors) {
            mColors = colors;
            refreshPalette();
        }
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int color) {
        if (mSelectedColor != color) {
            mSelectedColor = color;
            refreshPalette();
        }
    }

    public void setStrokeWidth(int width) {
        if (mStrokeWidth != width) {
            mStrokeWidth = width;
            refreshPalette();
        }
    }

    public void setStrokeColor(int color) {
        if (mStrokeColor != color) {
            mStrokeColor = color;
            refreshPalette();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_COLORS, mColors);
        outState.putSerializable(KEY_SELECTED_COLOR, mSelectedColor);
        outState.putStringArray(KEY_COLOR_CONTENT_DESCRIPTIONS, mColorContentDescriptions);
        outState.putBoolean(KEY_BACKWARDS_ORDER, mBackwardsOrder);
        outState.putInt(KEY_STROKE_WIDTH, mStrokeWidth);
        outState.putInt(KEY_STROKE_COLOR, mStrokeColor);
    }
}
