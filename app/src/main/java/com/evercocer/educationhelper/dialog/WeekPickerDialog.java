package com.evercocer.educationhelper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.ui.layouts.WeekPickLayout;
import com.evercocer.educationhelper.ui.week.ItemPickView;

import java.util.ArrayList;
import java.util.Random;

public class WeekPickerDialog extends Dialog {
    private View decorView;
    private Window window;
    private WindowManager.LayoutParams layoutParams;

    private ArrayList<int[]> colors;
    private Random random;
    private int currentWeek;

    private ImageView close;
    private ImageView check;
    private WeekPickLayout container;
    private EditText title;

    private Listener listener;


    private WeekPickerDialog(@NonNull Context context) {
        this(context, 0);
    }

    private WeekPickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        window = getWindow();
        decorView = window.getDecorView();
        layoutParams = window.getAttributes();

    }

    private WeekPickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(false);
        for (int index = 0, i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                index++;
                ItemPickView itemPickView = new ItemPickView(getContext());

                if (index > 18)
                    itemPickView.setVisibility(View.GONE);
                if (index < currentWeek)
                    itemPickView.setPassed(true);
                itemPickView.setColor(getRandomColor());
                itemPickView.setWeek(index);
                itemPickView.setClickable(false);
                itemPickView.setClickListener(new ItemPickView.ClickListener() {
                    @Override
                    public void actionUP(String week) {
                        title.setText(week);
                    }
                });
                container.addView(itemPickView);
            }
        }

        title.setText(String.valueOf(currentWeek));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public static class Builder {
        private final WeekPickerDialog dialog;

        public Builder(Context context) {
            this.dialog = new WeekPickerDialog(context);
        }

        public Builder setContentView(View v) {
            dialog.setContentView(v);
            return this;
        }

        public Builder setContentView(int layoutResID) {
            dialog.setContentView(layoutResID);
            dialog.close = dialog.decorView.findViewById(R.id.imageView_close);
            dialog.check = dialog.decorView.findViewById(R.id.imageView_check);
            dialog.title = dialog.decorView.findViewById(R.id.textView2);
            dialog.container = dialog.decorView.findViewById(R.id.dialog_week_content);
            dialog.container.setHang(5);
            dialog.container.setLie(7);

            return this;
        }

        public Builder setLocation(int location) {
            dialog.layoutParams.gravity = location;
            return this;
        }

        public Builder setSize(int width, int height) {
            dialog.layoutParams.width = width;
            dialog.layoutParams.height = height;
            DisplayMetrics displayMetrics = dialog.getContext().getResources().getDisplayMetrics();
            if (width == 0) dialog.layoutParams.width = displayMetrics.widthPixels;
            return this;
        }

        public Builder setCurrentWeek(int week) {
            dialog.currentWeek = week;
            return this;
        }

        public WeekPickerDialog build() {
            return dialog;
        }

        public Builder setListener(Listener listener) {
            dialog.listener = listener;
            dialog.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.listener.check(dialog.title.getText().toString());
                }
            });

            return this;
        }


    }

    //初始化colors
    private void initColor(ArrayList<int[]> colors_list) {
        int[] color1 = {25, 202, 173};
        int[] color2 = {236, 173, 158};
        int[] color3 = {190, 231, 233};
        int[] color4 = {214, 213, 183};
        int[] color5 = {244, 96, 108};
        int[] color6 = {209, 186, 116};
        int[] color7 = {190, 237, 199};
        int[] color8 = {230, 206, 172};
        int[] color9 = {140, 199, 181};
        int[] color10 = {160, 238, 225};

        colors_list.add(color1);
        colors_list.add(color2);
        colors_list.add(color3);
        colors_list.add(color4);
        colors_list.add(color5);
        colors_list.add(color6);
        colors_list.add(color7);
        colors_list.add(color8);
        colors_list.add(color9);
        colors_list.add(color10);
    }

    //获取随机的颜色
    public int[] getRandomColor() {
        if (random == null)
            random = new Random();

        if (colors == null) {
            colors = new ArrayList<>();
            initColor(colors);
        }
        int i = random.nextInt(10);
        switch (i) {
            case 0:
                return colors.get(0);
            case 1:
                return colors.get(1);
            case 2:
                return colors.get(2);
            case 3:
                return colors.get(3);
            case 4:
                return colors.get(4);
            case 5:
                return colors.get(5);
            case 6:
                return colors.get(6);
            case 7:
                return colors.get(7);
            case 8:
                return colors.get(8);
            case 9:
                return colors.get(9);
            default:
                return colors.get(0);
        }
    }


    public interface Listener {
        void check(String week);
    }

}
