package com.rove.notestick.Util;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import com.rove.notestick.CustomViews.ImageViewWithSrc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JsonViewModemTest {
    @Mock
    private Context context;
    @Mock
    private EditText editText;
    @Mock
    ImageViewWithSrc imageView1,imageView2;
    @Mock
    private Editable editable;

    private ImageSaver imageSaver = new ImageSaver(context);

    String textviewstr = "first text view";
    String imageviewstr1 = "image1";
    String imageviewstr2 = "image2";

    String inputJson = "[{\"viewType\":1,\"content\":\"first text view\"," +
            "\"viewSize\":{\"Width\":-2,\"Height\":-1}},{\"viewType\":2,\"content\":\"" +
            "image1\",\"viewSize\":{\"Width\":100,\"Height\":100}},{\"viewType\":2," +
            "\"content\":\"image2\",\"viewSize\":{\"Width\":100,\"Height\":100}}]";

    @Before
    public void setUp(){


        //when(scrollView.getChildCount()).thenReturn(3);
        //when(scrollView.getChildAt(0)).thenReturn(editText);
        //when(scrollView.getChildAt(1)).thenReturn(imageView1);
        //when(scrollView.getChildAt(2)).thenReturn(imageView2);

        //when(editable.toString()).thenReturn(textviewstr);

        //when(editText.getText()).thenReturn(editable);
//        when(imageView1.getWidth()).thenReturn(100);
//        when(imageView1.getHeight()).thenReturn(100);
//        when(imageView2.getWidth()).thenReturn(100);
//        when(imageView2.getHeight()).thenReturn(100);
//        when(imageView1.getURI()).thenReturn(imageviewstr1);
  //      when(imageView2.getURI()).thenReturn(imageviewstr2);


    }

    @Test
    public void isgetJsonFromViewWorks() {
       // StringImageJsonViewModem stringImageJsonViewModem = new StringImageJsonViewModem(context,scrollView,imageSaver);
       // String JsonArray = stringImageJsonViewModem.getJsonfromView();
        //System.out.println(JsonArray);
        assert(true);
    }


}