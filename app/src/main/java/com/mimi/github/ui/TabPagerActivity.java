package com.mimi.github.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;

import com.github.kevinsawicki.wishlist.ViewUtils;
import com.mimi.github.R;

/**
 * Created by zwb on 15-10-15.
 */
public abstract class TabPagerActivity<V extends PagerAdapter & FragmentProvider>
        extends PagerActivity {

    /**
     * View pager
     */
    protected ViewPager pager;

    /**
     * Tab layout
     */
    protected TabLayout tabLayout;

    /**
     * Pager adapter
     */
    protected V adapter;

    @Override
    public void onPageSelected(final int position) {
        super.onPageSelected(position);

        setCurrentItem(position);
    }

    /**
     * Create pager adapter
     *
     * @return pager adapter
     */
    protected abstract V createAdapter();

    /**
     * Get title for position
     *
     * @param position
     * @return title
     */
    protected String getTitle(final int position) {
        return adapter.getPageTitle(position).toString();
    }

    /**
     * Get icon for position
     *
     * @param position
     * @return icon
     */
    protected String getIcon(final int position) {
        return null;
    }

    /**
     * Set tab and pager as gone or visible
     *
     * @param gone
     * @return this activity
     */
    protected TabPagerActivity<V> setGone(boolean gone) {
        ViewUtils.setGone(tabLayout, gone);
        ViewUtils.setGone(pager, gone);
        return this;
    }

    /**
     * Set current item to new position
     * <p>
     * This is guaranteed to only be called when a position changes and the
     * current item of the pager has already been updated to the given position
     * <p>
     * Sub-classes may override this method
     *
     * @param position
     */
    protected void setCurrentItem(final int position) {
        // Intentionally left blank
    }


    protected int getContentView(){
        return R.layout.page_with_tabs;
    }

    private void createPager() {
        adapter = createAdapter();
        invalidateOptionsMenu();
        pager.setAdapter(adapter);
    }

    /**
     * Create tab using information from current adapter
     * <p>
     * This can be called when the tabs changed but must be called after an
     * initial call to {@link #configureTabPager()}
     */
    protected void createTabs() {
        tabLayout.setupWithViewPager(pager);
    }

    /**
     * Configure tabs and pager
     */
    protected void configureTabPager() {
        if (adapter == null) {
            createPager();
            createTabs();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());
        pager = (ViewPager) findViewById(R.id.vp_pages);
        pager.setOnPageChangeListener(this);
        tabLayout = (TabLayout) findViewById(R.id.th_tabs);
    }

    @Override
    protected FragmentProvider getProvider() {
        return adapter;
    }


}
