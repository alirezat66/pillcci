package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Category;

/**
 * Created by alireza on 5/27/18.
 */

public class CatAutoCompletePresenter extends RecyclerViewPresenter<Category> {

    protected Adapter adapter;
    public ArrayList<Category> categories = new ArrayList<>();
    public CatAutoCompletePresenter(Context context) {
        super(context);
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        categories = new ArrayList<>(database.categoryDao().listOfCats());
    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<Category> all = categories;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<Category> list = new ArrayList<>();
            for (Category u : all) {
                if (u.getName().toLowerCase().contains(query) ||
                        u.getName().toLowerCase().contains(query)) {
                    list.add(u);

                }
            }
            adapter.setData(list);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Category> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;

            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = ((TextView) itemView.findViewById(R.id.fullname));
            }
        }

        public void setData(List<Category> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 0 : data.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_pill_complite, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (isEmpty()) {

            }else {
                onViewShown();
                holder.root.setVisibility(View.VISIBLE);
                final Category pill = data.get(position);
                holder.fullname.setText(pill.getName());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dispatchClick(pill);
                    }
                });
            }
        }


    }



}
