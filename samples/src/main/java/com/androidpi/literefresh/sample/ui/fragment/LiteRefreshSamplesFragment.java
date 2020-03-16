/*
 * Copyright 2018 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidpi.literefresh.sample.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter;
import com.androidpi.literefresh.sample.databinding.FragmentLiteRefreshSamplesBinding;
import com.androidpi.literefresh.sample.model.LiteRefreshSamples;
import com.androidpi.literefresh.sample.ui.viewholder.LiteRefreshSampleViewHolder;


@BindLayout(R.layout.fragment_lite_refresh_samples)
public class LiteRefreshSamplesFragment extends BaseFragment<FragmentLiteRefreshSamplesBinding> {

    private RecyclerAdapter adapter;

    public static LiteRefreshSamplesFragment newInstance() {
        Bundle args = new Bundle();
        LiteRefreshSamplesFragment fragment = new LiteRefreshSamplesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapter();
        adapter.register(LiteRefreshSampleViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        adapter.setPayloads(LiteRefreshSamples.INSTANCE.getSamples());
    }
}
