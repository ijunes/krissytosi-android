/*
   Copyright 2012 Sean O' Shea

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.krissytosi.fragments.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

public class BaseDetailView {

    private View baseView;
    private Context context;
    protected ViewPager viewPager;

    /**
     * Used to clean up a detail view after the user has navigated away from it.
     */
    public void beforeDetatched() {
        if (viewPager != null) {
            viewPager.removeAllViews();
        }
    }

    // Getters/Setters

    public View getBaseView() {
        return baseView;
    }

    public void setBaseView(View baseView) {
        this.baseView = baseView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
