package com.example.roomstate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.roomstate.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private boolean happy = false;
    NetworkTask networkTask = new NetworkTask();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavHostFragment.findNavController(FirstFragment.this)
                //        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                // happy = !happy;
                // binding.textviewFirst.setText(happy ? "\uD83D\uDE04" : "\uD83D\uDE12");
                toggleHiveConnection();
            }
        });

        binding.textviewFirst.setText("HIVEry1");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void toggleHiveConnection() {
        if (!networkTask.isConnected) {
            networkTask.execute("https://brain.cioty.com/example", "4", "token=aToken_36d8715e3531fd8e8c01fcbfd26bf5af1908e14f15014d2d14817b568bc0bb0e&objectID=1&format=json");
            binding.buttonFirst.setText("Disconnect");
        } else {
            networkTask.shouldClose = true;
            binding.buttonFirst.setText("Connect");
        }
    }
}