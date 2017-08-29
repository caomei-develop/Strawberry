package com.cm.strawberry.base;

import static android.support.v7.widget.RecyclerView.LayoutManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cm.strawberry.R;
import com.cm.strawberry.listener.CustomItemRecyclerClickListener;
import com.cm.strawberry.util.Utils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by zhouwei on 17-8-5.
 */

public abstract class AbsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener, RecyclerArrayAdapter.OnItemViewClickListener{
    protected EasyRecyclerView recyclerView;
    protected RecyclerArrayAdapter adapter;
    private boolean alreadyToLastPage = false;
    private int page;
    private boolean isRequesting = false;
    protected ViewGroup contentView;
    protected View headerView;
    protected View footerView;
    protected View toTopView;
    protected int scrollY = 0;
    protected ViewGroup extraContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = getRecyclerViewAdapter();
        setAdapterState();
    }

    protected void setAdapterState() {
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        adapter.setMore(R.layout.view_more, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
            recyclerView = (EasyRecyclerView) contentView.findViewById(R.id.rv_products);
            if (hasRefresh()) {
                recyclerView.setRefreshListener(this);
            }
            recyclerView.setHeaderPrior(isHeaderPrior());
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            recyclerView.setLayoutManager(layoutManager);
            if (needProgress()) {
                recyclerView.setAdapterWithProgress(adapter);
            } else {
                recyclerView.setAdapter(adapter);
            }
            if (getItemDecoration() != null) {
                recyclerView.addItemDecoration(getItemDecoration());
            }

            headerView = getHeaderView(recyclerView);
            if (headerView != null) {
                adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                    @Override
                    public View onCreateView(ViewGroup parent) {
                        if (extraViewsListener != null) {
                            extraViewsListener.onHeaderAdded(headerView);
                        }
                        onHeaderAdded(headerView);
                        return headerView;
                    }

                    @Override
                    public void onBindView(View headerView) {

                    }
                });
            }

            footerView = getFooterView(recyclerView);
            if (footerView != null) {
                adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
                    @Override
                    public View onCreateView(ViewGroup parent) {
                        if (extraViewsListener != null) {
                            extraViewsListener.onFooterAdded(footerView);
                        }
                        onFooterAdded(footerView);
                        return footerView;
                    }

                    @Override
                    public void onBindView(View headerView) {

                    }
                });
            }

            if (recyclerView.getErrorView() != null) {
                recyclerView.getErrorView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.clear();
                        if (needProgress()) {
                            recyclerView.setAdapterWithProgress(adapter);
                            recyclerView.showProgress();
                        } else {
                            recyclerView.setAdapter(adapter);
                        }
                        onClickErrorView(view);
                        loadData();
                    }
                });
            }
            adapter.setOnItemViewClickListener(new CustomItemRecyclerClickListener(this));

            if (hasToTopView()) {
                toTopView = contentView.findViewById(R.id.fab_to_top);
                toTopView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (recyclerView != null) {
                            scrollY = 0;
                            recyclerView.scrollToPosition(0);
                        }
                    }
                });
            }

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    scrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    scrolled(recyclerView, dx, dy);
                }
            });
        }
        extraContainer = (ViewGroup) contentView.findViewById(R.id.extra_container);
        return contentView;
    }

    protected void onHeaderAdded(View headerView) {

    }

    protected void onFooterAdded(View footerView) {

    }

    public void onItemClick(int position) {

    }

    @Override
    public void onItemViewClick(View view, int position) {
        onItemClick(position);
    }

    public boolean needProgress() {
        return true;
    }

    protected boolean isHeaderPrior() {
        return true;
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    protected abstract RecyclerArrayAdapter getRecyclerViewAdapter();

    protected abstract void loadData();

    protected void onClickErrorView(View view) {

    }

    public View getHeaderView(View parent) {
        return null;
    }

    public View getFooterView(View parent) {
        return null;
    }

    public boolean hasRefresh() {
        return true;
    }

    public boolean hasToTopView() {
        return true;
    }

    public void setVisibleHint(boolean isVisibleToUser) {
        super.setVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isRequesting && !alreadyToLastPage && adapter.getCount() <= 0) {
                loadData();
            }
        }
    }
    @Override
    public void onRefresh() {
        page = 0;
        alreadyToLastPage = false;
        adapter.resetEventDelegate();
        recyclerView.setRefreshingImmediately(true);
        loadData();
        if (getActivity() instanceof SwipeRefreshLayout.OnRefreshListener) {
            ((SwipeRefreshLayout.OnRefreshListener) getActivity()).onRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    protected void scrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    protected void scrolled(RecyclerView recyclerView, int dx, int dy) {
        scrollY += dy;
        if (recyclerView != null && recyclerView.computeVerticalScrollOffset() == 0) {
            scrollY = 0;
        }
        if (toTopView != null) {
            if (scrollY > 2 * Utils.getDeviceHeight(getContext())) {
                setToTopViewVisibility(View.VISIBLE);
            } else {
                setToTopViewVisibility(View.GONE);
            }
        }
    }

    protected void setToTopViewVisibility(int visibility) {
        if (toTopView != null) {
            Utils.setViewVisibility(toTopView, visibility);
        }
    }

    protected boolean isRefreshing() {
        return recyclerView == null ? false : recyclerView.isRefreshing();
    }

    public void setRefreshing(boolean isRefreshing) {
        if (recyclerView != null) {
            recyclerView.setRefreshing(isRefreshing);
        }
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isAlreadyToLastPage() {
        return alreadyToLastPage;
    }

    public void setAlreadyToLastPage(boolean alreadyToLastPage) {
        this.alreadyToLastPage = alreadyToLastPage;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return 16;
    }

    public int getMinSizeToShowFooter() {
        return 6;
    }

    public void initScrollY() {
        scrollY = 0;
    }

    public EasyRecyclerView getRecyclerView() {
        return recyclerView;
    }

    private ExtraViewsListener extraViewsListener;

    public void setExtraViewsListener(ExtraViewsListener extraViewsListener) {
        this.extraViewsListener = extraViewsListener;
    }

    public interface ExtraViewsListener {
        public void onHeaderAdded(View headerView);

        public void onFooterAdded(View footerView);
    }
}
