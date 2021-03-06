package pl.hypeapp.wykopolka.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.hypeapp.wykopolka.App;
import pl.hypeapp.wykopolka.R;
import pl.hypeapp.wykopolka.model.Book;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.BooksRecyclerHolder> {
    private static final String WYKOPOLKA_IMG_HOST = App.WYKOPOLKA_IMG_HOST;
    private final PublishSubject<BooksRecyclerHolder> onClickSubject = PublishSubject.create();
    private LayoutInflater mLayoutInflater;
    private List<Book> mDataSet = Collections.emptyList();
    private Context mContext;

    public BooksRecyclerAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public BooksRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.book_row, parent, false);
        return new BooksRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(BooksRecyclerHolder booksRecyclerHolder, int position) {
        Book current = mDataSet.get(position);
        final BooksRecyclerHolder holder = booksRecyclerHolder;
        holder.bookTitle.setText(current.getTitle());
        holder.bookAuthor.setText(current.getAuthor());
        Glide.with(mContext)
                .load(WYKOPOLKA_IMG_HOST + current.getCover())
                .placeholder(R.drawable.default_book_cover)
                .error(R.drawable.default_book_cover)
                .override(300, 400)
                .thumbnail(0.9f)
                .into(holder.bookThumbnail);
        holder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(holder);
            }
        });
    }

    public Observable<BooksRecyclerHolder> getOnBookClicks() {
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setData(List<Book> books) {
        mDataSet = books;
        notifyDataSetChanged();
    }

    public class BooksRecyclerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_book_thumbnail) public ImageView bookThumbnail;
        @BindView(R.id.gradient) public ImageView gradient;
        @BindView(R.id.tv_book_title) TextView bookTitle;
        @BindView(R.id.tv_book_author) TextView bookAuthor;
        @BindView(R.id.card_view_added_book) CardView cardView;
        @BindView(R.id.ripple) MaterialRippleLayout materialRippleLayout;

        BooksRecyclerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
