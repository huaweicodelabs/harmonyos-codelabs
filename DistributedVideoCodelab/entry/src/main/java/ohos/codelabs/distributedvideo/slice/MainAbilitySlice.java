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

package ohos.codelabs.distributedvideo.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderIndicator;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.codelabs.distributedvideo.MainAbility;
import ohos.codelabs.distributedvideo.PlayerAbility;
import ohos.codelabs.distributedvideo.ResourceTable;
import ohos.codelabs.distributedvideo.data.AdvertisementMo;
import ohos.codelabs.distributedvideo.data.VideoListMo;
import ohos.codelabs.distributedvideo.data.VideoTabStyle;
import ohos.codelabs.distributedvideo.provider.AdvertisementProvider;
import ohos.codelabs.distributedvideo.provider.CommonProvider;
import ohos.codelabs.distributedvideo.provider.ViewProvider;
import ohos.codelabs.distributedvideo.util.LogUtil;
import ohos.codelabs.distributedvideo.util.MediaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * the main page
 *
 * @since 2021-09-07
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final String TAG = MainAbility.class.getSimpleName();
    private static final String INTENT_STARTTIME_PARAM = "intetn_starttime_param";
    private AdvertisementProvider<Component> advertisementProvider;
    private PageSlider advPageSlider;
    private ScheduledExecutorService scheduledExecutor;
    private int currentPageIndex = 0;
    private Runnable slidTask;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_video);
        initContent();
    }

    private void initContent() {
        initScheduledExecutor();
        initAdvertisement();
        initList();
        startSlidTimer();
    }

    private void initScheduledExecutor() {
        slidTask = () -> getUITaskDispatcher().asyncDispatch(() -> {
            if (currentPageIndex == advertisementProvider.getCount()) {
                currentPageIndex = 0;
            } else {
                currentPageIndex++;
            }
            advPageSlider.setCurrentPage(currentPageIndex);
        });
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    }

    private void initList() {
        LogUtil.info(TAG, "begin updateTableView tab view");
        Text playTitle = null;
        if (findComponentById(ResourceTable.Id_video_play_title) instanceof Text) {
            playTitle = (Text) findComponentById(ResourceTable.Id_video_play_title);
        }
        Font.Builder fb = new Font.Builder(VideoTabStyle.BOLD_FONT_NAME);
        fb.setWeight(Font.BOLD);
        Font newFont = fb.build();
        playTitle.setFont(newFont);
        CommonProvider<VideoListMo> commonProvider = new CommonProvider<VideoListMo>(
                MediaUtil.getPlayListMos(),
                getContext(),
                ResourceTable.Layout_recommend_gv_item) {
            @Override
            protected void convert(ViewProvider holder, VideoListMo item, int position) {
                holder.setText(ResourceTable.Id_name_tv, item.getName());
                holder.setText(ResourceTable.Id_content_tv, item.getDescription());
                holder.setImageResource(ResourceTable.Id_image_iv, item.getSourceId());
            }
        };
        ListContainer playListContainer = null;
        if (findComponentById(ResourceTable.Id_video_list_play_view) instanceof ListContainer) {
            playListContainer = (ListContainer) findComponentById(ResourceTable.Id_video_list_play_view);
        }
        playListContainer.setItemProvider(commonProvider);
        playListContainer.setItemClickedListener((listContainer, component, index, position) -> startFa());
    }

    private void initAdvertisement() {
        advertisementProvider = new AdvertisementProvider<>(getAdvertisementComponents());
        Component advViewPager = findComponentById(ResourceTable.Id_video_advertisement_viewpager);
        if (advViewPager instanceof PageSlider) {
            advPageSlider = (PageSlider) advViewPager;
            advPageSlider.setProvider(advertisementProvider);
            advPageSlider.setClickedListener(component -> startFa());
        } else {
            LogUtil.debug(TAG, "initAdvertisement failed, advertisement_viewpager is not PageSlider");
        }
        if (findComponentById(ResourceTable.Id_video_advertisement_indicator) instanceof PageSliderIndicator) {
            PageSliderIndicator advIndicator = (PageSliderIndicator) findComponentById(
                    ResourceTable.Id_video_advertisement_indicator);
            advIndicator.setItemOffset(VideoTabStyle.INDICATOR_OFFSET);
            ShapeElement normalDrawable = new ShapeElement();
            normalDrawable.setRgbColor(RgbColor.fromRgbaInt(Color.WHITE.getValue()));
            normalDrawable.setAlpha(VideoTabStyle.INDICATOR_NORMA_ALPHA);
            normalDrawable.setShape(ShapeElement.OVAL);
            normalDrawable.setBounds(0, 0, VideoTabStyle.INDICATOR_BONDS, VideoTabStyle.INDICATOR_BONDS);
            ShapeElement selectedDrawable = new ShapeElement();
            selectedDrawable.setRgbColor(RgbColor.fromRgbaInt(Color.WHITE.getValue()));
            selectedDrawable.setShape(ShapeElement.OVAL);
            selectedDrawable.setBounds(0, 0, VideoTabStyle.INDICATOR_BONDS, VideoTabStyle.INDICATOR_BONDS);
            advIndicator.setItemElement(normalDrawable, selectedDrawable);
            advIndicator.setViewPager((PageSlider) advViewPager);
        }
    }

    private void startFa() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withBundleName(getBundleName())
                .withAbilityName(PlayerAbility.class.getName()).build();
        intent.setOperation(operation);
        intent.setParam(INTENT_STARTTIME_PARAM, 0);
        startAbility(intent);
    }

    private List<Component> getAdvertisementComponents() {
        List<AdvertisementMo> advertisementMos = MediaUtil.getVideoAdvertisementInfo();
        List<Component> componentList = new ArrayList<>(advertisementMos.size());
        Font.Builder fb = new Font.Builder(VideoTabStyle.BOLD_FONT_NAME);
        fb.setWeight(Font.BOLD);
        Font newFont = fb.build();
        for (AdvertisementMo advertisementMo : advertisementMos) {
            Component advRootView = LayoutScatter.getInstance(getContext()).parse(
                    ResourceTable.Layout_video_advertisement_item, null, false);
            Image imgTemp = null;
            if (advRootView.findComponentById(ResourceTable.Id_video_advertisement_poster) instanceof Image) {
                imgTemp = (Image) advRootView.findComponentById(ResourceTable.Id_video_advertisement_poster);
            }
            imgTemp.setPixelMap(advertisementMo.getSourceId());
            Text titleTmp = null;
            if (advRootView.findComponentById(ResourceTable.Id_video_advertisement_title) instanceof Text) {
                titleTmp = (Text) advRootView.findComponentById(ResourceTable.Id_video_advertisement_title);
            }
            titleTmp.setText(advertisementMo.getDescription());
            titleTmp.setFont(newFont);
            componentList.add(advRootView);
        }

        return componentList;
    }

    private void startSlidTimer() {
        scheduledExecutor.scheduleAtFixedRate(slidTask, VideoTabStyle.ADVERTISEMENT_SLID_PROID,
                VideoTabStyle.ADVERTISEMENT_SLID_DELAY, TimeUnit.SECONDS);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onInactive() {
        super.onInactive();
        LogUtil.info(TAG, "is onInactive");
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
        LogUtil.info(TAG, "is onForeground");
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtil.info(TAG, "is onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.info(TAG, "is onStop");
    }

    @Override
    protected void onBackPressed() {
        LogUtil.info(TAG, "is onBackPressed");
        super.onBackPressed();
    }
}
