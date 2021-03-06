/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.broadcast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.zhanghai.android.douya.network.api.info.frodo.Broadcast;
import me.zhanghai.android.douya.ui.FragmentFinishable;
import me.zhanghai.android.douya.util.FragmentUtils;
import me.zhanghai.android.douya.util.TransitionUtils;

public class BroadcastActivity extends AppCompatActivity implements FragmentFinishable {

    private static final String KEY_PREFIX = BroadcastActivity.class.getName() + '.';

    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";
    private static final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";
    private static final String EXTRA_SHOW_SEND_COMMENT = KEY_PREFIX + "show_send_comment";
    private static final String EXTRA_TITLE = KEY_PREFIX + "title";

    private BroadcastFragment mFragment;

    private boolean mShouldFinish;

    public static Intent makeIntent(long broadcastId, Context context) {
        return new Intent(context, BroadcastActivity.class)
                .putExtra(EXTRA_BROADCAST_ID, broadcastId);
    }

    public static Intent makeIntent(Broadcast broadcast, Context context) {
        return makeIntent(broadcast.id, context)
                .putExtra(EXTRA_BROADCAST, broadcast);
    }

    public static Intent makeIntent(Broadcast broadcast, boolean showSendComment, String title,
                                    Context context) {
        return makeIntent(broadcast, context)
                .putExtra(EXTRA_SHOW_SEND_COMMENT, showSendComment)
                .putExtra(EXTRA_TITLE, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TransitionUtils.setupTransitionBeforeDecorate(this);

        super.onCreate(savedInstanceState);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        TransitionUtils.postponeTransition(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            long broadcastId = intent.getLongExtra(EXTRA_BROADCAST_ID, -1);
            Broadcast broadcast = intent.getParcelableExtra(EXTRA_BROADCAST);
            boolean showSendComment = intent.getBooleanExtra(EXTRA_SHOW_SEND_COMMENT, false);
            String title = intent.getStringExtra(EXTRA_TITLE);
            mFragment = BroadcastFragment.newInstance(broadcastId, broadcast, showSendComment,
                    title);
            FragmentUtils.add(mFragment, this, android.R.id.content);
        } else {
            mFragment = FragmentUtils.findById(this, android.R.id.content);
        }
    }

    @Override
    public void finish() {
        if (!mShouldFinish) {
            mFragment.onFinish();
            return;
        }
        super.finish();
    }

    @Override
    public void finishAfterTransition() {
        if (!mShouldFinish) {
            mFragment.onFinish();
            return;
        }
        super.finishAfterTransition();
    }

    @Override
    public void finishFromFragment() {
        mShouldFinish = true;
        super.finish();
    }

    @Override
    public void finishAfterTransitionFromFragment() {
        mShouldFinish = true;
        super.supportFinishAfterTransition();
    }
}
