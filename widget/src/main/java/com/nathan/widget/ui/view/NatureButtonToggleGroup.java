package com.nathan.widget.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;

import androidx.annotation.BoolRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.google.android.material.button.MaterialButton;
import com.nathan.widget.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class NatureButtonToggleGroup extends LinearLayout {

    private static final String TAG = "NatureButtonToggleGroup";
    private static final int DEF_STYLE_RES = R.style.Widget_Nature_ButtonToggleGroup;

    private final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners;
    private final Comparator<MaterialButton> childOrderComparator;
    @IdRes
    private final int defaultCheckId;

    private Integer[] childOrder;
    private boolean singleSelection;
    private boolean selectionRequired;
    private Set<Integer> checkedIds;

    public NatureButtonToggleGroup(@NonNull Context context) {
        this(context, null);
    }

    public NatureButtonToggleGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.natureButtonToggleGroupStyle);
    }

    public NatureButtonToggleGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        checkedIds = new HashSet<>();
        onButtonCheckedListeners = new LinkedHashSet<>();
        childOrderComparator = new Comparator<>() {
            public int compare(MaterialButton v1, MaterialButton v2) {
                int checked = Boolean.compare(v1.isChecked(), v2.isChecked());
                if (checked != 0) {
                    return checked;
                } else {
                    int stateful = Boolean.compare(v1.isPressed(), v2.isPressed());
                    return stateful != 0 ? stateful : Integer.compare(indexOfChild(v1), indexOfChild(v2));
                }
            }
        };

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NatureButtonToggleGroup, defStyleAttr, DEF_STYLE_RES);
        setSingleSelection(attributes.getBoolean(R.styleable.NatureButtonToggleGroup_singleSelection, false));
        defaultCheckId = attributes.getResourceId(R.styleable.NatureButtonToggleGroup_android_checkedButton, -1);
        selectionRequired = attributes.getBoolean(R.styleable.NatureButtonToggleGroup_selectionRequired, false);
        setChildrenDrawingOrderEnabled(true);
        setEnabled(attributes.getBoolean(R.styleable.NatureButtonToggleGroup_android_enabled, true));
        attributes.recycle();
        ViewCompat.setImportantForAccessibility(this, 1);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (defaultCheckId != -1) {
            updateCheckedIds(Collections.singleton(defaultCheckId));
        }
        adjustMarginHorizontal();
    }

    protected void dispatchDraw(@NonNull Canvas canvas) {
        updateChildOrder();
        super.dispatchDraw(canvas);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!(child instanceof MaterialButton)) {
            Log.e(TAG, "Child views must be of type NatureButton.");
        } else {
            super.addView(child, index, params);
            MaterialButton buttonChild = (MaterialButton) child;
            setGeneratedIdIfNeeded(buttonChild);
            setupButtonChild(buttonChild);
            checkInternal(buttonChild.getId(), buttonChild.isChecked());
            buttonChild.setEnabled(isEnabled());
            ViewCompat.setAccessibilityDelegate(buttonChild, new AccessibilityDelegateCompat() {
                public void onInitializeAccessibilityNodeInfo(@NonNull View host, @NonNull AccessibilityNodeInfoCompat info) {
                    super.onInitializeAccessibilityNodeInfo(host, info);
                    info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, getIndexWithinVisibleButtons(host), 1, false, ((MaterialButton) host).isChecked()));
                }
            });
        }
    }

    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        AccessibilityNodeInfoCompat infoCompat = AccessibilityNodeInfoCompat.wrap(info);
        infoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getVisibleButtonCount(), false, isSingleSelection() ? 1 : 2));
    }

    public void check(@IdRes int id) {
        checkInternal(id, true);
    }

    public void uncheck(@IdRes int id) {
        checkInternal(id, false);
    }

    public void clearChecked() {
        updateCheckedIds(new HashSet<>());
    }

    @IdRes
    public int getCheckedButtonId() {
        return singleSelection && !checkedIds.isEmpty() ? checkedIds.iterator().next() : -1;
    }

    @NonNull
    public List<Integer> getCheckedButtonIds() {
        List<Integer> orderedCheckedIds = new ArrayList<>();

        for (int i = 0; i < getChildCount(); ++i) {
            int childId = getChildButton(i).getId();
            if (checkedIds.contains(childId)) {
                orderedCheckedIds.add(childId);
            }
        }

        return orderedCheckedIds;
    }

    public void addOnButtonCheckedListener(@NonNull OnButtonCheckedListener listener) {
        onButtonCheckedListeners.add(listener);
    }

    public void removeOnButtonCheckedListener(@NonNull OnButtonCheckedListener listener) {
        onButtonCheckedListeners.remove(listener);
    }

    public void clearOnButtonCheckedListeners() {
        onButtonCheckedListeners.clear();
    }

    public boolean isSingleSelection() {
        return singleSelection;
    }

    public void setSingleSelection(boolean single) {
        if (singleSelection != single) {
            singleSelection = single;
            clearChecked();
        }
    }

    public void setSelectionRequired(boolean required) {
        selectionRequired = required;
    }

    public boolean isSelectionRequired() {
        return selectionRequired;
    }

    public void setSingleSelection(@BoolRes int id) {
        setSingleSelection(getResources().getBoolean(id));
    }

    private void setCheckedStateForView(@IdRes int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView instanceof MaterialButton) {
            ((MaterialButton) checkedView).setChecked(checked);
        }
    }

    /**
     * 设置非单选时，item的间距，这个值保持和Padding一致
     */
    private void adjustMarginHorizontal() {
        int marginMini = Math.max(getPaddingStart(), getPaddingEnd()) / 2;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                if (i == 0) {
                    ((MarginLayoutParams) layoutParams).setMarginEnd(marginMini);
                    continue;
                }
                if (i == count - 1) {
                    ((MarginLayoutParams) layoutParams).setMarginStart(marginMini);
                    continue;
                }
                ((MarginLayoutParams) layoutParams).setMarginStart(marginMini);
                ((MarginLayoutParams) layoutParams).setMarginEnd(marginMini);
            }
        }
    }

    private MaterialButton getChildButton(int index) {
        return (MaterialButton) getChildAt(index);
    }

    private int getFirstVisibleChildIndex() {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; ++i) {
            if (isChildVisible(i)) {
                return i;
            }
        }

        return -1;
    }

    private int getLastVisibleChildIndex() {
        int childCount = getChildCount();

        for (int i = childCount - 1; i >= 0; --i) {
            if (isChildVisible(i)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isChildVisible(int i) {
        View child = getChildAt(i);
        return child.getVisibility() != 8;
    }

    private int getVisibleButtonCount() {
        int count = 0;

        for (int i = 0; i < getChildCount(); ++i) {
            if (getChildAt(i) instanceof MaterialButton && isChildVisible(i)) {
                ++count;
            }
        }

        return count;
    }

    private int getIndexWithinVisibleButtons(@Nullable View child) {
        if (!(child instanceof MaterialButton)) {
            return -1;
        } else {
            int index = 0;

            for (int i = 0; i < getChildCount(); ++i) {
                if (getChildAt(i) == child) {
                    return index;
                }

                if (getChildAt(i) instanceof MaterialButton && isChildVisible(i)) {
                    ++index;
                }
            }

            return -1;
        }
    }

    private void checkInternal(@IdRes int buttonId, boolean checked) {
        if (buttonId == -1) {
            Log.e(TAG, "Button ID is not valid: " + buttonId);
        } else {
            Set<Integer> newCheckedIds = new HashSet<>(checkedIds);
            if (checked && !newCheckedIds.contains(buttonId)) {
                if (singleSelection && !newCheckedIds.isEmpty()) {
                    newCheckedIds.clear();
                }

                newCheckedIds.add(buttonId);
            } else {
                if (checked || !newCheckedIds.contains(buttonId)) {
                    return;
                }

                if (!selectionRequired || newCheckedIds.size() > 1) {
                    newCheckedIds.remove(buttonId);
                }
            }

            updateCheckedIds(newCheckedIds);
        }
    }

    private void updateCheckedIds(Set<Integer> newCheckedIds) {
        Set<Integer> previousCheckedIds = checkedIds;
        checkedIds = new HashSet<>(newCheckedIds);

        for (int i = 0; i < getChildCount(); ++i) {
            int buttonId = getChildButton(i).getId();
            setCheckedStateForView(buttonId, newCheckedIds.contains(buttonId));
            if (previousCheckedIds.contains(buttonId) != newCheckedIds.contains(buttonId)) {
                dispatchOnButtonChecked(buttonId, newCheckedIds.contains(buttonId));
            }
        }

        invalidate();
    }

    private void dispatchOnButtonChecked(@IdRes int buttonId, boolean checked) {
        for (OnButtonCheckedListener listener : onButtonCheckedListeners) {
            listener.onButtonChecked(this, buttonId, checked);
        }

    }

    private void setGeneratedIdIfNeeded(@NonNull MaterialButton materialButton) {
        if (materialButton.getId() == -1) {
            materialButton.setId(ViewCompat.generateViewId());
        }

    }

    private void setupButtonChild(@NonNull MaterialButton buttonChild) {
        buttonChild.setMaxLines(1);
        buttonChild.setEllipsize(TextUtils.TruncateAt.END);
        buttonChild.setCheckable(true);
        buttonChild.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !buttonChild.isChecked();
                buttonChild.setChecked(checked);
                onButtonCheckedStateChanged(buttonChild, checked);
            }
        });
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (childOrder != null && i < childOrder.length) {
            return childOrder[i];
        } else {
            Log.w(TAG, "Child order wasn't updated");
            return i;
        }
    }

    private void updateChildOrder() {
        SortedMap<MaterialButton, Integer> viewToIndexMap = new TreeMap<>(childOrderComparator);
        int childCount = getChildCount();

        for (int i = 0; i < childCount; ++i) {
            viewToIndexMap.put(getChildButton(i), i);
        }

        childOrder = viewToIndexMap.values().toArray(new Integer[0]);
    }

    void onButtonCheckedStateChanged(@NonNull MaterialButton button, boolean isChecked) {
        checkInternal(button.getId(), isChecked);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (int i = 0; i < getChildCount(); ++i) {
            MaterialButton childButton = getChildButton(i);
            childButton.setEnabled(enabled);
        }

    }

    public interface OnButtonCheckedListener {
        void onButtonChecked(NatureButtonToggleGroup group, @IdRes int id, boolean isChecked);
    }
}