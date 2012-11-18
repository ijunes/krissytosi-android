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
import android.widget.GridView;

import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.fragments.adapters.PortfoliosAdapter;
import com.krissytosi.fragments.views.PortfolioDetailView;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a grid of portfolio summaries.
 */
public class PortfoliosFragment extends BaseFragment {

    private static final String LOG_TAG = "PortfolioFragment";

    /**
     * Used to display a summary of porfolios to the user.
     */
    private GridView portfoliosGrid;

    /**
     * Task used to retrieve the portfolios from the API server.
     */
    private GetPortfoliosTask getPortfoliosTask;

    /**
     * Adapter which backs this view.
     */
    private PortfoliosAdapter adapter;

    /**
     * View for handling events related to a particular portfolio.
     */
    private PortfolioDetailView portfolioDetailView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.portfolios, container, false);
        portfoliosGrid = (GridView) v.findViewById(R.id.portfolios_grid);
        return v;
    }

    @Override
    public String getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_PORTFOLIO_ID;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPortfoliosTask != null) {
            getPortfoliosTask.cancel(true);
        }
    }

    @Override
    public void onTabSelected() {
        if (getActivity() != null && getPortfoliosTask == null) {
            toggleLoading(true, portfoliosGrid);
            getPortfoliosTask = new GetPortfoliosTask();
            getPortfoliosTask.execute(((KrissyTosiApplication) getActivity().getApplication())
                    .getApiClient());
        }
    }

    protected void buildView(List<Portfolio> portfolios) {
        if (adapter == null) {
            adapter = new PortfoliosAdapter(getActivity(), R.layout.portfolio_detail_view,
                    (ArrayList<Portfolio>) portfolios);
            portfoliosGrid.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        toggleLoading(false, portfoliosGrid);
    }

    /**
     * Callback executed when the portfolios API response has returned.
     * 
     * @param portfolios the list of portfolios from the server (or a list of
     *            errors detailing why the portfolios were not available).
     */
    protected void onGetPortfolios(List<Portfolio> portfolios) {
        // check to see that we actually have *a* portfolio back from the API
        // server
        if (portfolios.size() > 0) {
            // check to see that the first portfolio isn't an error (is there a
            // better way to communicate these errors?)
            Portfolio portfolio = portfolios.get(0);
            if (portfolio.getErrorCode() == -1 && portfolio.getErrorDescription() == null) {
                Log.d(LOG_TAG, "Retrieved at least one portfolio from the server");
                buildView(portfolios);
            } else {
                handlePortfolioApiError(portfolio);
            }
        } else {
            Portfolio portfolio = new Portfolio();
            portfolio.setErrorCode(ApiConstants.NO_PORTFOLIOS);
            portfolio.setErrorDescription(ApiConstants.NO_PORTFOLIOS_DESCRIPTION);
            handlePortfolioApiError(portfolio);
        }
    }

    /**
     * Callback executed when we fail to retrieve the portfolios from the API.
     * 
     * @param errorPortfolio
     */
    protected void handlePortfolioApiError(Portfolio errorPortfolio) {
        Log.d(LOG_TAG, "Portfolio Failure: " + errorPortfolio.getErrorDescription());
        toggleNoNetwork(true, portfoliosGrid);
    }

    /**
     * Simple AsynTask to retrieve the list of portfolios from the API server.
     */
    private class GetPortfoliosTask extends
            AsyncTask<ApiClient, Void, List<Portfolio>> {

        @Override
        protected List<Portfolio> doInBackground(ApiClient... params) {
            ApiClient apiClient = params[0];
            return apiClient.getPortfolioService().getPortfolios();
        }

        @Override
        protected void onPostExecute(List<Portfolio> result) {
            onGetPortfolios(result);
            getPortfoliosTask = null;
        }
    }
}
