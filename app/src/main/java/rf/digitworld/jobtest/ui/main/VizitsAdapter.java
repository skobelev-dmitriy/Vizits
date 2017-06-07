package rf.digitworld.jobtest.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rf.digitworld.jobtest.R;
import rf.digitworld.jobtest.data.model.Organization;
import rf.digitworld.jobtest.data.model.Vizit;

public class VizitsAdapter extends RecyclerView.Adapter<VizitsAdapter.RibotViewHolder> {
    private  OnItemClickListener listener;
     public interface OnItemClickListener {
        void onItemClick(Vizit item);
    }
    private List<Vizit> mVizits;
    private int selected_position;

    @Inject
    public VizitsAdapter() {
        mVizits = new ArrayList<>();
    }

    public void setVizits(List<Vizit> vizits) {
        mVizits = vizits;
    }
    public void refresh(){

        for (int i=0; i<getItemCount(); i++){//снимаем выделение

            notifyItemChanged(i);
        }
        /*for (int i=0; i<getItemCount();i++){ //Выделяем
            if(mVizits.get(i).getOrganizationId().equals(orgId)){
                //selected_position=i;
                notifyItemChanged(i);
            }

        }*/


    }
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public RibotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vizit, parent, false);
        return new RibotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RibotViewHolder holder, final int position) {
        Vizit vizit = mVizits.get(position);
        holder.bind(vizit, listener);

            //if(selected_position == position){
        if(vizit.getOrganization().isSelected()||vizit.isSelected()){

                // Here I am just highlighting the background
                holder.itemView.setBackgroundColor(Color.GREEN);
            }else{
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            }


        holder.nameTextView.setText(String.format("%s",
                vizit.getTitle()));

        holder.emailTextView.setText(vizit.getOrganization().getTitle());
     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Updating old as well as new positions

                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);

                // Do your another stuff for your onClick
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mVizits.size();
    }

    class RibotViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_name) TextView nameTextView;
        @Bind(R.id.text_email) TextView emailTextView;

        public RibotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(final Vizit item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
