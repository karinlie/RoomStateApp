package com.example.roomstate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.roomstate.databinding.FragmentFirstBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
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
                toggleHiveConnection();
            }
        });

        binding.textviewFirst.setText("HIVEry1");

        networkTask.dataHandler = (data -> {
            this.dataHandler(data);
            return null;
        });
    }

    public void dataHandler(String data) {
        getActivity().runOnUiThread(() -> {
            try {
                JSONObject jsonObject = new JSONObject(data.strip()).getJSONObject("RTW");
                if (jsonObject.getString("SENDER").equals("MicrobitErling")) {
                    double loudness = jsonObject.getDouble("LOUDNESS");
                    binding.textviewSmall.setText("LOUDNESS: " + String.format("%.2f", loudness));
                    setEmojiFromLoudness(loudness);
                }
            } catch (JSONException e) {
                // binding.textviewSmall.setText("Could not get loudness :(");
            }
        });
    }

    public void setEmojiFromLoudness(double loudness) {
        binding.textviewFirst.setText(loudness < 0.5 ? "\uD83D\uDE04" : "\uD83D\uDE21");
        binding.textviewFirst.setScaleX(17);
        binding.textviewFirst.setScaleY(17);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void toggleHiveConnection() {
        // TODO: Change this
        //Random r = new Random();
        //setEmojiFromLoudness(r.nextInt(2));

        if (!networkTask.isConnected) {
            networkTask.execute("https://8group.cioty.com/example1", "4", "token=aToken_36d8715e3531fd8e8c01fcbfd26bf5af1908e14f15014d2d14817b568bc0bb0e&objectID=2&format=json");
            binding.buttonFirst.setText("Disconnect");
        } else {
            networkTask.shouldClose = true;
            binding.buttonFirst.setText("Connect");
        }
    }
}
