/*
   Copyright 2012 - 2014 Sean O' Shea

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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    // orientation flag handlers

    private static final String PHOTOSETS = "com.krissytosi.fragments.PhotoSetsFragment.PHOTOSETS";
    private static final String CURRENT_PHOTOSET_ID = "com.krissytosi.fragments.PhotoSetsFragment.CURRENT_PHOTOSET_ID";
    private String currentPhotoSetId = "";

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
    private ArrayList<PhotoSet> photoSets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photosets, container, false);
        ScrollView photoSetDetailView = (ScrollView) v.findViewById(R.id.photoset_detail_view);
        photoSetDetailView.setSmoothScrollingEnabled(true);
        OnSwipeTouchListener listener = new OnSwipeTouchListener() {

            @Override
            public void onSwipeRight() {
                toggleGridView(true);
            }

            @Override
            public void onLongPressDetected(MotionEvent motionEvent) {
                super.onLongPressDetected(motionEvent);
                longPressDetected();
            }
        };
        listener.setIsLongpressEnabled(true);
        photoSetDetailView.setOnTouchListener(listener);
        if (savedInstanceState != null) {
            currentPhotoSetId = savedInstanceState.getString(CURRENT_PHOTOSET_ID);
            if (savedInstanceState.containsKey(PHOTOSETS)) {
                photoSets = savedInstanceState.getParcelableArrayList(PHOTOSETS);
            }
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // ensure that the correct photo set view shows up again after an
        // orientation change
        if (!isGridViewShowing()) {
            outState.putString(CURRENT_PHOTOSET_ID, currentPhotoSetId);
        }
        if (hasPhotoSets()) {
            outState.putParcelableArrayList(PHOTOSETS, photoSets);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_PHOTOSETS_POSITION;
    }

    @Override
    public void onTabSelected() {
        if (getActivity() != null && getPhotoSetsTask == null) {
            // check to see whether we already have the photoSets as a result of
            // an orientation change
            if (hasPhotoSets()) {
                currentPhotoSetId = "";
                buildView();
            } else {
                // no photo sets - go grab 'em
                toggleLoading(true, getView().findViewById(R.id.photoset_flipper));
                getPhotoSetsTask = new GetPhotoSetsTask();
                getPhotoSetsTask.execute(((KrissyTosiApplication) getActivity().getApplication())
                        .getApiClient());
            }
        } else if (adapter != null && adapter.getCount() > 0) {
            toggleLoading(false, getView().findViewById(R.id.photoset_flipper));
        }
    }

    @Override
    public void onCurrentTabClicked() {
        super.onCurrentTabClicked();
        if (!isGridViewShowing()) {
            toggleGridView(true);
            currentPhotoSetId = "";
            FragmentHelper.handleDetailViewBeforeDetatched(photoSetDetailView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cleanupPhotoTasks();
    }

    @Override
    public void beforeDetatched() {
        super.beforeDetatched();
        FragmentHelper.handleDetailViewBeforeDetatched(photoSetDetailView);
        photoSetDetailView = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        handleOnItemClick(position);
    }

    @Override
    public void onLongPressDetected() {
        super.onLongPressDetected();
        if (longPressDetected()) {
            photoSetDetailView.getViewPager().performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS);
        }
    }

    /**
     * Executed when a long press is detected on one of the photos in the photo
     * set.
     */
    protected boolean longPressDetected() {
        boolean longPressDetected = false;
        // check to see what photo set is currently selected
        if (hasPhotoSets() && hasDetailView() && !isGridViewShowing()
                && photoSetDetailView.getPhotoSet() != null) {
            PhotoSet photoSet = photoSetDetailView.getPhotoSet();
            int currentPhotoIndex = photoSetDetailView.getViewPager().getCurrentItem();
            if (photoSet != null && photoSet.getPhotos() != null
                    && photoSet.getPhotos().size() > currentPhotoIndex) {
                longPressDetected = true;
                Photo photo = photoSet.getPhotos().get(currentPhotoIndex);
                if (getActivity() != null) {
                    ((KrissyTosiApplication) getActivity().getApplication()).getTracking()
                            .mediaShared(photoSet.getTitle());
                }
                Intent intent = FragmentHelper.createShareUrlIntent(photo.getUrlOriginal());
                PhotoSetsFragment.this.startActivity(Intent.createChooser(intent, getResources()
                        .getString(R.string.share_this)));
            }
        }
        return longPressDetected;
    }

    private void handleOnItemClick(int position) {
        toggleGridView(false);
        PhotoSet photoSet = adapter.getItem(position);
        if (getActivity() != null && photoSet.getTitle() != null) {
            ((KrissyTosiApplication) getActivity().getApplication()).getTracking()
                    .trackPhotoSetChange(photoSet.getTitle());
        }
        populatePhotoSet(photoSet);
        getView().findViewById(R.id.photoset_detail_view).setVisibility(View.VISIBLE);
        currentPhotoSetId = photoSet.getId();
        FragmentHelper.setTitle(getActivity(), photoSet.getTitle());
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
        this.photoSets = (ArrayList<PhotoSet>) photoSets;
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
        } else {
            FragmentHelper.setTitle(getActivity(), getResources().getString(R.string.app_name));
            if (gridView != null) {
                gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    protected void buildView() {
        flipperId = R.id.photoset_flipper;
        gridView = (GridView) getView().findViewById(R.id.photosets_grid);
        gridView.setOnItemClickListener(this);
        if (adapter == null) {
            adapter = new PhotoSetsAdapter(getActivity(), R.layout.photoset_detail_view,
                    photoSets);
        }
        if (gridView.getAdapter() == null) {
            gridView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        FragmentHelper.setTitle(getActivity(), getResources().getString(R.string.app_name));
        toggleLoading(false, getView().findViewById(R.id.photoset_flipper));
        navigateToCurrentPhotoSet();
    }

    /**
     * Delegates to <code>handleOnItemClick</code> if the
     * <code>currentPhotoSetId</code> member variable is set to something
     * reasonable.
     */
    private void navigateToCurrentPhotoSet() {
        if (hasDetailView()) {
            // deal with remembering the previous photo set after an orientation
            // change
            for (int i = 0, l = photoSets.size(); i < l; i++) {
                PhotoSet photoSet = photoSets.get(i);
                if (currentPhotoSetId.equalsIgnoreCase(photoSet.getId())) {
                    handleOnItemClick(i);
                    break;
                }
            }
        }
    }

    /**
     * Checks to see whether there are photo sets available.
     * 
     * @return boolean indicating that there is at least one photo set
     *         available.
     */
    private boolean hasPhotoSets() {
        boolean hasPhotoSets = photoSets != null && !photoSets.isEmpty();
        if (hasPhotoSets) {
            // make sure that the first photo set does not include an error
            hasPhotoSets = photoSets.get(0).getErrorCode() == -1;
        }
        return hasPhotoSets;
    }

    /**
     * Checks to see whether the user is currently in a viable detail view.
     * 
     * @return boolean indicating that the currentPhotoSetId is set.
     */
    private boolean hasDetailView() {
        return photoSetDetailView != null && !"".equalsIgnoreCase(currentPhotoSetId);
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
        if (!photoSets.isEmpty()) {
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
        if (photos != null) {
            if (!photos.isEmpty()) {
                photoSetId = photos.get(0).getPhotoSetId();
            }
            // check to see whether we can go ahead and build the view.
            Iterator<GetPhotosTask> iterator = getPhotosTasks.iterator();
            while (iterator.hasNext()) {
                GetPhotosTask task = iterator.next();
                if (photoSetId != null && photoSetId.equalsIgnoreCase(task.getPhotoSetId())) {
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
            if (getPhotosTasks.isEmpty()) {
                buildView();
            }
        } else {

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
        if (!getPhotosTasks.isEmpty()) {
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
