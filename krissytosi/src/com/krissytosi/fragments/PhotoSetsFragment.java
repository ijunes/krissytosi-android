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

package com.krissytosi.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ViewFlipper;

import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.fragments.adapters.PhotoSetsAdapter;
import com.krissytosi.fragments.views.PhotoSetDetailView;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a grid of photo sets.
 */
public class PhotoSetsFragment extends BaseFragment implements OnItemClickListener {

    private static final String LOG_TAG = "PhotoSetsFragment";

    /**
     * Used to display a summary of photo sets to the user.
     */
    private GridView photoSetsGrid;

    /**
     * Task used to retrieve the photo sets from the API server.
     */
    private GetPhotoSetsTask getPhotoSetsTask;

    /**
     * Used to flip between the main grid view of photo sets & a particular
     * photo set view.
     */
    private ViewFlipper flipper;

    /**
     * Adapter which backs this view.
     */
    private PhotoSetsAdapter adapter;

    /**
     * View for handling events related to a particular photo set.
     */
    private PhotoSetDetailView photoSetDetailView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photosets, container, false);
        photoSetsGrid = (GridView) v.findViewById(R.id.photosets_grid);
        photoSetsGrid.setOnItemClickListener(this);
        return v;
    }

    @Override
    public String getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_PHOTOSETS_ID;
    }

    @Override
    public void onTabSelected() {
        if (getActivity() != null && getPhotoSetsTask == null) {
            toggleLoading(true, photoSetsGrid);
            getPhotoSetsTask = new GetPhotoSetsTask();
            getPhotoSetsTask.execute(((KrissyTosiApplication) getActivity().getApplication())
                    .getApiClient());
        } else if (adapter != null && adapter.getCount() > 0) {
            toggleLoading(false, getActivity().findViewById(R.id.photoset_flipper));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPhotoSetsTask != null) {
            getPhotoSetsTask.cancel(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        FragmentHelper.toggleFlipper(false, flipper, getActivity());
        PhotoSet photoSet = adapter.getItem(position);
        populatePhotoSet(photoSet);
    }

    private void populatePhotoSet(PhotoSet photoSet) {
        if (photoSetDetailView == null) {
            photoSetDetailView = new PhotoSetDetailView();
        }
        photoSetDetailView.setBaseView(getView());
        photoSetDetailView.setPhotoSet(photoSet);
        photoSetDetailView.setContext(getActivity());
        photoSetDetailView.buildPage();
    }

    protected void buildView(List<PhotoSet> photoSets) {
        flipper = (ViewFlipper) getActivity().findViewById(R.id.photoset_flipper);
        if (adapter == null) {
            adapter = new PhotoSetsAdapter(getActivity(), R.layout.photoset_detail_view,
                    (ArrayList<PhotoSet>) photoSets);
            photoSetsGrid.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        toggleLoading(false, photoSetsGrid);
    }

    /**
     * Callback executed when the photo sets API response has returned.
     * 
     * @param photoSets the list of photo sets from the server (or a list of
     *            errors detailing why the photo sets were not available).
     */
    protected void onGetPhotoSets(List<PhotoSet> photoSets) {
        // check to see that we actually have *a* photo set back from the API
        // server
        if (photoSets.size() > 0) {
            // check to see that the first photo set isn't an error (is there a
            // better way to communicate these errors?)
            PhotoSet photoSet = photoSets.get(0);
            if (photoSet.getErrorCode() == -1 && photoSet.getErrorDescription() == null) {
                Log.d(LOG_TAG, "Retrieved at least one photo set from the server");
                buildView(photoSets);
            } else {
                handlePhotoSetsApiError(photoSet);
            }
        } else {
            PhotoSet photoSet = new PhotoSet();
            photoSet.setErrorCode(ApiConstants.NO_PHOTOSETS);
            photoSet.setErrorDescription(ApiConstants.NO_PHOTOSETS_DESCRIPTION);
            handlePhotoSetsApiError(photoSet);
        }
    }

    /**
     * Callback executed when we fail to retrieve the photo sets from the API.
     * 
     * @param errorPhotoSet
     */
    protected void handlePhotoSetsApiError(PhotoSet errorPhotoSet) {
        Log.d(LOG_TAG, "Photo Set Failure: " + errorPhotoSet.getErrorDescription());
        toggleNoNetwork(true, photoSetsGrid);
    }

    /**
     * Simple AsynTask to retrieve the list of photo sets from the API server.
     */
    private class GetPhotoSetsTask extends
            AsyncTask<ApiClient, Void, List<PhotoSet>> {

        @Override
        protected List<PhotoSet> doInBackground(ApiClient... params) {
            ApiClient apiClient = params[0];
            return apiClient.getPhotoSetService().getPhotoSets();
        }

        @Override
        protected void onPostExecute(List<PhotoSet> result) {
            onGetPhotoSets(result);
            getPhotoSetsTask = null;
        }
    }
}
