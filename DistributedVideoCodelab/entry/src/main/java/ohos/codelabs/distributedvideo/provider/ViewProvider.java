/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ohos.codelabs.distributedvideo.provider;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.HashMap;

/**
 * ViewProvider
 *
 * @since 2020-12-04
 *
 */
public class ViewProvider {
    /**
     * set Position
     */
    protected int componentPosition;
    /**
     * set layout id
     */
    protected int layoutId;
    private Component component;
    private Context context;
    private HashMap<Integer, Component> views;

    /**
     * constructor of ViewProvider
     *
     * @param context context
     * @param itemView itemView
     * @param parent parent
     * @param position position
     */
    public ViewProvider(Context context, Component itemView, ComponentContainer parent, int position) {
        this.context = context;
        component = itemView;
        this.componentPosition = position;
        views = new HashMap<Integer, Component>(0);
        component.setTag(this);
    }

    /**
     * constructor of ViewProvider
     *
     * @param context context
     * @param convertView convertView
     * @param parent parent
     * @param layoutId layoutId
     * @param position position
     * @return ViewProvider
     */
    public static ViewProvider get(Context context, Component convertView, ComponentContainer parent,
        int layoutId, int position) {
        if (convertView == null) {
            Component itemView = LayoutScatter.getInstance(context).parse(layoutId, null, false);
            ViewProvider viewProvider = new ViewProvider(context, itemView, parent, position);
            viewProvider.layoutId = layoutId;
            return viewProvider;
        } else {
            ViewProvider viewProvider = null;
            Object object = convertView.getTag();
            if (object instanceof ViewProvider) {
                viewProvider = (ViewProvider) object;
                viewProvider.componentPosition = position;
            }
            return viewProvider;
        }
    }

    /**
     * Get the control by viewId
     *
     * @param viewId viewId
     * @param <T> generic
     * @return view
     */
    public <T extends Component> T getView(int viewId) {
        Component view = views.get(viewId);
        if (view == null) {
            view = component.findComponentById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * return Component
     *
     * @return Component
     */
    public Component getComponentView() {
        return component;
    }

    /**
     * get layout id
     *
     * @return xmlId
     */
    public int getLayoutId() {
        return layoutId;
    }

    /**
     * update pointer
     *
     * @param position position
     */
    public void updatePosition(int position) {
        this.componentPosition = position;
    }

    /**
     * get item position
     *
     * @return int
     */
    public int getItemPosition() {
        return componentPosition;
    }

    /**
     * set text
     *
     * @param viewId viewId
     * @param text text
     * @return ViewProvider
     */
    public ViewProvider setText(int viewId, String text) {
        Text tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * set image
     *
     * @param viewId viewId
     * @param resId ImageResource
     * @return ViewProvider
     */
    public ViewProvider setImageResource(int viewId, int resId) {
        Image image = getView(viewId);
        image.setPixelMap(resId);
        image.setScaleMode(Image.ScaleMode.STRETCH);
        return this;
    }

    /**
     * set onClick Listener
     *
     * @param viewId viewId
     * @param listener listener
     * @return ViewProvider
     */
    public ViewProvider setOnClickListener(int viewId, Component.ClickedListener listener) {
        Component newComponent = getView(viewId);
        newComponent.setClickedListener(listener);
        return this;
    }

    /**
     * set OnTouch Listener
     *
     * @param viewId viewId
     * @param listener listener
     * @return ViewProvider
     */
    public ViewProvider setOnTouchListener(int viewId, Component.TouchEventListener listener) {
        Component newComponent = getView(viewId);
        newComponent.setTouchEventListener(listener);
        return this;
    }
}
