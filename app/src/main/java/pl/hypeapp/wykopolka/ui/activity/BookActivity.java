package pl.hypeapp.wykopolka.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.internal.TiPresenterProvider;
import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.hypeapp.wykopolka.App;
import pl.hypeapp.wykopolka.R;
import pl.hypeapp.wykopolka.model.Book;
import pl.hypeapp.wykopolka.presenter.BookPresenter;
import pl.hypeapp.wykopolka.ui.listener.AppBarStateChangeListener;
import pl.hypeapp.wykopolka.view.BookView;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class BookActivity extends CompositeActivity implements BookView {
    private static final String EMPTY_STRING = "";
    private static final String WYKOPOLKA_IMG_HOST = App.WYKOPOLKA_IMG_HOST;
    private static final int BOOK_UNWISHLISTED = 0;
    private static final int BOOK_WISHLISTED = 1;
    private static final int BOOK_WISHLISTING_DISABLED = 2;
    public static final int BOOK_ID_INDEX = 0;
    public static final int BOOK_TITLE_INDEX = 1;
    public static final int BOOK_COVER_INDEX = 2;
    private String mBookTitle;
    private boolean isSearchViewShown = false;
    private AppBarStateChangeListener.State mState;
    private SmallBang mSmallBang;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.fab_add_to_wishlist)
    FloatingActionButton mFabButton;
    @BindView(R.id.search_status_bar)
    View searchStatusBar;
    @BindViews({R.id.tv_author, R.id.tv_description, R.id.tv_genre})
    List<TextView> mBookInfoTextViews;
    @BindView(R.id.iv_book_cover)
    ImageView mBookCover;

    private final TiActivityPlugin<BookPresenter, BookView> mPresenterPlugin =
            new TiActivityPlugin<>(new TiPresenterProvider<BookPresenter>() {
                @NonNull
                @Override
                public BookPresenter providePresenter() {
                    String accountKey = App.readFromPreferences(BookActivity.this, "user_account_key", null);
                    String bookId = intentExtra().get(BOOK_ID_INDEX);
                    return new BookPresenter(accountKey, bookId);
                }
            });

    public BookActivity() {
        addPlugin(mPresenterPlugin);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        mToolbar = initToolbar();
        mSmallBang = SmallBang.attach2Window(this);

        mBookTitle = intentExtra().get(BOOK_TITLE_INDEX);
        mCollapsingToolbarLayout.setTitle(mBookTitle);
        Glide.with(this).load(WYKOPOLKA_IMG_HOST + intentExtra().get(BOOK_COVER_INDEX)).into(mBookCover);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_book, menu);

        searchStatusBar.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        MenuItem item = menu.findItem(R.id.action_search);

        mSearchView.setMenuItem(item);
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchStatusBar.setVisibility(View.VISIBLE);
                if (mState == AppBarStateChangeListener.State.IDLE || mState == AppBarStateChangeListener.State.EXPANDED) {
                    mCollapsingToolbarLayout.setTitle(mBookTitle);
                } else if (mState == AppBarStateChangeListener.State.COLLAPSED) {
                    mCollapsingToolbarLayout.setTitle(EMPTY_STRING);
                }
                isSearchViewShown = true;
            }

            @Override
            public void onSearchViewClosed() {
                searchStatusBar.setVisibility(View.GONE);
                mCollapsingToolbarLayout.setTitle(mBookTitle);
                isSearchViewShown = false;
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                mState = state;
                if (state == State.EXPANDED || state == State.IDLE) {
                    mCollapsingToolbarLayout.setTitle(mBookTitle);
                } else if (state == State.COLLAPSED && isSearchViewShown) {
                    mCollapsingToolbarLayout.setTitle(EMPTY_STRING);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.e("MENU", " SEARCH ACTION");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setBookInfo(Book book) {
        mBookInfoTextViews.get(0).setText(book.getAuthor());
        mBookInfoTextViews.get(1).setText(book.getDesc());
        mBookInfoTextViews.get(2).setText(book.getGenre());
    }

    @Override
    public void setBookCover(String coverUrl) {
        if (mBookCover.getDrawable() == null) {
            Glide.with(this)
                    .load(WYKOPOLKA_IMG_HOST + coverUrl)
                    .into(mBookCover);
        }
    }

    @Override
    public void setBookOwner(String addedBy, String ownedBy) {

    }

    @Override
    public void setWishlistStatus(boolean status) {

    }

    @Override
    public void editBookDescription() {

    }

    @Override
    public void loadPdfBookCard() {

    }

    @Override
    @OnClick(R.id.fab_add_to_wishlist)
    public void addBookToWishlist() {
        mSmallBang.bang(mFabButton, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                mFabButton.setImageResource(R.drawable.ic_favorite_white_36dp);
            }

            @Override
            public void onAnimationEnd() {

            }
        });
        showSnackbarBookWishlistedSuccesful();
    }

    @Override
    public void showSnackbarBookWishlistedSuccesful() {
        Snackbar snackbar = Snackbar.make(mFabButton, R.string.message_added_to_wishlist, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.action_go_to_wishlist, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to wish list
            }
        }).show();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Toolbar initToolbar() {
        mToolbar.setTitle(EMPTY_STRING);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return mToolbar;
    }

    private List<String> intentExtra() {
        Intent intent = getIntent();
        List<String> extra = new ArrayList<>();
        extra.add(intent.getStringExtra("BOOK_ID"));
        extra.add(intent.getStringExtra("BOOK_TITLE"));
        extra.add(intent.getStringExtra("BOOK_COVER"));
        return extra;
    }
}

