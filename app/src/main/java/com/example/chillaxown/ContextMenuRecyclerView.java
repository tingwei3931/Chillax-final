package com.example.chillaxown;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

public class ContextMenuRecyclerView extends RecyclerView {
    private ContextMenu.ContextMenuInfo mContextMenuInfo;

    public ContextMenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(View view) {
        super(view.getContext());
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    /**
     * Used to initialize before creating context menu and Bring up the context menu for this view.
     *
     * @param position for ContextMenuInfo
     */
    public void openContextMenu(int position) {
        if (position >= 0) {
            final long childId = getAdapter().getItemId(position);
            mContextMenuInfo = createContextMenuInfo(position, childId);
        }
        showContextMenu();
    }

    ContextMenu.ContextMenuInfo createContextMenuInfo(int position, long id) {
        return new RecyclerViewContextMenuInfo(position, id);
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int longPressPosition = getChildAdapterPosition(originalView);
        if (longPressPosition >= 0) {
            final long longPressId = getAdapter().getItemId(longPressPosition);
            mContextMenuInfo = new RecyclerViewContextMenuInfo(longPressPosition, longPressId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    public static class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public RecyclerViewContextMenuInfo(int position, long id) {
            this.position = position;
            this.id = id;
        }

        final public int position;
        final public long id;
    }

}
