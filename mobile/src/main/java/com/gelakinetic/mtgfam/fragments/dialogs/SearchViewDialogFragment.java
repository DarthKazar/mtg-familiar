package com.gelakinetic.mtgfam.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gelakinetic.mtgfam.R;
import com.gelakinetic.mtgfam.fragments.SearchViewFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Class that creates dialogs for SearchViewFragment
 */
public class SearchViewDialogFragment extends FamiliarDialogFragment {

    /* Dialog IDs */
    public static final int SET_LIST = 1;
    public static final int FORMAT_LIST = 2;
    public static final int RARITY_LIST = 3;

    /**
     * @return The currently viewed SearchViewFragment
     */
    private SearchViewFragment getParentSearchViewFragment() {
        return (SearchViewFragment) getFamiliarFragment();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            getParentSearchViewFragment().checkDialogButtonColors();
        } catch (NullPointerException e) {
            /* Ignore it if there's no activity */
        }
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

                /* This will be set to false if we are returning a null dialog. It prevents a crash */
        setShowsDialog(true);

        mDialogId = getArguments().getInt(ID_KEY);
        try {
                    /* Build the dialogs to display format, rarity, and set choices. The arrays were already filled in
                       onCreate() */
            switch (mDialogId) {
                case SET_LIST: {
                    getParentSearchViewFragment().mSetDialog = new MaterialDialog.Builder(this.getActivity())
                            .title(R.string.search_sets)
                            .positiveText(R.string.dialog_ok)
                            .items(getParentSearchViewFragment().mSetNames)
                            .alwaysCallMultiChoiceCallback()
                            .itemsCallbackMultiChoice(toIntegerArray(getParentSearchViewFragment().mSetCheckedIndices), new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    getParentSearchViewFragment().mSetCheckedIndices = toIntArray(which);
                                    return true;
                                }
                            })
                            .build();
                    return getParentSearchViewFragment().mSetDialog;
                }
                case FORMAT_LIST: {
                    getParentSearchViewFragment().mFormatDialog = new MaterialDialog.Builder(this.getActivity())
                            .title(R.string.search_formats)
                            .items(getParentSearchViewFragment().mFormatNames)
                            .itemsCallbackSingleChoice(getParentSearchViewFragment().mSelectedFormat, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    getParentSearchViewFragment().mSelectedFormat = which;
                                    return true;
                                }
                            })
                            .positiveText(R.string.dialog_ok)
                            .build();
                    return getParentSearchViewFragment().mFormatDialog;
                }
                case RARITY_LIST: {
                    getParentSearchViewFragment().mRarityDialog = new MaterialDialog.Builder(this.getActivity())
                            .title(R.string.search_rarities)
                            .positiveText(R.string.dialog_ok)
                            .items(getParentSearchViewFragment().mRarityNames)
                            .alwaysCallMultiChoiceCallback()
                            .itemsCallbackMultiChoice(toIntegerArray(getParentSearchViewFragment().mRarityCheckedIndices), new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    getParentSearchViewFragment().mRarityCheckedIndices = toIntArray(which);
                                    return true;
                                }
                            })
                            .build();
                    return getParentSearchViewFragment().mRarityDialog;
                }
                default: {
                    return DontShowDialog();
                }
            }
        } catch (NullPointerException e) {
                    /* if the db failed to open, these arrays will be null. */
            getParentSearchViewFragment().handleFamiliarDbException(false);
            return DontShowDialog();
        }
    }

    /**
     * Make an int[] from an Integer[]
     *
     * @param which An Integer[]
     * @return An int[] with the same values as "which"
     */
    private int[] toIntArray(Integer[] which) {
        int tmp[] = new int[which.length];
        for (int i = 0; i < which.length; i++) {
            tmp[i] = which[i];
        }
        return tmp;
    }

    /**
     * Make an int[] from an Integer[]
     *
     * @param which An Integer[]
     * @return An int[] with the same values as "which"
     */
    private Integer[] toIntegerArray(int[] which) {
        Integer tmp[] = new Integer[which.length];
        for (int i = 0; i < which.length; i++) {
            tmp[i] = which[i];
        }
        return tmp;
    }
}