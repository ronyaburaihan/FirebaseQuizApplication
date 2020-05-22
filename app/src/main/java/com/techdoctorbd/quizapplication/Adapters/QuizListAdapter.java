package com.techdoctorbd.quizapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techdoctorbd.quizapplication.Interfaces.QuizItemClickListener;
import com.techdoctorbd.quizapplication.Models.QuizItem;
import com.techdoctorbd.quizapplication.R;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<QuizItem> quizItems;
    private QuizItemClickListener itemClickListener;

    public QuizListAdapter(QuizItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setQuizItems(List<QuizItem> quizItems) {
        this.quizItems = quizItems;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.title.setText(quizItems.get(position).getTitle());
        String listDesc = quizItems.get(position).getDescription();
        if (listDesc.length() > 150){
            listDesc = listDesc.substring(0,150)+" ...";
        }
        holder.description.setText(listDesc);
        String imageUrl = quizItems.get(position).getImageUrl();
        Glide.with(holder.image.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.image);
        holder.difficulty.setText(quizItems.get(position).getDifficulty());
    }

    @Override
    public int getItemCount() {
        if (quizItems == null)
            return 0;
        else
            return quizItems.size();
    }

     class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title,description,difficulty;
        private ImageView image;
        private Button viewButton;

        QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_single_list_item);
            title = itemView.findViewById(R.id.title_single_list_item);
            description = itemView.findViewById(R.id.description_single_list_item);
            difficulty = itemView.findViewById(R.id.difficulty_single_list_item);
            viewButton = itemView.findViewById(R.id.view_button_single_list_item);

            viewButton.setOnClickListener(this);
        }

         @Override
         public void onClick(View view) {
             itemClickListener.onQuizItemClicked(getAdapterPosition(),image,title);
         }
     }
}
