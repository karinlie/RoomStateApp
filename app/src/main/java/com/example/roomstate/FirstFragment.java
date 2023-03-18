package com.example.roomstate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.roomstate.databinding.FragmentFirstBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    NetworkTask networkTask;

    boolean isParty = false;
    double loudness = 0;

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

        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(4000L)
                .shapes(new Shape.Rectangle(0.2f))
                .sizes(new Size(40, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleHiveConnection();
            }
        });
        binding.partyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isParty = !isParty;
                if (isParty) {
                    binding.konfettiView.start(party);
                    binding.konfettiView.setBackgroundColor(Color.GREEN);
                } else {
                    binding.konfettiView.setBackgroundColor(Color.TRANSPARENT);
                }
                setEmojiFromLoudness();
            }
        });

        setDefaultText();


    }

    public void dataHandler(String data) {
        getActivity().runOnUiThread(() -> {
            try {
                JSONObject jsonObject = new JSONObject(data.strip()).getJSONObject("RTW");
                if (jsonObject.getString("SENDER").equals("MicrobitErling")) {
                    loudness = jsonObject.getDouble("LOUDNESS");
                    binding.textviewSmall.setText("LOUDNESS: " + String.format("%.2f", loudness));
                    setEmojiFromLoudness();
                }
            } catch (JSONException e) {
                // binding.textviewSmall.setText("Could not get loudness :(");
            }
        });
    }

    public void setEmojiFromLoudness() {
        if (isParty) {
            if (loudness < 0.1){
                binding.textviewFirst.setText("\uD83E\uDD22");
            }
            else if(loudness < 0.3){
                binding.textviewFirst.setText("\uD83E\uDD20");
            }
            else{
                binding.textviewFirst.setText("\uD83E\uDD73");
            }
        } else {
            if (loudness < 0.1){
                binding.textviewFirst.setText("\uD83D\uDE34");
            }
            else if(loudness < 0.3){
                binding.textviewFirst.setText("\uD83D\uDE04");
            }
            else{
                binding.textviewFirst.setText("\uD83D\uDE21");
            }
        }

        binding.textviewFirst.setScaleX(17);
        binding.textviewFirst.setScaleY(17);
    }

    public void setDefaultText() {
        binding.textviewFirst.setText("HIVEry1");
        binding.textviewFirst.setScaleX(7);
        binding.textviewFirst.setScaleY(7);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void toggleHiveConnection() {
        if (networkTask == null) {
            networkTask = new NetworkTask();
            networkTask.execute("https://8group.cioty.com/example1", "4", "token=aToken_36d8715e3531fd8e8c01fcbfd26bf5af1908e14f15014d2d14817b568bc0bb0e&objectID=2&format=json");
            networkTask.dataHandler = (data -> {
                this.dataHandler(data);
                return null;
            });
            binding.buttonFirst.setText("Disconnect");
        } else {
            networkTask.cancel(true);
            networkTask = null;
            binding.buttonFirst.setText("Connect");
            setDefaultText();
        }
    }
}
