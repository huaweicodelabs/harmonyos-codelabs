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
import ohos.agp.components.PageSliderProvider;

import java.util.List;

/**
 * Advertising display adapter
 *
 * @param <T> type
 * @since 2021-09-07
 */
public class AdvertisementProvider<T extends Component> extends PageSliderProvider {
    private final List<T> componentList;

    /**
     * constructor
     *
     * @param componentList component list
     */
    public AdvertisementProvider(List<T> componentList) {
        this.componentList = componentList;
    }

    /**
     * get count
     *
     * @return int
     */
    public int getCount() {
        return componentList.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int index) {
        componentContainer.addComponent(componentList.get(index));
        return componentList.get(index);
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int index, Object obj) {
        componentContainer.removeComponent(componentList.get(index));
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object obj) {
        return component == obj;
    }
}
