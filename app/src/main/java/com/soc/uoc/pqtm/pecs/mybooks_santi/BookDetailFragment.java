package com.soc.uoc.pqtm.pecs.mybooks_santi;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.soc.uoc.pqtm.pecs.mybooks_santi.model.BookContent;

import com.squareup.picasso.Picasso;



/**
 * A fragment representing a single book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private BookContent.BookItem mItem;

    private ImageView imageHeader;
    private Activity activity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            // ============ INICI CODI A COMPLETAR ===============
            mItem = BookContent.getBooks().get(getArguments().getInt(ARG_ITEM_ID));

            activity = this.getActivity();



            // ============ FI CODI A COMPLETAR ===============

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
            ((TextView) rootView.findViewById(R.id.book_author)).setText(mItem.author);
            ((TextView) rootView.findViewById(R.id.book_date)).setText(mItem.publicationDate);
            ((TextView) rootView.findViewById(R.id.book_detail)).setText(mItem.description);
            ImageView imageView1 = (ImageView) rootView.findViewById(R.id.book_image);

            imageHeader = (ImageView) activity.findViewById(R.id.image_header);
            if (imageHeader != null) {
                //Only Detail
                imageView1.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(mItem.urlImage).into(imageHeader);
            } else {
                //When in landscape tablet (Two Panes)
                Picasso.with(getActivity()).load(mItem.urlImage).into(imageView1);
            }
        }

        return rootView;
    }
}
