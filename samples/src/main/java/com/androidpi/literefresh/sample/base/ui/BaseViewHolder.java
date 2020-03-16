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
package com.androidpi.literefresh.sample.base.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Convention prior to configuration:
 *
 * One view holder is bound with one layout, but the rendered data types has no limitation.
 */

public abstract class BaseViewHolder<VDB extends ViewDataBinding> extends RecyclerView.ViewHolder
        implements LifecycleOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    protected VDB binding;
    protected List<Class> dataType = new ArrayList<>();
    protected FragmentManager fragmentManager;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Instantiate a subclass of BaseViewHolder.
     */
    public static BaseViewHolder instance(Class<? extends BaseViewHolder> clazz, ViewGroup parent) {
        if (null == clazz) {
            throw new NullPointerException("The view holder class to be instantiated is null, it may not be " +
                    "registered in RecyclerAdapter or the data types of the objects are not bound with the view holder.");
        }
        BindLayout bindLayout = clazz.getAnnotation(BindLayout.class);
        if (null == bindLayout) {
            throw new NullPointerException("A view holder must be annotated with BindLayout!");
        }
        int layoutId = bindLayout.value();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        try {
            Constructor<? extends BaseViewHolder> constructor = clazz.getDeclaredConstructor(View.class);
            BaseViewHolder viewHolder = constructor.newInstance(itemView);
            viewHolder.binding = DataBindingUtil.bind(itemView);
            viewHolder.dataType = Arrays.asList(bindLayout.dataTypes());
            return viewHolder;
        } catch (NoSuchMethodException e) {
            logError(clazz, e);
        } catch (IllegalAccessException e) {
            Timber.e(e, "%s instantiation error.", clazz);
        } catch (InvocationTargetException e) {
            Timber.e(e, "%s instantiation error.", clazz);
        } catch (InstantiationException e) {
            Timber.e(e, "%s instantiation error.", clazz);
        }
        return null;
    }

    public static BaseViewHolder instance(Class<? extends BaseViewHolder> clazz, ViewGroup parent, FragmentManager fm) {
        BaseViewHolder holder = instance(clazz, parent);
        if (holder != null)
            holder.fragmentManager = fm;
        return holder;
    }

    private static void logError(Class<? extends BaseViewHolder> clazz, NoSuchMethodException e) {
        Timber.e(e, "%s instantiation error.", clazz);
    }

    public final void onBindViewHolder(Object data, int position) {
        if (null == data) {
            Timber.w("Data is null!");
            return;
        }
        if (!dataType.contains(data.getClass())) {
            Timber.e("Data type doesn't match the contract!");
            return;
        }
        onBind(data, position);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.lifecycleRegistry;
    }

    public abstract <T> void onBind(T data, int position);

    protected void onAttachedToWindow() {
        // empty
    }

    protected void onDetachedToWindow() {
        // empty
    }

    public void onViewRecycled() {
        // empty
    }
}
