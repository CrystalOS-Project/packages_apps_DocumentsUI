/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.documentsui.dirlist;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.MotionEvent;
import android.view.View;

import com.android.documentsui.selection.ItemDetailsLookup;

/**
 * Access to details of an item associated with a {@link MotionEvent} instance.
 */
final class DocsItemDetailsLookup extends ItemDetailsLookup {

    private final RecyclerView mRecView;

    public DocsItemDetailsLookup(RecyclerView view) {
        mRecView = view;
    }

    @Override
    public boolean overItem(MotionEvent e) {
        return getItemPosition(e) != RecyclerView.NO_POSITION;
    }

    @Override
    public boolean overStableItem(MotionEvent e) {
        return overItem(e) && getItemDetails(e).hasStableId();
    }

    @Override
    public boolean inItemDragRegion(MotionEvent e) {
        return overItem(e) && getItemDetails(e).inDragRegion(e);
    }

    @Override
    public boolean inItemSelectRegion(MotionEvent e) {
        return overItem(e) && getItemDetails(e).inSelectionHotspot(e);
    }

    @Override
    public int getItemPosition(MotionEvent e) {
        View child = mRecView.findChildViewUnder(e.getX(), e.getY());
        return (child != null)
                ? mRecView.getChildAdapterPosition(child)
                : RecyclerView.NO_POSITION;
    }

    @Override
    public ItemDetails getItemDetails(MotionEvent e) {
        @Nullable DocumentHolder holder = getDocumentHolder(e);
        return holder == null ? null : holder.getItemDetails();
    }

    private @Nullable DocumentHolder getDocumentHolder(MotionEvent e) {
        View childView = mRecView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            ViewHolder holder = mRecView.getChildViewHolder(childView);
            if (holder instanceof DocumentHolder) {
                return (DocumentHolder) holder;
            }
        }
        return null;
    }
}
