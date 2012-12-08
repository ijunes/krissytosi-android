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

package com.krissytosi.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TabHost;

public class ReClickableTabHost extends TabHost {

    private ReClickableTabHostListener reClickableTabHostListener;

    public ReClickableTabHost(Context context) {
        super(context);
    }

    public ReClickableTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentTab(int index) {
        if (index == getCurrentTab()) {
            if (reClickableTabHostListener != null) {
                reClickableTabHostListener.onCurrentTabClicked(getCurrentTabTag());
            }
        } else {
            super.setCurrentTab(index);
        }
    }

    // Getters/Setters

    public ReClickableTabHostListener getReClickableTabHostListener() {
        return reClickableTabHostListener;
    }

    public void setReClickableTabHostListener(ReClickableTabHostListener reClickableTabHostListener) {
        this.reClickableTabHostListener = reClickableTabHostListener;
    }
}
