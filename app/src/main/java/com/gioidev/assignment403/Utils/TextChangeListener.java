package com.gioidev.assignment403.Utils;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public interface TextChangeListener {
    void afterTextChanged(Editable editable);
    void onTextChanged(CharSequence s, int start, int before, int count);
    void onClickDrawRight(EditText editText);
    void onFocusChange(View view, boolean b);
}
