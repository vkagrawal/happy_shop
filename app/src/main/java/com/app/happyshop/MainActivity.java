package com.app.happyshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.app.happyshop.base.App;
import com.app.happyshop.base.BaseFragment;
import com.app.happyshop.base.BaseNetworkNotifyActivity;
import com.app.happyshop.fragments.CartViewFragment;
import com.app.happyshop.fragments.CategoryDetailsFragment;
import com.app.happyshop.fragments.HomeFragment;
import com.app.happyshop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MainActivity extends BaseNetworkNotifyActivity {

    public static final String FAVOURITE = "favourite";
    public static final String CATEGORIES = "categories";
    public static final String WISHLIST = "wishlist";
    public static final String CART = "cart";
    public static final String SETTINGS = "settings";

    /* Your Tab host */
    private TabHost mTabHost;

    /*Your Tab Widget */
    private TabWidget mTabWidget;

    /* A HashMap of stacks, where we use tab identifier as keys..*/
    private HashMap<String, Stack<Fragment>> mStacks;

    /*Save current tabs identifier in this..*/
    private String mCurrentTab;

    private View getCartView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowBackHome(true);
        setContentView(R.layout.fragments_container);

        /*
         *  Navigation stacks for each tab gets created..
         *  tab identifier is used as key to get respective stack for each tab
         */
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(FAVOURITE, new Stack<Fragment>());
        mStacks.put(CATEGORIES, new Stack<Fragment>());
        mStacks.put(WISHLIST, new Stack<Fragment>());
        mStacks.put(CART, new Stack<Fragment>());
        mStacks.put(SETTINGS, new Stack<Fragment>());

        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(listener);
        mTabHost.setup();

        initializeTabs();
        setCurrentTab(2);
    }

    private View createTabView(final int image, int title) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabIcon);
        TextView tabName = (TextView) view.findViewById(R.id.tabName);
        TextView cartItemBadge = (TextView) view.findViewById(R.id.cartItemBadge);
        imageView.setImageResource(image);
        tabName.setText(title);
        return view;
    }

    public void initializeTabs() {
        /* Setup your tab icons and content views.. Nothing special in this..*/
        TabHost.TabSpec spec = mTabHost.newTabSpec(FAVOURITE);
        mTabHost.setCurrentTab(-3);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.favourite_action, R.string.favourite));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(CATEGORIES);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.category_action, R.string.categories));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(WISHLIST);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.wishlist_action, R.string.wishlist));
        mTabHost.addTab(spec);

        getCartView = createTabView(R.drawable.cart_action, R.string.cart);
        spec = mTabHost.newTabSpec(CART);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(getCartView);
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(SETTINGS);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.settings_action, R.string.settings));
        mTabHost.addTab(spec);


    }


    /*Comes here when user switch tab, or we do programmatically*/
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {
        /*Set current tab..*/
            mCurrentTab = tabId;

            if (mStacks.get(tabId).size() == 0) {
          /*
           *    First time this tab is selected. So add first fragment of that tab.
           *    Dont need animation, so that argument is false.
           *    We are adding a new fragment which is not present in stack. So add to stack is true.
           */
                if (tabId.equals(FAVOURITE)) {
                    selectLastSelectedTab();
                } else if (tabId.equals(CATEGORIES)) {
                    pushFragments(tabId, HomeFragment.create(), true, true);
                } else if (tabId.equals(WISHLIST)) {
                    selectLastSelectedTab();
                } else if (tabId.equals(CART)) {
                    // pushFragments(tabId, CartViewFragment.create(), true, true);
                } else if (tabId.equals(SETTINGS)) {
                    selectLastSelectedTab();
                }
            } else {
          /*
           *    We are switching tabs, and target tab is already has atleast one fragment.
           *    No need of animation, no need of stack pushing. Just show the target fragment
           */
                pushFragments(tabId, mStacks.get(tabId).lastElement(), false, false);
            }
        }
    };

    private void selectLastSelectedTab() {
        if (currentFragment instanceof CartViewFragment) {
            mTabHost.setCurrentTab(3);
        } else {
            mTabHost.setCurrentTab(1);
        }
    }


    /* Might be useful if we want to switch tab programmatically, from inside any of the fragment.*/
    public void setCurrentTab(int val) {
        mTabHost.setCurrentTab(val);
    }


    /*
     *      To add fragment to a tab.
     *  tag             ->  Tab identifier
     *  fragment        ->  Fragment to show, in tab identified by tag
     *  shouldAnimate   ->  should animate transaction. false when we switch tabs, or adding first fragment to a tab
     *                      true when when we are pushing more fragment into navigation stack.
     *  shouldAdd       ->  Should add to fragment navigation stack (mStacks.get(tag)). false when we are switching tabs (except for the first time)
     *                      true in all other cases.
     */
    public void pushFragments(String tag, Fragment fragment, boolean shouldAnimate, boolean shouldAdd) {
        if (shouldAdd)
            mStacks.get(tag).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate)
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }


    public void popFragments() {
      /*
       *    Select the second last fragment in current tab's stack..
       *    which will be shown after the fragment transaction given below
       */
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

      /*pop current fragment from stack.. */
        mStacks.get(mCurrentTab).pop();

      /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        if (mStacks.get(mCurrentTab).size() <= 1) {
            super.onBackPressed();  // or call finish..
        } else {
            popFragments();
        }
    }

    @Override
    public void setCurrentFrag(BaseFragment frag) {
        super.setCurrentFrag(frag);
        onFragmentChangeAction();
    }

    private void onFragmentChangeAction() {
        updateTabBarVisiblity();
        updateBadge();
    }

    private void updateBadge() {

        new RetriveProducts().execute();
    }

    class RetriveProducts extends AsyncTask<Void, Void, ArrayList<Product>> {

        @Override
        protected ArrayList<Product> doInBackground(Void... voids) {
            try {
                ArrayList<Product> productArrayList = (ArrayList<Product>) Product.getInstance().findAllData();
                return productArrayList;
            } catch (Exception e) {
                e.printStackTrace();
                App.printToast(R.string.error_to_retrive);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productArrayList) {
            super.onPostExecute(productArrayList);
            TextView badgeView = getCartView.findViewById(R.id.cartItemBadge);
            if (productArrayList != null && productArrayList.size() > 0 && badgeView != null) {
                badgeView.setText("" + productArrayList.size());
                badgeView.setVisibility(View.VISIBLE);
            } else {
                if (badgeView != null) {
                    badgeView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void updateTabBarVisiblity() {
        if (currentFragment instanceof CategoryDetailsFragment) {
            mTabWidget.setVisibility(View.GONE);
        } else {
            mTabWidget.setVisibility(View.VISIBLE);
        }
    }

    /*
         *   Imagine if you wanted to get an image selected using ImagePicker intent to the fragment. Ofcourse I could have created a public function
         *  in that fragment, and called it from the activity. But couldn't resist myself.
         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mStacks.get(mCurrentTab).size() == 0) {
            return;
        }

        /*Now current fragment on screen gets onActivityResult callback..*/
        mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void notifyOnNetworkStateChange(boolean isNetworkAvailable) {
        currentFragment.notifyNetworkStateChange(isNetworkAvailable);
    }

    @Override
    protected void initNetworkMessageTextView() {
        networkMessageTextView = (TextView) findViewById(R.id.network_message_text_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
