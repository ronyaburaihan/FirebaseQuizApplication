package com.techdoctorbd.quizapplication.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techdoctorbd.quizapplication.Adapters.QuizListAdapter;
import com.techdoctorbd.quizapplication.Interfaces.QuizItemClickListener;
import com.techdoctorbd.quizapplication.Models.QuizItem;
import com.techdoctorbd.quizapplication.Models.QuizListViewModel;
import com.techdoctorbd.quizapplication.R;

import java.util.List;

public class ListFragment extends Fragment implements QuizItemClickListener {

    private RecyclerView recyclerView;
    private QuizListViewModel quizListViewModel;
    private QuizListAdapter mAdapter;
    private ProgressBar progressBar;
    private Animation fadeInAnim,fadeOutAnim;
    private NavController navController;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_list_fragment);
        progressBar = view.findViewById(R.id.progressBar_list_fragment);
        mAdapter = new QuizListAdapter(this);

        fadeInAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);

        navController = Navigation.findNavController(view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizListViewModel = new ViewModelProvider(requireActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizItem>>() {
            @Override
            public void onChanged(List<QuizItem> quizItems) {

                recyclerView.setAnimation(fadeInAnim);
                progressBar.setAnimation(fadeOutAnim);

                mAdapter.setQuizItems(quizItems);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onQuizItemClicked(int position, ImageView quizImage, TextView quizHeading) {
        ListFragmentDirections.ActionListFragmentToDetailsFragment action = ListFragmentDirections.actionListFragmentToDetailsFragment();
        action.setPosition(position);

        /*FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(quizImage, "header_image")
                .addSharedElement(quizHeading, "header_title")
                .build();*/

        navController.navigate(action);
    }
}
