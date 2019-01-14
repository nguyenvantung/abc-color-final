package com.color.kid.coloring.util;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;

import com.color.kid.colorpaintkids.R;


public class FragmentUtil {

    public enum AnimationType {
        Image,
        Shop
    }
    public static void pushFragment(FragmentActivity activity, @NonNull Fragment fragment, @Nullable Bundle data) {
        showFragment(activity, fragment, true, data, null, false);
    }

    public static void replaceFragment(FragmentActivity activity, @NonNull Fragment fragment, @Nullable Bundle data) {
        showFragment(activity, fragment, false, data, null, false);
    }

    public static void showFragment(FragmentActivity activity, @NonNull Fragment fragment, boolean isPushInsteadOfReplace, @Nullable Bundle data, @Nullable String tag, boolean isShowAnimation) {
        if (activity == null) {
            return;
        }

        if (data != null) {
            fragment.setArguments(data);
        }

        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

        if (isShowAnimation) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,
                    R.anim.slide_out_up);
        }

        fragmentTransaction.replace(R.id.fr_content, fragment, tag);
        if (isPushInsteadOfReplace) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        try {
            fragmentTransaction.commit();
        } catch (Exception ex) {
            DebugLog.e("Can not perform this action after onSaveInstanceState!");
        }
    }

    public static void pushSharedFragment(FragmentActivity activity, Fragment fromFragment, Fragment fragment, String tag, View sharedView, String transitionName, AnimationType animationType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            TransitionSet fragmentTrans = new TransitionSet();
            fragmentTrans.addTransition(new Fade().setDuration(300));

            if (animationType == AnimationType.Shop) {
                fragmentTrans.setStartDelay(400);
            }
            // out animation
            fromFragment.setSharedElementReturnTransition(setShareElementTransByType(animationType));
            fromFragment.setExitTransition(fragmentTrans);

            //in animation
            fragment.setSharedElementEnterTransition(setShareElementTransByType(animationType));
            fragment.setEnterTransition(fragmentTrans);

            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fr_content, fragment);
            fragmentTransaction.addSharedElement(sharedView, transitionName);
            fragmentTransaction.addToBackStack(null);
            try {
                activity.getSupportFragmentManager().executePendingTransactions();
                fragmentTransaction.commit();
            } catch (Exception ex) {
                DebugLog.e("Can not perform this action after onSaveInstanceState!");
            }
        } else {
            pushFragment(activity, fragment, null);
        }
    }

    private static TransitionSet setShareElementTransByType(AnimationType animationType) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return null;
        }

        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());

        transitionSet.setStartDelay(animationType == AnimationType.Shop ? 100 : 0);
        if (animationType == AnimationType.Shop) {
            transitionSet.setDuration(400);
        }
        return transitionSet;
    }


    public static void popEntireFragmentBackStack(@NonNull FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    public static void popFragmentBackStack(@NonNull FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }

    public static void popLevel1FragmentBackStack(@NonNull FragmentActivity activity) {
        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        for (int i = 0; i < backStackCount - 1; i++) {
            fm.popBackStack();
        }
    }

}
