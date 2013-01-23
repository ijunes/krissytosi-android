/*
   Copyright 2012 - 2013 Sean O' Shea

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

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.krissytosi.R;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.KrissyTosiUtils.ImageSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to back the grid of images which allows the user focus in on one
 * particular photo set.
 */
public class PhotoSetsAdapter extends ArrayAdapter<PhotoSet> {

    private final List<PhotoSet> photoSets;

    public PhotoSetsAdapter(Context context, int textViewResourceId,
            ArrayList<PhotoSet> photoSets) {
        super(context, textViewResourceId, photoSets);
        this.photoSets = photoSets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.photoset, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.photoSetImageView = (ImageView) v.findViewById(R.id.photoset_image);
            viewHolder.photoSetTitle = (TextView) v.findViewById(R.id.photoset_title);
            viewHolder.photoSetSubTitle = (TextView) v.findViewById(R.id.photoset_sub_title);
            v.setTag(viewHolder);
        }
        return populate(v, position);
    }

    private View populate(View v, int position) {
        if (position < photoSets.size()) {
            final PhotoSet photoSet = photoSets.get(position);
            if (photoSet != null) {
                final ViewHolder holder = (ViewHolder) v.getTag();
                if (holder != null) {
                    if (photoSet.getPhotos() != null && !photoSet.getPhotos().isEmpty()) {
                        UrlImageViewHelper.setUrlDrawable(holder.photoSetImageView,
                                KrissyTosiUtils.determineImageUrl(photoSet.getPhotos().get(0),
                                        ImageSize.MEDIUM));
                        holder.photoSetSubTitle.setText(String.format(
                                v.getResources().getString(R.string.number_of_images),
                                photoSet.getPhotos().size()));
                    }
                    holder.photoSetTitle.setText(photoSet.getTitle());
                }
            }
        }
        return v;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (photoSets != null) {
            count = photoSets.size();
        }
        return count;
    }

    public static class ViewHolder {
        public ImageView photoSetImageView;
        public TextView photoSetTitle;
        public TextView photoSetSubTitle;
    }
}
