package com.alphawizard.hdwallet.alphahdwallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphawizard.hdwallet.alphahdwallet.R;

public class WordWrapView extends ViewGroup {

    private  int padding_hor =10;//子view水平方向padding
    private  int padding_vertical=10;//子view垂直方向padding
    private  int margin_hor=20;//子view之间的水平间距
    private  int margin_vertical=20;//行间距


    private int num = 0;//最多字个数

    /**
     * @param context
     */
    public WordWrapView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public WordWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public WordWrapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context,attrs);
    }

    //获取属性值
    private void initAttrs(Context context, AttributeSet attrs) {
//        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.WordWrapView);
//        padding_hor= (int) ta.getDimension(R.styleable.WordWrapView_padding_hor,10);
//        padding_vertical= (int) ta.getDimension(R.styleable.WordWrapView_padding_vertical,10);
//        margin_hor= (int) ta.getDimension(R.styleable.WordWrapView_margin_hor,20);
//        margin_vertical= (int) ta.getDimension(R.styleable.WordWrapView_margin_vertial,20);
//        ta.recycle();
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();
        int acturalWith=r-l;//实际宽度
        int x=0;
        int y=0;
        int rows=1;

        for (int i = 0; i <childCount ; i++) {//判断累积高度
            View view=getChildAt(i);
            int width=view.getMeasuredWidth();
            int height=view.getMeasuredHeight();
            x+=width+margin_hor;
            if(x>acturalWith-margin_hor){
                if(i!=0){
                    x=width+margin_hor;
                    rows++;
                }
            }
            //当一个子view长度超出父view长度时
            if(x>acturalWith-margin_hor){
                if(view instanceof TextView){//判断单个高度
                    TextView tv= (TextView) view;
                    if(num==0){
                        int wordNum=tv.getText().toString().length();
                        num=wordNum*(acturalWith-2*margin_hor-2* padding_hor)/(width-2* padding_hor)-1;
                    }
                    String text=tv.getText().toString();
                    text=text.substring(0,num)+"...";
                    tv.setText(text);
                }
                x=acturalWith-margin_hor;
                width=acturalWith-2*margin_hor;
            }


            y = rows * (height + margin_vertical);
            view.layout(x - width, y - height, x, y);
        }
    }

    public float getCharacterWidth(String text, float size) {
        if (null == text || "".equals(text))
            return 0;
        float width = 0;
        Paint paint = new Paint();
        paint.setTextSize(size);
        float text_width = paint.measureText(text);// 得到总体长度
        width = text_width / text.length();// 每一个字符的长度

        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x=0;//横坐标
        int y=0;//纵坐标
        int rows=1;//总行数
        int specWidth=MeasureSpec.getSize(widthMeasureSpec);
        int acturalWith=specWidth;//实际宽度
        int childCount=getChildCount();
        for (int i = 0; i <childCount ; i++) {
            View child=getChildAt(i);
            child.setPadding(padding_hor,padding_vertical, padding_hor,padding_vertical);
            child.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
            int width=child.getMeasuredWidth();
            int height=child.getMeasuredHeight();
            x+=width+margin_hor;
            if(x>acturalWith-margin_hor){//换行
                if(i!=0){
                    x=width+margin_hor;
                    rows++;
                }
            }
            y=rows*(height+margin_vertical);
        }
        setMeasuredDimension(acturalWith,y+margin_vertical);
    }

    public int getPadding_hor() {
        return padding_hor;
    }

    public void setPadding_hor(int padding_hor) {
        this.padding_hor = padding_hor;
    }

    public int getPadding_vertical() {
        return padding_vertical;
    }

    public void setPadding_vertical(int padding_vertical) {
        this.padding_vertical = padding_vertical;
    }

    public int getMargin_hor() {
        return margin_hor;
    }

    public void setMargin_hor(int margin_hor) {
        this.margin_hor = margin_hor;
    }

    public int getMargin_vertical() {
        return margin_vertical;
    }

    public void setMargin_vertical(int margin_vertical) {
        this.margin_vertical = margin_vertical;
    }
}