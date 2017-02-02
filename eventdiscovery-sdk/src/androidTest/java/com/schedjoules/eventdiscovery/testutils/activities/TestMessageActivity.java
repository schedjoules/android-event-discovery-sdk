/*
 * Copyright 2017 SchedJoules
 *
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

package com.schedjoules.eventdiscovery.testutils.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * An Activity to display a text from a test. Can be used to check Context dependent texts.
 *
 * @author Gabor Keszthelyi
 */
public final class TestMessageActivity extends AppCompatActivity
{
    private static final String EXTRA_MESSAGE = "TestMessageActivity.message";

    private static final Object lock = new Object();


    public static void waitUntilNotified() throws InterruptedException
    {
        synchronized (lock)
        {
            lock.wait();
        }
    }


    public static Intent launch(Context context, String message)
    {
        Intent intent = new Intent(context, TestMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText(getIntent().getStringExtra(EXTRA_MESSAGE));
        textView.setMovementMethod(new ScrollingMovementMethod());

        Button button = new Button(this);
        button.setText("Close");
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                close();
            }
        });

        linearLayout.addView(button);
        linearLayout.addView(textView);

        setContentView(linearLayout);
    }


    private void close()
    {
        synchronized (lock)
        {
            lock.notify();
        }
    }

}
