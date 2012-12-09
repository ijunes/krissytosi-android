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
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.fragments.adapters.PhotoSetsAdapter;
import com.krissytosi.fragments.views.PhotoSetDetailView;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Displays a grid of photo sets.
 */
public class PhotoSetsFragment extends BaseFragment implements OnItemClickListener {

    private static final String LOG_TAG = "PhotoSetsFragment";

    /**
     * Used to display a summary of photo sets to the user.
     */
    private GridView gridView;

    /**
     * Task used to retrieve the photo sets from the API server.
     */
    private GetPhotoSetsTask getPhotoSetsTask;

    /**
     * Data structure of tasks for retrieving photos for a particular photo set.
     */
    private final List<GetPhotosTask> getPhotosTasks = new ArrayList<GetPhotosTask>();

    /**
     * Adapter which backs this view.
     */
    private PhotoSetsAdapter adapter;

    /**
     * View for handling events related to a particular photo set.
     */
    private PhotoSetDetailView photoSetDetailView;

    /**
     * Photo sets which back this view.
     */
    private List<PhotoSet> photoSets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photosets, container, false);
        ScrollView photoSetDetailView = (ScrollView) v.findViewById(R.id.photoset_detail_view);
        photoSetDetailView.setSmoothScrollingEnabled(true);
        photoSetDetailView.setOnTouchListener(new OnSwipeTouchListener() {

            @Override
            public void onSwipeRight() {
                toggleGridView(true);
            }
        });
        return v;
    }

    @Override
    public String getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_PHOTOSETS_ID;
    }

    @Override
    public void onTabSelected() {
        if (getActivity() != null && getPhotoSetsTask == null) {
            toggleLoading(true, getView().findViewById(R.id.photoset_flipper));
            getPhotoSetsTask = new GetPhotoSetsTask();
            getPhotoSetsTask.execute(((KrissyTosiApplication) getActivity().getApplication())
                    .getApiClient());
        } else if (adapter != null && adapter.getCount() > 0) {
            toggleLoading(false, getView().findViewById(R.id.photoset_flipper));
        }
    }

    @Override
    public void onCurrentTabClicked() {
        super.onCurrentTabClicked();
        if (!isGridViewShowing()) {
            toggleGridView(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cleanupPhotoTasks();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        toggleGridView(false);
        PhotoSet photoSet = adapter.getItem(position);
        populatePhotoSet(photoSet);
        getView().findViewById(R.id.photoset_detail_view).setVisibility(View.VISIBLE);
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

    protected void buildPhotoSets(List<PhotoSet> photoSets) {
        this.photoSets = photoSets;
        cleanupPhotoTasks();
        // populate a new batch of GetPhotosTask objects & execute 'em all
        for (PhotoSet photoSet : photoSets) {
            GetPhotosTask task = new GetPhotosTask();
            task.execute(((KrissyTosiApplication) getActivity().getApplication()).getApiClient(),
                    photoSet.getId());
            getPhotosTasks.add(task);
        }
    }

    private boolean isGridViewShowing() {
        return gridView != null && gridView.getVisibility() == View.VISIBLE;
    }

    private void toggleGridView(boolean show) {
        FragmentHelper.toggleFlipper(show,
                (ViewFlipper) getView().findViewById(R.id.photoset_flipper),
                getActivity(), getFragmentIdentifier());
        if (!show) {
            getView().findViewById(R.id.photoset_detail_view).scrollTo(0, 0);
        }
    }

    protected void buildView() {
        flipperId = R.id.photoset_flipper;
        gridView = (GridView) getActivity().findViewById(R.id.photosets_grid);
        gridView.setOnItemClickListener(this);
        if (adapter == null) {
            adapter = new PhotoSetsAdapter(getActivity(), R.layout.photoset_detail_view,
                    (ArrayList<PhotoSet>) photoSets);
        }
        if (gridView.getAdapter() == null) {
            gridView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        toggleLoading(false, getActivity().findViewById(R.id.photoset_flipper));
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
                buildPhotoSets(photoSets);
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
     * Callback executed when we get the list of photos back from the API
     * server.
     * 
     * @param photos a list of {@link Photo} objects corresponding to the
     *            photoSet member variable.
     */
    protected void onGetPhotos(List<Photo> photos) {
        String photoSetId = "";
        if (photos.size() != 0) {
            photoSetId = photos.get(0).getPhotoSetId();
        }
        // check to see whether we can go ahead and build the view.
        Iterator<GetPhotosTask> iterator = getPhotosTasks.iterator();
        while (iterator.hasNext()) {
            GetPhotosTask task = iterator.next();
            if (photoSetId.equalsIgnoreCase(task.getPhotoSetId())) {
                iterator.remove();
            }
        }
        for (PhotoSet photoSet : photoSets) {
            if (photoSet.getId() != null && photoSet.getId().equalsIgnoreCase(photoSetId)) {
                photoSet.setPhotos(photos);
                // see if there's a primary photo id
                int primaryPhotoIndex = 0;
                for (int i = 0, l = photos.size(); i < l; i++) {
                    if (photos.get(i).getIsPrimary() == 1) {
                        primaryPhotoIndex = i;
                        break;
                    }
                }
                photoSet.setIndexOfPrimaryPhoto(primaryPhotoIndex);
            }
        }
        if (getPhotosTasks.size() == 0) {
            buildView();
        }
    }

    /**
     * Callback executed when we fail to retrieve the photo sets from the API.
     * 
     * @param errorPhotoSet
     */
    protected void handlePhotoSetsApiError(PhotoSet errorPhotoSet) {
        Log.d(LOG_TAG, "Photo Set Failure: " + errorPhotoSet.getErrorDescription());
        toggleNoNetwork(true, gridView);
    }

    private void cleanupPhotoTasks() {
        if (getPhotoSetsTask != null) {
            getPhotoSetsTask.cancel(true);
        }
        // make sure we're starting with a clean slate.
        if (getPhotosTasks.size() > 0) {
            for (GetPhotosTask task : getPhotosTasks) {
                task.cancel(true);
            }
            getPhotosTasks.clear();
        }
    }

    /**
     * Simple AsynTask to retrieve the list of photo sets from the API server.
     */
    private class GetPhotoSetsTask extends
            AsyncTask<ApiClient, Void, List<PhotoSet>> {

        @Override
        protected List<PhotoSet> doInBackground(ApiClient... params) {
            ApiClient apiClient = params[0];
            return apiClient.getPhotoService().getPhotoSets();
        }

        @Override
        protected void onPostExecute(List<PhotoSet> result) {
            onGetPhotoSets(result);
            getPhotoSetsTask = null;
        }
    }

    /**
     * Simple AsynTask to retrieve the photo urls for a photo set.
     */
    private class GetPhotosTask extends
            AsyncTask<Object, Void, List<Photo>> {

        private String photoSetId;

        @Override
        protected List<Photo> doInBackground(Object... params) {
            ApiClient apiClient = (ApiClient) params[0];
            photoSetId = (String) params[1];
            return apiClient.getPhotoService().getPhotos(photoSetId);
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            onGetPhotos(photos);
        }

        // Getters/Setters

        public String getPhotoSetId() {
            return photoSetId;
        }
    }
}
