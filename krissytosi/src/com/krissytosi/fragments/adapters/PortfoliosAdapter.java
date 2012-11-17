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

package com.krissytosi.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krissytosi.R;
import com.krissytosi.api.domain.Portfolio;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to back the grid of images which allows the user focus in on one
 * particular portfolio.
 */
public class PortfoliosAdapter extends ArrayAdapter<Portfolio> {

    private final List<Portfolio> portfolios;

    public PortfoliosAdapter(Context context, int textViewResourceId,
            ArrayList<Portfolio> portfolios) {
        super(context, textViewResourceId, portfolios);
        this.portfolios = portfolios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.portfolio, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.portfolioImageView = (ImageView) v.findViewById(R.id.portfolio_image);
            viewHolder.portfolioTitle = (TextView) v.findViewById(R.id.portfolio_title);
            viewHolder.portfolioSubTitle = (TextView) v.findViewById(R.id.portfolio_sub_title);
            v.setTag(viewHolder);
        }
        return populate(v, position);
    }

    private View populate(View v, int position) {
        if (position < portfolios.size()) {
            final Portfolio portfolio = portfolios.get(position);
            if (portfolio != null) {
                final ViewHolder holder = (ViewHolder) v.getTag();
                if (holder != null) {
                    holder.portfolioTitle.setText(portfolio.getName());
                    holder.portfolioSubTitle.setText(String.format(
                            v.getResources().getString(R.string.number_of_images),
                            portfolio.getNumberOfImages()));
                }
            }
        }
        return v;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (portfolios != null) {
            count = portfolios.size();
        }
        return count;
    }

    public static class ViewHolder {
        public ImageView portfolioImageView;
        public TextView portfolioTitle;
        public TextView portfolioSubTitle;
    }
}
