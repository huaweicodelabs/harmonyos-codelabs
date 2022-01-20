/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
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

@Component
struct Search_Input {
  @Link historyArr: string[]
  @State private historyInput: string = ''

  build() {
    Flex({ alignItems: ItemAlign.Center }) {
      // @ts-ignore
      TextInput({ placeholder: '请输入...', text: this.historyInput })
        .type(InputType.Normal)
        .placeholderColor(Color.Gray)
        .placeholderFont({ size: 20, weight: 2 })
        .enterKeyType(EnterKeyType.Search)
        .caretColor(Color.Green)
        .layoutWeight(8)
        .height(40)
        .borderRadius('20px')
        .backgroundColor(Color.White)
        .onChange((value: string) => {
        this.historyInput = value
      })

      Button({ type: ButtonType.Capsule, stateEffect: false }) {
        Text('搜索').fontSize(17).fontColor(Color.Blue)
      }
      .layoutWeight(2)
      .backgroundColor('#00000000')
      .onClick((event: ClickEvent) => {
        if (this.historyInput != null && this.historyInput.length > 0) {
          this.historyArr.unshift(this.historyInput)
          this.historyInput = ''
        }
      })
    }
    .height(60)
    .padding({ left: 10 })
    .backgroundColor('#FFedf2f5')
  }
}

@Component
struct Clear_History {
  @Link historyArr: string[]

  build() {
    Flex({ alignItems: ItemAlign.Center }) {
      if (this.historyArr.length > 0) {
        Text('历史记录')
          .fontSize(14)
          .fontColor(Color.Grey)
          .layoutWeight(8)

        Text('清空')
          .fontSize(14)
          .fontColor(Color.Grey)
          .layoutWeight(2)
          .onClick((event: ClickEvent) => {
          this.historyArr.splice(0, this.historyArr.length)
          this.historyArr.length = 0
        })
      }
    }
    .height(40)
    .padding({ left: 20, top: 10 })
  }
}

@Component
struct FlowLayout_Container {
  @Link historyArr: string[]

  build() {
    Scroll() {
      Flex({ justifyContent: FlexAlign.Start, wrap: FlexWrap.Wrap }) {
        if (this.historyArr.length > 0) {
          ForEach(this.historyArr,
            (item: string) => {
              Text(`${item}`)
                .fontSize(18)
                .borderStyle(BorderStyle.Solid)
                .borderWidth('1px')
                .borderColor('#dddddd')
                .borderRadius('90px')
                .padding({ top: 4, bottom: 4, right: 10, left: 10 })
                .margin({ top: 10, right: 10 })
                .textOverflow({ overflow: TextOverflow.Ellipsis })
                .maxLines(2)
            },
            (item: string) => item.toString()
          )
        }
      }
      .margin({ left: 20, bottom: 100, right: 10 })
      .padding({ bottom: 10 })
    }
  }
}

@Entry
@Component
struct Search_FlowLayout {
  @State private historyArr: string[] = ["Text", "Button", "TextField", "Image", "Switch", "Checkbox", "RadioButton", "ProgressBar", "ScrollView"]

  build() {
    Column() {
      Search_Input({ historyArr: $historyArr })
      Clear_History({ historyArr: $historyArr })
      FlowLayout_Container({ historyArr: $historyArr })
    }
    .alignItems(HorizontalAlign.Center)
  }
}