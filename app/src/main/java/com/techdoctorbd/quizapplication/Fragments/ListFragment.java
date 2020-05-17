package com.techdoctorbd.quizapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techdoctorbd.quizapplication.Adapters.QuizListAdapter;
import com.techdoctorbd.quizapplication.Models.QuizItem;
import com.techdoctorbd.quizapplication.Models.QuizListViewModel;
import com.techdoctorbd.quizapplication.R;

import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuizListViewModel quizListViewModel;
    private QuizListAdapter mAdapter;

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
        mAdapter = new QuizListAdapter();

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
                mAdapter.setQuizItems(quizItems);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
